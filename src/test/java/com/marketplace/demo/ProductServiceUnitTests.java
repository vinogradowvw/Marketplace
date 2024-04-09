package com.marketplace.demo;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marketplace.demo.domain.Image;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.persistance.ImageRepository;
import com.marketplace.demo.persistance.ProductRepository;
import com.marketplace.demo.service.ImageService.ImageService;
import com.marketplace.demo.service.ProductService.ProductService;

@SpringBootTest
class ProductServiceUnitTests {

	@Autowired
	ProductService productService;
	@Autowired
	ImageService imageService;
	@MockBean
	ImageRepository imageRepository;
	@MockBean
	ProductRepository productRepository;

	Product product;
	Image image1;
	Image image2;
	List<Image> images;

	@BeforeEach
	void setUp() {
		product = new Product();
		product.setDescription("testDescr");
		product.setName("testProduct");
		product.setPrice(100);
		product.setPostsWithProduct(new ArrayList<>());
		product.setProductImages(new ArrayList<>());

		image1 = new Image();
		image1.setPath("/test/path/1");
		image1.setProductsWithImg(new ArrayList<>());

		image2 = new Image();
		image2.setPath("/test/path/2");
		image2.setProductsWithImg(new ArrayList<>());

		images = new ArrayList<Image>();
		images.add(image1);
		images.add(image2);

		Mockito.when(
                productRepository.findById(product.getID())
        ).thenReturn(Optional.of(product));

		Mockito.when(
                productRepository.existsById(product.getID())
        ).thenReturn(true);

		Mockito.when(
                imageRepository.existsById(image1.getID())
        ).thenReturn(true);

		Mockito.when(
                imageRepository.existsById(image2.getID())
        ).thenReturn(true);

        Mockito.when(
                imageRepository.findById(image1.getID())
        ).thenReturn(Optional.of(image1));
		Mockito.when(
                imageRepository.findById(image2.getID())
        ).thenReturn(Optional.of(image2));
	}

	@Test
	void addProductWithImage() {
		productService.addProductImages(product, images);

		Assertions.assertTrue(product.getProductImages().contains(image1));
		Assertions.assertTrue(product.getProductImages().contains(image2));
        Assertions.assertTrue(image1.getProductsWithImg().contains(product));

		Assertions.assertEquals(2, product.getProductImages().size());
        Assertions.assertEquals(1, image1.getProductsWithImg().size());
		Assertions.assertEquals(1, image2.getProductsWithImg().size());

		Mockito.verify(productRepository, Mockito.atLeastOnce()).save(product);
        Mockito.verify(imageRepository, Mockito.atLeastOnce()).saveAll(images);

	}
}