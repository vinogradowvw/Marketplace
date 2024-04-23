package com.marketplace.demo.controller.dto;

import java.sql.Timestamp;

public record SubscriptionDTO(Long id, Timestamp timestamp, Long subscriber, Long user) { }
