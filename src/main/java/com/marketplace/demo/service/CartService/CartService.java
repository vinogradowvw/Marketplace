package com.marketplace.demo.service.CartService;

import com.marketplace.demo.domain.*;
import com.marketplace.demo.persistance.CartRepository;
import com.marketplace.demo.persistance.ProductRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import com.marketplace.demo.service.OrderService.OrderService;
import com.marketplace.demo.service.PaymentService.PaymentService;
import com.marketplace.demo.service.ProductService.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;


@Service
@Transactional
@AllArgsConstructor
public class CartService extends CrudServiceImpl<Cart, Long> implements CartServiceInterface{

    private CartRepository cartRepository;
    private OrderService orderService;
    private PaymentService paymentService;
    private ProductRepository productRepository;

    @Override
    public Cart addProduct(Cart cart, Product product, Long quantity) {
        if (!cartRepository.existsById(cart.getID())){
            throw new IllegalArgumentException("There is no cart with id: " + cart.getID());
        }

        if (!productRepository.existsById(product.getID())){
            throw new IllegalArgumentException("There is no product with id: " + product.getID());
        }

        cart.getProducts().put(product, quantity);
        cart.setTimestamp(new Timestamp(System.currentTimeMillis()));

        product.getCarts().add(cart);
        productRepository.save(product);

        return cartRepository.save(cart);
    }

    @Override
    public Cart createOrder(Cart cart) {
        cart.getProducts().keySet().forEach(p -> p.getCarts().remove(cart));

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTimestamp(new Timestamp(System.currentTimeMillis()));
        order.setState(State.NOT_PAID);
        order.setProducts(cart.getProducts());

        order.getProducts().forEach((p, q) -> {
            p.getOrders().add(order);
            productRepository.save(p);
        });

        Payment payment = new Payment();
        payment.setOrder(order);
        Long amount = cart.getProducts().entrySet()
                .stream()
                .mapToLong(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
        payment.setAmount(amount);

        orderService.create(order);
        paymentService.create(payment);

        cart.getProducts().clear();
        cart.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return cartRepository.save(cart);
    }

    @Override
    protected CrudRepository<Cart, Long> getRepository() {
        return cartRepository;
    }
}
