package com.marketplace.demo.service.UserService;

import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.domain.Subscription;
import com.marketplace.demo.persistance.PaymentRepository;
import com.marketplace.demo.persistance.ProductRepository;
import com.marketplace.demo.persistance.SubscriptionRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.User;
import com.marketplace.demo.persistance.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService extends CrudServiceImpl<User, Long> implements UserServiceInterface {

    private UserRepository userRepository;
    private SubscriptionRepository subscriptionRepository;
    private ProductRepository productRepository;
    private PaymentRepository paymentRepository;

    @Override
    public User create(User user){
        Optional<Long> id = Optional.ofNullable(user.getID());
        if (id.isPresent()){
            if (userRepository.existsById(id.get())){
                throw new IllegalArgumentException("User with id " + id.get() + " already exists");
            }
        }
        else{
            if (userRepository.existsByUsername(user.getUsername())){
                throw new IllegalArgumentException("User with username " + user.getUsername() + " already exists");
            }
        }

        return getRepository().save(user);
    }

    @Override
    public void addSubscriptionToUsers(User user, User subscriber, Subscription subscription) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (subscriptionRepository.existsById(subscription.getID())) {
                if (userRepository.existsById(subscriber.getID())) {
                    subscription.setUser(user);
                    subscription.setSubscriber(subscriber);
                    user.getSubscribers().add(subscriber);
                    subscriber.getSubscriptions().add(user);
                    userRepository.save(user);
                    userRepository.save(subscriber);
                    subscriptionRepository.save(subscription);
                }
                else{
                    throw new IllegalArgumentException("User with id " + subscriber.getID() + " does not exists");
                }
            }

            throw new IllegalArgumentException("Subscription with ID " + subscription.getID() + " does not exists");

        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    @Override
    public void removeSubscriptionToUsers(User user, User subscriber, Subscription subscription) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (subscriptionRepository.existsById(subscription.getID())) {
                if (userRepository.existsById(subscriber.getID())) {
                    subscription.setUser(null);
                    subscription.setSubscriber(null);
                    user.getSubscribers().remove(subscriber);
                    subscriber.getSubscriptions().remove(user);
                    userRepository.save(user);
                    userRepository.save(subscriber);
                    subscriptionRepository.save(subscription);
                }
                else{
                    throw new IllegalArgumentException("User with id " + subscriber.getID() + " does not exists");
                }
            }

            throw new IllegalArgumentException("Subscription with ID " + subscription.getID() + " does not exists");

        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    @Override
    public void addPaymentToUser(User user, Product product, Payment payment) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (productRepository.existsById(product.getID())) {
                if (paymentRepository.existsById(payment.getID())) {
                    payment.setUser(user);
                    user.getPayments().add(payment);
                    product.getPayments().add(payment);
                    userRepository.save(user);
                    paymentRepository.save(payment);
                    productRepository.save(product);
                }
                else{
                    throw new IllegalArgumentException("Payment with id " + payment.getID() + " does not exists");
                }
            }

            throw new IllegalArgumentException("Product with ID " + product.getID() + " does not exists");

        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    @Override
    public void removePaymentFromUser(User user, Product product, Payment payment) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (productRepository.existsById(product.getID())) {
                if (paymentRepository.existsById(payment.getID())) {
                    payment.setUser(null);
                    user.getPayments().remove(payment);
                    product.getPayments().remove(payment);
                    userRepository.save(user);
                    paymentRepository.save(payment);
                    productRepository.save(product);
                }
                else{
                    throw new IllegalArgumentException("Payment with id " + payment.getID() + " does not exists");
                }
            }

            throw new IllegalArgumentException("Product with ID " + product.getID() + " does not exists");

        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }


    @Override
    protected CrudRepository<User, Long> getRepository() {
        return userRepository;
    }
}

