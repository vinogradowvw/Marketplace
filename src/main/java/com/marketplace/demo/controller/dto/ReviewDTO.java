package com.marketplace.demo.controller.dto;

public record ReviewDTO(Long id, String title, String description,
                        Integer rating, Long author, Long post) {}
