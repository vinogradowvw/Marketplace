package com.marketplace.demo.service.ProductService;

import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.persistance.PaymentRepository;
import com.marketplace.demo.persistance.UserRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.Product;
import com.marketplace.demo.persistance.ProductRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService extends CrudServiceImpl<Product, Long> implements ProductServiceInterface {

    private ProductRepository productRepository;
    private UserRepository userRepository;
    private PaymentRepository paymentRepository;

    @Override
    public void addPayment(User user, Product product, Payment payment) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (productRepository.existsById(product.getID())) {
                if (paymentRepository.existsById(payment.getID())) {
                    payment.setUser(user);
                    payment.getProducts().add(product);
                    user.getPayments().add(payment);
                    product.getPayments().add(payment);
                    userRepository.save(user);
                    paymentRepository.save(payment);
                    productRepository.save(product);
                }
                else{
                    throw new IllegalArgumentException("Payment with id " + payment.getID() + " does not exists");
                }

                return;
            }

            throw new IllegalArgumentException("Product with ID " + product.getID() + " does not exists");

        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    @Override
    public void removePayment(User user, Product product, Payment payment) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (productRepository.existsById(product.getID())) {
                if (paymentRepository.existsById(payment.getID())) {
                    payment.setUser(null);
                    payment.getProducts().remove(product);
                    user.getPayments().remove(payment);
                    product.getPayments().remove(payment);
                    userRepository.save(user);
                    paymentRepository.save(payment);
                    productRepository.save(product);
                }
                else{
                    throw new IllegalArgumentException("Payment with id " + payment.getID() + " does not exists");
                }

                return;
            }

            throw new IllegalArgumentException("Product with ID " + product.getID() + " does not exists");

        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    @Override
    protected CrudRepository<Product, Long> getRepository() {
        return productRepository;
    }
}
