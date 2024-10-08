package com.marketplace.demo.controller.dto;

import java.util.List;

public record PostDTO(Long id, String name, String description,
                      Long views, Long product, Long user,
                      List<Long> likedUsers, List<Long> tags,
                      List<Long> images, List<Long> reviews) {}
