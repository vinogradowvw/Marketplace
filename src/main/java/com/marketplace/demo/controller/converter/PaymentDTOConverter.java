package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.PaymentDTO;
import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.persistance.ProductRepository;
import com.marketplace.demo.persistance.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PaymentDTOConverter implements DTOConverter<PaymentDTO, Payment> {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public PaymentDTO toDTO(Payment payment) {
        Optional<User> optUser = Optional.ofNullable(payment.getUser());
        Optional<Product> optProduct = Optional.ofNullable(payment.getProduct());

        if (optUser.isPresent() && optProduct.isPresent()) {
            return new PaymentDTO(payment.getID(), payment.getAmount(), payment.getUser().getID(), payment.getProduct().getID());
        }
        else if (optUser.isPresent()) {
            return new PaymentDTO(payment.getID(), payment.getAmount(), payment.getUser().getID(), null);
        }
        else if (optProduct.isPresent()) {
            return new PaymentDTO(payment.getID(), payment.getAmount(), null, payment.getProduct().getID());
        }
        else{
            return new PaymentDTO(payment.getID(), payment.getAmount(), null, null);
        }

    }

    @Override
    public Payment toEntity(PaymentDTO paymentDTO) {
        Payment payment = new Payment();

        payment.setId(paymentDTO.id());
        payment.setAmount(paymentDTO.amount());

        Optional<Long> optUserId = Optional.ofNullable(paymentDTO.userId());
        Optional<Long> optProductId = Optional.ofNullable(paymentDTO.productId());

        Optional<User> optUser = Optional.empty();
        Optional<Product> optProduct = Optional.empty();

        if (optUserId.isPresent()) {
            optUser = userRepository.findById(optUserId.get());
        }

        if (optProductId.isPresent()) {
            optProduct = productRepository.findById(optProductId.get());
        }

        if (optUser.isPresent() && optProduct.isPresent()) {
            payment.setUser(optUser.get());
            payment.setProduct(optProduct.get());
        }
        else if (optUser.isPresent()) {
            payment.setUser(optUser.get());
            payment.setProduct(null);
        }
        else if (optProduct.isPresent()) {
            payment.setUser(null);
            payment.setProduct(optProduct.get());
        }
        else{
            payment.setUser(null);
            payment.setProduct(null);
        }

        return payment;
    }
}
