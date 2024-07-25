package com.marketplace.demo.service.CartService;

import com.marketplace.demo.domain.Cart;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.service.CrudService;

public interface CartServiceInterface extends CrudService<Cart, Long> {

    public Cart createOrder(Cart cart);
    public Cart addProduct(Cart cart, Product product, Long quantity);
}
