package com.marketplace.demo.controller;

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.PaymentDTO;
import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.controller.dto.RoleDTO;
import com.marketplace.demo.controller.dto.UserDTO;
import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Role;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class userController {

    private final DTOConverter<UserDTO, User> userDTOConverter;
    private final DTOConverter<PostDTO, Post> postDTOConverter;
    private final DTOConverter<PaymentDTO, Payment> paymentDTOConverter;
    private final DTOConverter<RoleDTO, Role> roleDTOConverter;
    private final UserService userService;

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

    @GetMapping(path = "/{id}/liked")
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


}
