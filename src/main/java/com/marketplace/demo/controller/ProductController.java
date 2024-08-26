package com.marketplace.demo.controller;

import com.marketplace.demo.domain.Post;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.marketplace.demo.controller.dto.ProductDTO;
import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.controller.converter.ProductDTOConverter;
import com.marketplace.demo.service.PaymentService.PaymentService;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.UserService.UserService;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProductController {

    private ProductService productService;
    private ProductDTOConverter productConverter;
    private PostController postController;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        Iterable<Product> products = productService.readAll();
        List<ProductDTO> productDTOs = new ArrayList<>();

        for (Product product : products) {
            productDTOs.add(productConverter.toDTO(product));
        }

        return productDTOs;
    }

    @GetMapping(path = "/{id}")
    public ProductDTO getProductById(@PathVariable("id") Long id) {
        return productConverter.toDTO(productService.readById(id).get());
    }

    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        return productConverter.toDTO(productService.create(productConverter.toEntity(productDTO)));
    }

    @PutMapping(path = "/{id}")
    public ProductDTO updateProduct(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO) {
        Optional<Product> oldProduct = productService.readById(id);
        Product product = productConverter.toEntity(productDTO);

        if (oldProduct.isPresent()){
            oldProduct.get().setName(product.getName());
            oldProduct.get().setDescription(product.getDescription());
            oldProduct.get().setPrice(product.getPrice());
        }

        productService.update(id, oldProduct.get());
        return productConverter.toDTO(oldProduct.get());
    }

    @DeleteMapping(path = "/{id}")
    public void deleteProduct(@PathVariable("id") Long id) {
        Product product = productService.readById(id).get();
        Post post = product.getPost();

        postController.deletePost(post.getID());
    }

}
