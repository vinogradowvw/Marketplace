package com.marketplace.demo.service.CartService;

import com.marketplace.demo.domain.*;
import com.marketplace.demo.persistance.*;
import com.marketplace.demo.service.CrudServiceImpl;
import com.marketplace.demo.service.PaymentService.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CartService extends CrudServiceImpl<Cart, Long> implements CartServiceInterface{

    private CartRepository cartRepository;
    private CartProductRepository cartProductRepository;
    private OrderProductRepository orderProductRepository;
    private OrderRepository orderRepository;
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

        if (product.getPost().getUser().getID().equals(cart.getUser().getID())){
            throw new IllegalArgumentException("User cannot buy his product.");
        }

        if (cartProductRepository.existsByCartAndProduct(cart, product)){
            CartProduct.CartProductId cartProductId = new CartProduct.CartProductId(cart.getID(), product.getID());
            CartProduct cartProduct = cartProductRepository.findById(cartProductId).get();

            cartProduct.setQuantity(quantity + cartProduct.getQuantity());
            cartProductRepository.save(cartProduct);

            return cartRepository.save(cart);
        }

        CartProduct.CartProductId cartProductId = new CartProduct.CartProductId(cart.getID(), product.getID());
        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(cartProductId);
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(quantity);
        cartProductRepository.save(cartProduct);

        cart.getProducts().add(cartProduct);
        cart.setTimestamp(new Timestamp(System.currentTimeMillis()));

        product.getCarts().add(cartProduct);
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

        if (!cartProductRepository.existsByCartAndProduct(cart, product)){
            throw new IllegalArgumentException("There is no product with id: " + product.getID() + " in the cart.");
        }

        CartProduct.CartProductId cartProductId = new CartProduct.CartProductId(cart.getID(), product.getID());
        CartProduct cartProduct = cartProductRepository.findById(cartProductId).get();
        Long productQuantity = cartProduct.getQuantity();
        if (productQuantity.compareTo(quantity) < 0){
            throw new IllegalArgumentException("There are fewer products with id: " + product.getID() + " in the cart.");
        }

        if (productQuantity.equals(quantity)){
            cart.getProducts().remove(cartProduct);

            product.getCarts().remove(cartProduct);
            cartProductRepository.delete(cartProduct);
            productRepository.save(product);
        }
        else{
            cartProduct.setQuantity(productQuantity - quantity);
            cartProductRepository.save(cartProduct);
        }

        return cartRepository.save(cart);
    }

    @Override
    public Cart clearCart(Cart cart) {
        if (!cartRepository.existsById(cart.getID())){
            throw new IllegalArgumentException("There is no cart with id: " + cart.getID());
        }

        cart.getProducts().forEach(p -> {
            Product product = p.getProduct();
            product.getCarts().remove(p);
            productRepository.save(product);

            cartProductRepository.delete(p);
        });

        cart.getProducts().clear();
        cart.setTimestamp(new Timestamp(System.currentTimeMillis()));

        return cartRepository.save(cart);
    }

    @Override
    public List<Product> getProducts(Cart cart) {
        if (!cartRepository.existsById(cart.getID())){
            throw new IllegalArgumentException("There is no cart with id: " + cart.getID());
        }

        List<Product> products = new ArrayList<>();

        cart.getProducts().forEach(p -> products.add(p.getProduct()));

        return products;
    }

    @Override
    public Order createOrder(Cart cart) {
        if (!cartRepository.existsById(cart.getID())){
            throw new IllegalArgumentException("There is no cart with id: " + cart.getID());
        }

        if (cart.getProducts().isEmpty()){
            throw new IllegalArgumentException("There is no products in cart with id: " + cart.getID());
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTimestamp(new Timestamp(System.currentTimeMillis()));
        order.setState(State.NOT_PAID);

        Order savedOrder = orderRepository.save(order);

        cart.getProducts().forEach(p -> {
            Product product = p.getProduct();
            Long quantity = p.getQuantity();

            OrderProduct.OrderProductId orderProductId = new OrderProduct.OrderProductId(savedOrder.getID(), product.getID());
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setId(orderProductId);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(quantity);
            orderProduct.setOrder(savedOrder);
            orderProductRepository.save(orderProduct);

            product.getOrders().add(orderProduct);
            productRepository.save(product);
        });
        savedOrder.getUser().getOrders().add(savedOrder);

        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        Long amount = cart.getProducts().stream()
                .mapToLong(entry -> entry.getProduct().getPrice() * entry.getQuantity())
                .sum();
        payment.setAmount(amount);
        paymentService.create(payment);

        savedOrder.setPayment(payment);
        orderRepository.save(savedOrder);

        this.clearCart(cart);

        return savedOrder;
    }

    @Override
    protected CrudRepository<Cart, Long> getRepository() {
        return cartRepository;
    }
}
