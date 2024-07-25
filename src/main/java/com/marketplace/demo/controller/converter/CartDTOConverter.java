package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.CartDTO;
import com.marketplace.demo.domain.Cart;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CartDTOConverter implements DTOConverter<CartDTO, Cart> {

    private UserService userService;
    private ProductService productService;

    @Override
    public CartDTO toDTO(Cart cart) {
        Map<Long, Long> products = new HashMap<>();
        cart.getProducts().keySet().forEach(p -> products.put(p.getID(), cart.getProducts().get(p)));

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

        Map<Product, Long> products = new HashMap<>();
        for (Long pId : cartDTO.products().keySet()) {
            Optional<Product> productOpt = productService.readById(pId);

            Product product = null;
            if (productOpt.isPresent()) {
                product = productOpt.get();
            }

            products.put(product, cart.getProducts().get(product));
        }
        cart.setProducts(products);

        return cart;
    }
}
