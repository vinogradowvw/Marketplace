package com.marketplace.demo.controller.dto;

import java.sql.Timestamp;
import java.util.Map;

public record CartDTO (Long id, Timestamp timestamp, Long User,
                       Map<Long, Long> products){}
