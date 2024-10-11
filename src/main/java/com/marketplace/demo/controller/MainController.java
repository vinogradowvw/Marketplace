package com.marketplace.demo.controller;

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.UserDTO;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.service.MainService.MainService;
import com.marketplace.demo.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class MainController {

    private final UserService userService;
    private final DTOConverter<UserDTO, User> userDTOConverter;
    private final AuthenticationManager authManager;
    private final MainService mainService;

    @Autowired
    MainController(UserService userService, DTOConverter<UserDTO, User> userDTOConverter,
                   MainService mainService, AuthenticationManager authManager) {
        this.userService = userService;
        this.userDTOConverter = userDTOConverter;
        this.authManager = authManager;
        this.mainService = mainService;
    }

    @PostMapping(path = "/register")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {

        UserDTO user = userDTOConverter.toDTO(userService.create(userDTOConverter.toEntity(userDTO)));

        mainService.sendUserData(user);

        return user;
    }

    @PostMapping(path = "/login")
    public String login(@RequestBody UserDTO userDTO) {
        return userService.verify(userDTOConverter.toEntity(userDTO), authManager);
    }

}
