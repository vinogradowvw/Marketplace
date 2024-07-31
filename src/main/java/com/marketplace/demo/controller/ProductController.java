package com.marketplace.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private PaymentService paymentService;
    private UserService userService;

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

        if (oldProduct.isPresent()) {
            // product.setPayments(oldProduct.get().getPayments());
            product.setPost(oldProduct.get().getPost());
        }
        
        product.setId(id);

        productService.update(id, product);
        return productConverter.toDTO(product);
        
    }

    // @PostMapping(path = "/{id}/payment")
    // public ProductDTO addPayment(@PathVariable("id") Long productId, @RequestParam Long userId, @RequestParam Long paymentId) {
    //     Product product = productService.readById(productId).get();
    //     Payment payment = paymentService.readById(paymentId).get();
    //     User user = userService.readById(userId).get();
    //
    //     productService.addPayment(user, product, payment);
    //     product = productService.readById(productId).get();
    //
    //     return productConverter.toDTO(product);
    // }

    // @PostMapping(path = "/{id}/payment")
    // public ProductDTO removePayment(@PathVariable("id") Long productId, @RequestParam Long userId, @RequestParam Long paymentId) {
    //     Product product = productService.readById(productId).get();
    //     Payment payment = paymentService.readById(paymentId).get();
    //     User user = userService.readById(userId).get();
    //
    //     productService.removePayment(user, product, payment);
    //     product = productService.readById(productId).get();
    //
    //     return productConverter.toDTO(product);
    // }

}
