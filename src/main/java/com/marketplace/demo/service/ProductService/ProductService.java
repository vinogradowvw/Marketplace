package com.marketplace.demo.service.ProductService;

import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.Product;
import com.marketplace.demo.persistance.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService extends CrudServiceImpl<Product, Long> implements ProductServiceInterface {

    private ProductRepository productRepository;

    @Override
    protected CrudRepository<Product, Long> getRepository() {
        return productRepository;
    }
}
