package com.marketplace.demo.controller.dto;

import java.util.List;

public record PostDTO(Long id, String name, Long views,
                      String description, Long product, Long user,
                      List<Long> likedUsers, List<Long> tags,
                      List<Long> images, List<Long> reviews) {}
