package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.ProductDTO;
import com.marketplace.demo.domain.*;
import com.marketplace.demo.persistance.CartProductRepository;
import com.marketplace.demo.persistance.OrderProductRepository;
import com.marketplace.demo.service.PostService.PostService;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductDTOConverter implements DTOConverter<ProductDTO, Product> {

    private final PostService postService;
    private OrderProductRepository orderProductRepository;
    private CartProductRepository cartProductRepository;

    @Override
    public ProductDTO toDTO(Product product) {

        Long postId = Optional.ofNullable(product.getPost()).map(Post::getID).orElse(null);

        ArrayList<Long> ordersId = new ArrayList<>();
        for (OrderProduct order : product.getOrders()) {
            ordersId.add(order.getOrder().getID());
        }

        ArrayList<Long> cartIds = new ArrayList<>();
        for (CartProduct cart : product.getCarts()) {
            cartIds.add(cart.getCart().getID());
        }

        return new ProductDTO(product.getID(), product.getPrice(), product.getName(),
                postId, ordersId, cartIds);
    }

    @Override
    public Product toEntity(ProductDTO productDTO) {

        Product product = new Product();
        product.setId(productDTO.id());
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());

        Post post = null;
        Optional<Long> postId = Optional.ofNullable(productDTO.post());
        if (postId.isPresent()) {
            post = postService.readById(postId.get()).orElse(null);
        }
        product.setPost(post);

        ArrayList<OrderProduct> orders = new ArrayList<>();
        for (Long orderId : productDTO.orders()) {
            OrderProduct.OrderProductId orderProductId = new OrderProduct.OrderProductId(orderId, product.getID());

           OrderProduct order = orderProductRepository.findById(orderProductId).orElse(null);

           orders.add(order);
        }
        product.setOrders(orders);

        ArrayList<CartProduct> carts = new ArrayList<>();
        for (Long cartId : productDTO.carts()) {
            CartProduct.CartProductId cartProductId = new CartProduct.CartProductId(cartId, product.getID());

            CartProduct cart = cartProductRepository.findById(cartProductId).orElse(null);

            carts.add(cart);
        }
        product.setCarts(carts);

        return product;
    }
}
