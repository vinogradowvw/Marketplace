package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.ProductDTO;
import com.marketplace.demo.domain.*;
import com.marketplace.demo.service.CartService.CartService;
import com.marketplace.demo.service.OrderService.OrderService;
import com.marketplace.demo.service.PaymentService.PaymentService;
import com.marketplace.demo.service.PostService.PostService;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductDTOConverter implements DTOConverter<ProductDTO, Product> {

    public final OrderService orderService;
    public final CartService cartService;
    public final PostService postService;

    @Override
    public ProductDTO toDTO(Product product) {

        Long postId = Optional.ofNullable(product.getPost()).map(Post::getID).orElse(null);

        ArrayList<Long> ordersId = new ArrayList<>();
        for (Order order : product.getOrders()) {
            ordersId.add(order.getID());
        }

        ArrayList<Long> cartIds = new ArrayList<>();
        for (Cart cart : product.getCarts()) {
            cartIds.add(cart.getID());
        }

        return new ProductDTO(product.getID(), product.getPrice(), product.getName(),
                              product.getDescription(), postId, ordersId, cartIds);
    }

    @Override
    public Product toEntity(ProductDTO productDTO) {

        Product product = new Product();
        product.setId(productDTO.id());
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setDescription(productDTO.description());

        Post post = postService.readById(productDTO.post()).orElse(null);
        product.setPost(post);

        ArrayList<Order> orders = new ArrayList<>();
        for (Long orderId : productDTO.orders()) {
           Order order = orderService.readById(orderId).orElse(null);

           orders.add(order);
        }
        product.setOrders(orders);

        ArrayList<Cart> carts = new ArrayList<>();
        for (Long cartId : productDTO.carts()) {
            Cart cart = cartService.readById(cartId).orElse(null);

            carts.add(cart);
        }
        product.setCarts(carts);

        return product;
    }
}
