package com.marketplace.demo.persistance;

import com.marketplace.demo.domain.Cart;
import com.marketplace.demo.domain.CartProduct;
import com.marketplace.demo.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface CartProductRepository extends CrudRepository<CartProduct, CartProduct.CartProductId> {

    public boolean existsByCartAndProduct(Cart cart, Product product);
    public boolean existsById(CartProduct.CartProductId id);
}
