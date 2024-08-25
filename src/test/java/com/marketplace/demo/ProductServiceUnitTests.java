package com.marketplace.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.TestInstance;

import com.marketplace.demo.service.ProductService.ProductService;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceUnitTests {

	@Autowired
	private ProductService productService;

	@BeforeEach
	void setRules() {
	}

	@BeforeEach
	public void setUp() {
	}

}
