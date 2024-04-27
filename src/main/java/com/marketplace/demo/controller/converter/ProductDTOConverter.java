package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.ProductDTO;
import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.service.PaymentService.PaymentService;
import com.marketplace.demo.service.PostService.PostService;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductDTOConverter implements DTOConverter<ProductDTO, Product> {

    public final PaymentService paymentService;
    public final PostService postService;

    @Override
    public ProductDTO toDTO(Product product) {

        Long postId = null;
        
        Optional<Post> optPost = Optional.ofNullable(product.getPost());
        if (optPost.isPresent()) {
            postId = optPost.get().getID();
        }

        Optional<ArrayList<Payment>> optPayments = Optional.ofNullable(product.getPayments());
        ArrayList<Long> paymentsIds = new ArrayList<>();

        if (optPayments.isPresent()) {
            for (Payment payment : optPayments.get()) {
                paymentsIds.add(payment.getID());
            }
        }

        return new ProductDTO(product.getID(), product.getPrice(), product.getName(),
                                product.getDescription(), postId, paymentsIds);
    }

    @Override
    public Product toEntity(ProductDTO productDTO) {

        Product product = new Product();
        product.setId(productDTO.id());
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        
        Optional<String> optDescription = Optional.ofNullable(productDTO.description());
        if (optDescription.isPresent()) {
            product.setDescription(optDescription.get());
        }

        Optional<Long> postId = Optional.ofNullable(productDTO.post());
        if (postId.isPresent()) {
            Optional<Post> post = Optional.empty();
            post = postService.readById(postId.get());
            if (post.isPresent()) {
                product.setPost(post.get());
            }
        }

        Optional<List<Long>> optPaymentsIds = Optional.ofNullable(productDTO.payments());
        ArrayList<Payment> payments = new ArrayList<>();
        if (optPaymentsIds.isPresent()) {
            for (Long paymentId : optPaymentsIds.get()) {
                Optional<Payment> payment =  Optional.empty();
                payment = paymentService.readById(paymentId);
                if (payment.isPresent()) {
                    payments.add(payment.get());
                }
            }
        }

        product.setPayments(payments);

        return null;
    }
}
