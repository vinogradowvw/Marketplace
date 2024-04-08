package com.marketplace.demo.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.demo.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}