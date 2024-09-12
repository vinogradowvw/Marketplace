package com.marketplace.demo.service;

import com.marketplace.demo.domain.Cart;
import com.marketplace.demo.domain.CustomUserDetails;
import com.marketplace.demo.domain.Order;
import com.marketplace.demo.service.CartService.CartService;
import com.marketplace.demo.service.OrderService.OrderService;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SecurityService {

    private CartService cartService;
    private OrderService orderService;

    public boolean isAdminOrModerOrCalledUser(Authentication authentication, Long userId) {

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {

            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR"))) {
                return true;
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getId().equals(userId);
        }

        return false;
    }

    public boolean isCalledByUserWithCart(Authentication authentication, Long cartId) {

        Optional<Cart> cart = cartService.readById(cartId);

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails && cart.isPresent()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getId().equals(cart.get().getUser().getID());
        }

        return false;
    }

    public boolean orderSec(Authentication authentication, Long oderId) {

        Optional<Order> order = orderService.readById(oderId);

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails && order.isPresent()){

            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR")
                             || auth.getAuthority().equals("ROLE_SUPPORT_AGENT"))){
                return true;
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getId().equals(order.get().getUser().getID());
        }

        return false;
    }
}
