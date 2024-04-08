package com.marketplace.demo.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.demo.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}