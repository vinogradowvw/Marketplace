package com.marketplace.demo.service.PaymentService;

import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.persistance.PaymentRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class PaymentService extends CrudServiceImpl<Payment, Long> implements PaymentServiceInterface {

    private PaymentRepository paymentRepository;

    @Override
    protected CrudRepository<Payment, Long> getRepository() {
        return paymentRepository;
    }
}
