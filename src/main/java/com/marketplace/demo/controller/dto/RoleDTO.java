package com.marketplace.demo.controller.dto;

import java.util.List;

public record RoleDTO(Long id, String name, List<Long> users) { }
