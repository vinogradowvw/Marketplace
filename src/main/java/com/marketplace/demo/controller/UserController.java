package com.marketplace.demo.controller;

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.PaymentDTO;
import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.controller.dto.RoleDTO;
import com.marketplace.demo.controller.dto.UserDTO;
import com.marketplace.demo.domain.*;
import com.marketplace.demo.service.PostService.PostService;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.RoleService.RoleService;
import com.marketplace.demo.service.SubscriptionService.SubscriptionService;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {

    private final DTOConverter<UserDTO, User> userDTOConverter;
    private final DTOConverter<PostDTO, Post> postDTOConverter;
    private final DTOConverter<PaymentDTO, Payment> paymentDTOConverter;
    private final DTOConverter<RoleDTO, Role> roleDTOConverter;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final RoleService roleService;
    private final PostService postService;
    private final ProductService productService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        Iterable<User> users = userService.readAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users) {
            userDTOs.add(userDTOConverter.toDTO(user));
        }

        return userDTOs;
    }

    @GetMapping(path = "/{id}")
    public UserDTO getUserById(@PathVariable("id")Long id) {
        return userDTOConverter.toDTO(userService.readById(id).get());
    }

    @GetMapping(path = "/{id}/likes")
    public List<PostDTO> getLikedPosts(@PathVariable("id")Long id){
        return userService.readById(id).get().getLikes().stream().map(postDTOConverter::toDTO).toList();
    }

    @GetMapping(path = "/{id}/payments")
    public List<PaymentDTO> getPayments(@PathVariable("id")Long id){
        return userService.readById(id).get().getPayments().stream().map(paymentDTOConverter::toDTO).toList();
    }

    @GetMapping(path = "/{id}/posts")
    public List<PostDTO> getPosts(@PathVariable("id")Long id){
        return userService.readById(id).get().getPosts().stream().map(postDTOConverter::toDTO).toList();
    }

    @GetMapping(path = "/{id}/subscribers")
    public List<UserDTO> getSubscribers(@PathVariable("id")Long id){
        return userService.readById(id).get().getSubscribers().stream().map(userDTOConverter::toDTO).toList();
    }

    @GetMapping(path = "/{id}/subscriptions")
    public List<UserDTO> getSubscriptions(@PathVariable("id")Long id){
        return userService.readById(id).get().getSubscriptions().stream().map(userDTOConverter::toDTO).toList();
    }

    @GetMapping(path = "/{id}/role")
    public RoleDTO getRole(@PathVariable("id")Long id){
        return roleDTOConverter.toDTO(userService.readById(id).get().getRole());
    }

    @PostMapping(path = "/user")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userDTOConverter.toDTO(userService.create(userDTOConverter.toEntity(userDTO)));
    }

    @PostMapping(path = "/{id}/subscription")
    public UserDTO subscribe(@PathVariable("id") Long userId, Long subscriberId){
        Subscription subscription = new Subscription();
        subscription.setTimestamp(new Timestamp(System.currentTimeMillis()));
        subscriptionService.create(subscription);

        User user = userService.readById(userId).get();
        User subscriber = userService.readById(subscriberId).get();

        userService.addSubscriptionToUsers(user, subscriber, subscription);

        return userDTOConverter.toDTO(user);
    }

    @DeleteMapping(path = "/{userId}/subscription/{subscriberId}")
    public UserDTO unsubscribe(@PathVariable("userId") Long userId, @PathVariable("subscriberId") Long subscriberId){
        Subscription subscription = subscriptionService.findByUserIdAndSubscriberId(userId, subscriberId);

        User user = userService.readById(userId).get();
        User subscriber = userService.readById(subscriberId).get();

        userService.removeSubscriptionToUsers(user, subscriber, subscription);
        subscriptionService.deleteById(subscription.getID());

        return userDTOConverter.toDTO(user);
    }

    @PostMapping(path = "/{id}/role")
    public UserDTO addRole(@PathVariable("id") Long userId, Long roleId){
        User user = userService.readById(userId).get();
        Role role = roleService.readById(roleId).get();

        userService.addRoleToUser(user, role);

        return userDTOConverter.toDTO(user);
    }

    @DeleteMapping(path = "/{id}/role")
    public UserDTO removeRole(@PathVariable("id") Long userId){
        User user = userService.readById(userId).get();
        Role role = user.getRole();

        userService.removeRoleFromUser(user, role);

        return userDTOConverter.toDTO(user);
    }

    @PutMapping(path = "/{id}")
    public UserDTO updateUser(@PathVariable ("id") Long id, @RequestBody UserDTO userDTO){
        User user = userDTOConverter.toEntity(userDTO);
        Optional<User> oldUser = userService.readById(id);

        if (oldUser.isPresent()){
            user.setPosts(oldUser.get().getPosts());
            user.setSubscriptions(oldUser.get().getSubscriptions());
            user.setSubscribers(oldUser.get().getSubscribers());
            user.setLikes(oldUser.get().getLikes());
            user.setRole(oldUser.get().getRole());
            user.setPayments(oldUser.get().getPayments());
        }

        user.setId(id);

        userService.update(id, user);
        return userDTOConverter.toDTO(user);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable("id") Long id){
        User user = userService.readById(id).get();
        for (User subscriber : user.getSubscribers()){
            this.unsubscribe(user.getID(), subscriber.getID());
        }

        for (User sub : user.getSubscriptions()){
            this.unsubscribe(sub.getID(), user.getID());
        }

        this.removeRole(user.getID());

        for (Post like : user.getLikes()){
            postService.likePost(like, user);
        }

        for (Post post : user.getPosts()){
            for (Product product : post.getProductsInPost()){
                productService.deleteById(product.getID());
            }
            postService.deleteById(post.getID());
        }

        for (Payment payment : user.getPayments()){
            payment.setUser(null);
        }

        userService.deleteById(user.getID());
    }
}
