package com.marketplace.demo.controller.dto;

import java.util.ArrayList;

public record ProductDTO (Long id, Integer price, String name, 
                            String description, Long post,
                            ArrayList<Long> payments) {}
