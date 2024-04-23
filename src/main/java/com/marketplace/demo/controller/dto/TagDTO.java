package com.marketplace.demo.controller.dto;

import java.util.List;

public record TagDTO(Long id, String name, List<Long> posts) { }
