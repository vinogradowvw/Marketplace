package com.marketplace.demo.controller.dto;

import java.util.List;

public record UserDTO(Long id, String username, String email, String password,
                      List<Long> likes, List<Long> orders, List<Long> posts,
                      List<Long> subscribers, List<Long> subscriptions,
                      Long cart, List<Long> roles, List<Long> reviews) { }
