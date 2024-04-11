package com.marketplace.demo.service.PaymentService;

import org.springframework.data.repository.CrudRepository;

import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.persistance.PaymentRepository;
import com.marketplace.demo.service.CrudServiceImpl;

public class PaymentService extends CrudServiceImpl<Payment, Long> implements PaymentServiceInterface {

    PaymentRepository paymentRepository;

    @Override
    protected CrudRepository<Payment, Long> getRepository() {
        return paymentRepository;
    }
}
