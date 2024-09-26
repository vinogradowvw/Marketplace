package com.marketplace.demo.controller;

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.*;
import com.marketplace.demo.domain.*;
import com.marketplace.demo.service.RoleService.RoleService;
import com.marketplace.demo.service.SubscriptionService.SubscriptionService;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final DTOConverter<UserDTO, User> userDTOConverter;
    private final DTOConverter<SubscriptionDTO, Subscription> subscriptionDTOConverter;
    private final DTOConverter<PostDTO, Post> postDTOConverter;
    private final DTOConverter<OrderDTO, Order> orderDTOConverter;
    private final DTOConverter<RoleDTO, Role> roleDTOConverter;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final RoleService roleService;
    @Value("${api.url}")
    private String baseUrl;

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

    @GetMapping(path = "/username" )
    public UserDTO getUserByUsername(@RequestParam("username")String username) {
        return userDTOConverter.toDTO(userService.findByUsername(username));
    }

    @GetMapping(path = "/{id}/likes")
    public List<PostDTO> getLikedPosts(@PathVariable("id")Long id){
        return userService.readById(id).get().getLikes().stream().map(postDTOConverter::toDTO).toList();
    }

    @GetMapping(path = "/{id}/orders")
    public List<OrderDTO> getPayments(@PathVariable("id")Long id){
        return userService.readById(id).get().getOrders().stream().map(orderDTOConverter::toDTO).toList();
    }

    @GetMapping(path = "/{id}/posts")
    public List<PostDTO> getPosts(@PathVariable("id")Long id){
        return userService.readById(id).get().getPosts().stream().map(postDTOConverter::toDTO).toList();
    }

    @GetMapping(path = "/{id}/subscribers")
    public List<SubscriptionDTO> getSubscribers(@PathVariable("id")Long id){
        return userService.getSubscribers(userService.readById(id).get()).stream().map(subscriptionDTOConverter::toDTO).toList();
    }

    @GetMapping(path = "/{id}/subscriptions")
    public List<SubscriptionDTO> getSubscriptions(@PathVariable("id")Long id){
        return userService.getSubscription(userService.readById(id).get()).stream().map(subscriptionDTOConverter::toDTO).toList();
    }

    @GetMapping(path = "/{id}/role")
    public List<RoleDTO> getRoles(@PathVariable("id")Long id){
        return userService.readById(id).get().getRoles().stream().map(roleDTOConverter::toDTO).toList();
    }

    @PostMapping(path = "/{id}/subscription")
    public UserDTO subscribe(@PathVariable("id") Long userId, @RequestParam Long subscriberId){
        User user = userService.readById(userId).get();
        User subscriber = userService.readById(subscriberId).get();

        userService.addSubscriptionToUsers(user, subscriber);

        return userDTOConverter.toDTO(user);
    }

    @DeleteMapping(path = "/{userId}/subscription/{subscriberId}")
    public UserDTO unsubscribe(@PathVariable("userId") Long userId, @PathVariable("subscriberId") Long subscriberId){
        Subscription subscription = subscriptionService.findByUserIdAndSubscriberId(userId, subscriberId);

        User user = userService.readById(userId).get();
        User subscriber = userService.readById(subscriberId).get();

        userService.removeSubscriptionToUsers(user, subscriber, subscription);

        return userDTOConverter.toDTO(user);
    }

    @PostMapping(path = "/{id}/role/{roleId}")
    public UserDTO addRole(@PathVariable("id") Long userId, @PathVariable("roleId") Long roleId){
        User user = userService.readById(userId).get();
        Role role = roleService.readById(roleId).get();

        userService.addRoleToUser(user, role);

        return userDTOConverter.toDTO(user);
    }

    @DeleteMapping(path = "/{userId}/role/{roleId}")
    public UserDTO removeRole(@PathVariable("userId") Long userId, @PathVariable("roleId") Long roleId){
        User user = userService.readById(userId).get();
        Role role = roleService.readById(roleId).get();

        userService.removeRoleFromUser(user, role);

        return userDTOConverter.toDTO(user);
    }

    @PutMapping(path = "/{id}")
    public UserDTO updateUser(@PathVariable ("id") Long id, @RequestBody UserDTO userDTO){
        User user = userDTOConverter.toEntity(userDTO);
        Optional<User> oldUser = userService.readById(id);

        oldUser.ifPresent(value -> {
            value.setEmail(user.getEmail());
            value.setPassword(user.getPassword());
            value.setUsername(user.getUsername());
        });

        userService.update(id, oldUser.get());
        return userDTOConverter.toDTO(oldUser.get());
    }

     @DeleteMapping(path = "/{id}")
     public void deleteUser(@PathVariable("id") Long id){

        userService.deleteById(id);

         RestClient.builder().baseUrl(baseUrl).build()
                 .delete()
                 .uri("/user/" + id)
                 .retrieve()
                 .toBodilessEntity();
     }
}
