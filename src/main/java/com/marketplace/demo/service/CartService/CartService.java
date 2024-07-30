package com.marketplace.demo.service.CartService;

import com.marketplace.demo.domain.*;
import com.marketplace.demo.persistance.CartRepository;
import com.marketplace.demo.persistance.ProductRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import com.marketplace.demo.service.OrderService.OrderService;
import com.marketplace.demo.service.PaymentService.PaymentService;
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
    public Cart deleteProduct(Cart cart, Product product, Long quantity) {
        if (!cartRepository.existsById(cart.getID())){
            throw new IllegalArgumentException("There is no cart with id: " + cart.getID());
        }

        if (!productRepository.existsById(product.getID())){
            throw new IllegalArgumentException("There is no product with id: " + product.getID());
        }

        if (!cart.getProducts().containsKey(product)){
            throw new IllegalArgumentException("There is no product with id: " + product.getID() + " in the cart.");
        }

        if (cart.getProducts().get(product).compareTo(quantity) < 0){
            throw new IllegalArgumentException("There are fewer products with id: " + product.getID() + " in the cart.");
        }

        if (cart.getProducts().get(product).equals(quantity)){
            cart.getProducts().remove(product);

            product.getCarts().remove(cart);
            productRepository.save(product);
        }
        else{
            cart.getProducts().put(product, cart.getProducts().get(product) - quantity);
        }

        return cartRepository.save(cart);
    }

    @Override
    public Cart clearCart(Cart cart) {
        if (!cartRepository.existsById(cart.getID())){
            throw new IllegalArgumentException("There is no cart with id: " + cart.getID());
        }

        cart.getProducts().forEach((p, q) -> {
            p.getCarts().remove(cart);
            productRepository.save(p);
        });
        cart.getProducts().clear();

        return cartRepository.save(cart);
    }

    @Override
    public Order createOrder(Cart cart) {
        if (cart.getProducts().isEmpty()){
            throw new IllegalArgumentException("There is no products in cart with id: " + cart.getID());
        }

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
        order.getUser().getOrders().add(order);

        Payment payment = new Payment();
        payment.setOrder(order);
        Long amount = cart.getProducts().entrySet()
                .stream()
                .mapToLong(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
        payment.setAmount(amount);

        order.setPayment(payment);

        paymentService.create(payment);

        cart.getProducts().clear();
        cart.setTimestamp(new Timestamp(System.currentTimeMillis()));
        cartRepository.save(cart);

        return orderService.create(order);
    }

    @Override
    protected CrudRepository<Cart, Long> getRepository() {
        return cartRepository;
    }
}
