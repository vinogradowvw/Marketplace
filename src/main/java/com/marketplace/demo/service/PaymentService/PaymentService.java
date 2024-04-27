package com.marketplace.demo.service.PaymentService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;

import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.persistance.PaymentRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class PaymentService extends CrudServiceImpl<Payment, Long> implements PaymentServiceInterface {

    PaymentRepository paymentRepository;

    @Override
    protected CrudRepository<Payment, Long> getRepository() {
        return paymentRepository;
    }
}
