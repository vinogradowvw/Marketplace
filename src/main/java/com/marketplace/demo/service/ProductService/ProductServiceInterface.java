package com.marketplace.demo.service.ProductService;

import com.marketplace.demo.domain.Product;

import jakarta.persistence.EntityNotFoundException;

public interface ProductServiceInterface {

    public Product getProductById(Long id) throws EntityNotFoundException;

    public Product createProduct(Product product) throws IllegalArgumentException;

    public Product updateProduct(Product product);

    public void deleteProduct(Product product);
    
}
