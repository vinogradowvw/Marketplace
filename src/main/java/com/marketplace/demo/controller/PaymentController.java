package com.marketplace.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.demo.controller.converter.PaymentDTOConverter;
import com.marketplace.demo.controller.dto.PaymentDTO;
import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.service.PaymentService.PaymentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PaymentController {

    private PaymentService paymentService;
    private PaymentDTOConverter paymentConverter;

    @GetMapping
    public List<PaymentDTO> getAllPayements() {
        Iterable<Payment> payments = paymentService.readAll();
        List<PaymentDTO> paymentDTOs = new ArrayList<>();

        for (Payment payment : payments) {
            paymentDTOs.add(paymentConverter.toDTO(payment));
        }

        return paymentDTOs;
    }

    @GetMapping(path = "/{id}")
    public PaymentDTO getPaymentById(@PathVariable("id") Long id) {
        return paymentConverter.toDTO(paymentService.readById(id).get());
    }

    // To Do: хз че еще тут писать хз как оно работать будет и че надо будет

}
