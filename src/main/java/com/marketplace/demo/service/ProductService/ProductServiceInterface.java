package com.marketplace.demo.service.ProductService;

import com.marketplace.demo.domain.Product;

import com.marketplace.demo.service.CrudService;
import jakarta.persistence.EntityNotFoundException;

public interface ProductServiceInterface extends CrudService<Product, Long> {
}
