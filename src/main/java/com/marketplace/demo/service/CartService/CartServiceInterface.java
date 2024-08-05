package com.marketplace.demo.service.CartService;

import com.marketplace.demo.domain.Cart;
import com.marketplace.demo.domain.Order;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.service.CrudService;

import java.util.List;

public interface CartServiceInterface extends CrudService<Cart, Long> {

    public Order createOrder(Cart cart);

    public Cart addProduct(Cart cart, Product product, Long quantity);
    public Cart deleteProduct(Cart cart, Product product, Long quantity);

    public Cart clearCart(Cart cart);

    public List<Product> getProducts(Cart cart);
}
