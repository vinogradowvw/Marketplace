package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.CartDTO;
import com.marketplace.demo.domain.Cart;
import com.marketplace.demo.domain.CartProduct;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.persistance.CartProductRepository;
import com.marketplace.demo.persistance.CartRepository;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class CartDTOConverter implements DTOConverter<CartDTO, Cart> {

    private UserService userService;
    private ProductService productService;
    private CartProductRepository cartProductRepository;

    @Override
    public CartDTO toDTO(Cart cart) {
        Map<Long, Long> products = new HashMap<>();
        cart.getProducts().forEach(p -> products.put(p.getProduct().getID(), p.getQuantity()));

        Optional<User> userOpt = Optional.ofNullable(cart.getUser());
        Long userId = null;
        if (userOpt.isPresent()) {
            userId = userOpt.get().getID();
        }

       return new CartDTO(cart.getID(), cart.getTimestamp(), userId, products);
    }

    @Override
    public Cart toEntity(CartDTO cartDTO) {
        Cart cart = new Cart();
        cart.setId(cartDTO.id());
        cart.setTimestamp(cartDTO.timestamp());

        Optional<User> userOpt = userService.readById(cartDTO.User());
        User user = null;

        if (userOpt.isPresent()) {
            user = userOpt.get();
        }
        cart.setUser(user);

        List<CartProduct> products = new ArrayList<>();
        for (Long pId : cartDTO.products().keySet()) {
            Optional<Product> productOpt = productService.readById(pId);

            Product product = null;
            if (productOpt.isPresent()) {
                product = productOpt.get();
            }

            if (cartProductRepository.existsByCartAndProduct(cart, product)){
                CartProduct.CartProductId cartProductId = new CartProduct.CartProductId(cart.getID(), pId);
                CartProduct cartProduct = cartProductRepository.findById(cartProductId).get();

                products.add(cartProduct);
            }
        }
        cart.setProducts(products);

        return cart;
    }
}
