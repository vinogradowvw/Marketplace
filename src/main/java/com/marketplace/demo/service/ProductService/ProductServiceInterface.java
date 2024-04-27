package com.marketplace.demo.service.ProductService;

import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.domain.Product;

import com.marketplace.demo.domain.User;
import com.marketplace.demo.service.CrudService;

public interface ProductServiceInterface extends CrudService<Product, Long> {

    public void addPayment(User user, Product product, Payment payment) throws IllegalArgumentException;
    public void removePayment(User user, Product product, Payment payment) throws IllegalArgumentException;

}
