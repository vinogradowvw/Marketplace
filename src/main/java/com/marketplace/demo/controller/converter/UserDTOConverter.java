package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.UserDTO;
import com.marketplace.demo.domain.*;
import com.marketplace.demo.persistance.*;
import com.marketplace.demo.service.CartService.CartService;
import com.marketplace.demo.service.OrderService.OrderService;
import com.marketplace.demo.service.PostService.PostService;
import com.marketplace.demo.service.ReviewService.ReviewService;
import com.marketplace.demo.service.RoleService.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserDTOConverter implements DTOConverter<UserDTO, User> {

    private final PostService postService;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final CartService cartService;
    private final RoleService roleService;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public UserDTO toDTO(User user) {
        List<Long> postIds = new ArrayList<>();
        List<Long> orderIds = new ArrayList<>();
        List<Long> likeIds = new ArrayList<>();
        List<Long> subscriberIds = new ArrayList<>();
        List<Long> subscriptionIds = new ArrayList<>();
        List<Long> roleIds = new ArrayList<>();
        List<Long> reviewIds = new ArrayList<>();

        Long cartId = Optional.ofNullable(user.getCart()).map(Cart::getID).orElse(null);

        Optional<List<Post>> likes = Optional.ofNullable(user.getLikes());
        Optional<List<Post>> posts = Optional.ofNullable(user.getPosts());
        Optional<List<Order>> orders = Optional.ofNullable(user.getOrders());
        Optional<List<Subscription>> subscribers = Optional.ofNullable(user.getSubscribers());
        Optional<List<Subscription>> subscriptions = Optional.ofNullable(user.getSubscriptions());
        Optional<List<Role>> roles = Optional.ofNullable(user.getRoles());
        Optional<List<Review>> reviews = Optional.ofNullable(user.getReviews());

        if (likes.isPresent()){
            for (Post post : likes.get()) {
                likeIds.add(post.getID());
            }
        }

        if (posts.isPresent()){
            for (Post post : posts.get()) {
                postIds.add(post.getID());
            }
        }

        if (orders.isPresent()){
            for (Order order : orders.get()) {
                orderIds.add(order.getID());
            }
        }

        if (subscribers.isPresent()){
            for (Subscription subs : subscribers.get()) {
                subscriberIds.add(subs.getSubscriber().getID());
            }
        }

        if (subscriptions.isPresent()){
            for (Subscription s : subscriptions.get()) {
                subscriptionIds.add(s.getUser().getID());
            }
        }

        if (roles.isPresent()){
            for (Role role : roles.get()) {
                roleIds.add(role.getID());
            }
        }

        if (reviews.isPresent()){
            for (Review review : reviews.get()) {
                reviewIds.add(review.getID());
            }
        }


        return new UserDTO(user.getID(), user.getUsername(), user.getEmail(),
                user.getPassword(), likeIds, orderIds, postIds,
                subscriberIds, subscriptionIds, cartId, roleIds, reviewIds);
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        User user = new User();

        user.setId(userDTO.id());
        user.setUsername(userDTO.username());
        user.setPassword(userDTO.password());
        user.setEmail(userDTO.email());

        List<Post> posts = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        List<Post> likes = new ArrayList<>();
        List<Subscription> subscribers = new ArrayList<>();
        List<Subscription> subscriptions = new ArrayList<>();
        List<Role> roles = new ArrayList<>();
        List<Review> reviews = new ArrayList<>();

        Cart cart = cartService.readById(userDTO.cart()).orElse(null);

        for (Long likeId : userDTO.likes()) {
            Post post = postService.readById(likeId).orElse(null);

            likes.add(post);
        }

        for (Long postId : userDTO.posts()) {
            Post post = postService.readById(postId).orElse(null);

            posts.add(post);
        }

        for (Long orderId : userDTO.orders()) {
            Order order = orderService.readById(orderId).orElse(null);

            orders.add(order);
        }

        for (Long subscriberId : userDTO.subscribers()) {
            Subscription sub = subscriptionRepository.findByUserIdAndSubscriberId(userDTO.id(), subscriberId);

            subscribers.add(sub);
        }

        for (Long subscriptionId : userDTO.subscriptions()) {
            Subscription sub = subscriptionRepository.findByUserIdAndSubscriberId(subscriptionId, userDTO.id());

            subscriptions.add(sub);
        }

        for (Long roleId : userDTO.roles()) {
            Role role = roleService.readById(roleId).orElse(null);

            roles.add(role);
        }

        for (Long reviewId : userDTO.reviews()) {
            Review review = reviewService.readById(reviewId).orElse(null);

            reviews.add(review);
        }

        user.setLikes(likes);
        user.setPosts(posts);
        user.setOrders(orders);
        user.setSubscribers(subscribers);
        user.setSubscriptions(subscriptions);
        user.setCart(cart);
        user.setRoles(roles);
        user.setReviews(reviews);

        return user;
    }
}
