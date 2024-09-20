package com.marketplace.demo.controller;

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.UserDTO;
import com.marketplace.demo.domain.User;
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

    private UserService userService;
    private DTOConverter<UserDTO, User> userDTOConverter;
    private AuthenticationManager authManager;
    private String baseUrl;
    private RestClient userClient;

    @Autowired
    MainController(UserService userService, DTOConverter<UserDTO, User> userDTOConverter,
                   AuthenticationManager authManager, @Value("${api.url}") String baseUrl) {
        this.userService = userService;
        this.userDTOConverter = userDTOConverter;
        this.authManager = authManager;
        this.baseUrl = baseUrl + "/user/init";
        userClient = RestClient.builder().baseUrl(this.baseUrl).build();
    }

    @PostMapping(path = "/register")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {

        UserDTO user = userDTOConverter.toDTO(userService.create(userDTOConverter.toEntity(userDTO)));

        userClient.post()
                .uri("/" + user.id())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .toBodilessEntity();

        return user;
    }

    @PostMapping(path = "/login")
    public String login(@RequestBody UserDTO userDTO) {
        return userService.verify(userDTOConverter.toEntity(userDTO), authManager);
    }

}
