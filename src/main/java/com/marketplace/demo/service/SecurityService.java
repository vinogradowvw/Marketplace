package com.marketplace.demo.service;

import com.marketplace.demo.domain.*;
import com.marketplace.demo.service.CartService.CartService;
import com.marketplace.demo.service.OrderService.OrderService;
import com.marketplace.demo.service.PostService.PostService;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.ReviewService.ReviewService;
import com.marketplace.demo.service.RoleService.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SecurityService {

    private ProductService productService;
    private CartService cartService;
    private OrderService orderService;
    private PostService postService;
    private ReviewService reviewService;
    private RoleService roleService;

    private boolean moderatorAndSupAgCheck(Authentication authentication, Long userId) {

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails){

            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR")
                            || auth.getAuthority().equals("ROLE_SUPPORT_AGENT"))){
                return true;
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getId().equals(userId);
        }

        return false;
    }

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

        return order.filter(value -> moderatorAndSupAgCheck(authentication, value.getUser().getID())).isPresent();
    }

    public boolean postSec(Authentication authentication, Long postId) {

        Optional<Post> post = postService.readById(postId);

        return post.filter(val -> moderatorAndSupAgCheck(authentication, val.getUser().getID())).isPresent();
    }

    public boolean reviewSec(Authentication authentication, Long reviewId){

        Optional<Review> review = reviewService.readById(reviewId);

        return review.filter(val -> isAdminOrModerOrCalledUser(authentication, val.getAuthor().getID())).isPresent();
    }

    public boolean productSec(Authentication authentication, Long reviewId){

        Optional<Product> product = productService.readById(reviewId);

        return product.filter(val -> isAdminOrModerOrCalledUser(authentication, val.getPost().
                getUser().getID())).isPresent();
    }

    public boolean isRoleSec(Authentication authentication, Long roleId){

        Optional<Role> role = roleService.readById(roleId);

        if (role.isPresent()){

            if (role.get().getName().equals("ROLE_USER")){
                return true;
            }

            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {

                if (authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR"))) {
                    return true;
                }
            }
        }

        return false;
    }
}
