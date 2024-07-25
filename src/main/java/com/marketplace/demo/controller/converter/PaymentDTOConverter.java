package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.PaymentDTO;
import com.marketplace.demo.domain.Order;
import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.service.OrderService.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PaymentDTOConverter implements DTOConverter<PaymentDTO, Payment> {

    private final OrderService orderService;

    @Override
    public PaymentDTO toDTO(Payment payment) {
        Long orderId = Optional.ofNullable(payment.getOrder()).map(Order::getID).orElse(null);

        return new PaymentDTO(payment.getID(), payment.getAmount(), payment.getTimestamp(), orderId);
    }

    @Override
    public Payment toEntity(PaymentDTO paymentDTO) {
        Payment payment = new Payment();

        payment.setId(paymentDTO.id());
        payment.setAmount(paymentDTO.amount());
        payment.setTimestamp(paymentDTO.timestamp());

        Order order = orderService.readById(paymentDTO.orderId()).orElse(null);
        payment.setOrder(order);

        return payment;
    }
}
