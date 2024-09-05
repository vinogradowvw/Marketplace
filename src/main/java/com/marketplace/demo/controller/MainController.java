package com.marketplace.demo.controller;

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.UserDTO;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MainController {

    private UserService userService;
    private DTOConverter<UserDTO, User> userDTOConverter;
    private AuthenticationManager authManager;

    @PostMapping(path = "/register")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userDTOConverter.toDTO(userService.create(userDTOConverter.toEntity(userDTO)));
    }

    @PostMapping(path = "/login")
    public String login(@RequestBody UserDTO userDTO) {
        return userService.verify(userDTOConverter.toEntity(userDTO), authManager);
    }

}
