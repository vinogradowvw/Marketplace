package com.marketplace.demo.controller.dto;

import java.sql.Timestamp;

public record PaymentDTO (Long id, Long amount, Timestamp timestamp, Long orderId) { }
