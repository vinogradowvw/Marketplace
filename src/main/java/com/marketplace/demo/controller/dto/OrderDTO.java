package com.marketplace.demo.controller.dto;

import java.sql.Timestamp;
import java.util.Map;

public record OrderDTO (Long id, Timestamp timestamp, String state,
                        Map<Long, Long> products, Long userId, Long paymentId){}
