package com.marketplace.demo.controller.dto;

import java.util.List;

public record ProductDTO (Long id, Integer price, String name, 
                            Long post, List<Long> orders, List<Long> carts) {}
