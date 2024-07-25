package com.marketplace.demo.service.UserService;

import com.marketplace.demo.domain.*;
import com.marketplace.demo.persistance.*;
import com.marketplace.demo.service.CartService.CartService;
import com.marketplace.demo.service.CrudServiceImpl;
import com.marketplace.demo.service.SubscriptionService.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService extends CrudServiceImpl<User, Long> implements UserServiceInterface {

    private final SubscriptionService subscriptionService;
    private UserRepository userRepository;
    private SubscriptionRepository subscriptionRepository;
    private RoleRepository roleRepository;
    private CartService cartService;

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

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);
        cart.setTimestamp(new Timestamp(System.currentTimeMillis()));
        cartService.create(cart);

        return getRepository().save(user);
    }

    @Override
    public void deleteById(Long id){
        if (!userRepository.existsById(id)){
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }

        User user = userRepository.findById(id).get();
        Cart cart = user.getCart();

        for (var p : cart.getProducts().keySet()){
            p.getCarts().remove(cart);
        }

        cartService.deleteById(cart.getID());

        userRepository.deleteById(id);
    }

    @Override
    public void addSubscriptionToUsers(User user, User subscriber) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (userRepository.existsById(subscriber.getID())) {

                if (subscriptionRepository.existsByUserIdAndSubscriberId(user.getID(), subscriber.getID())){
                    throw new IllegalArgumentException("Subscription with user: " + user.getID() + " and subscriber: " + subscriber.getID() + " already exists");
                }

                Subscription subscription = new Subscription();
                subscription.setUser(user);
                subscription.setSubscriber(subscriber);
                subscription.setTimestamp(new Timestamp(System.currentTimeMillis()));
                subscriptionService.create(subscription);

                user.getSubscribers().add(subscription);
                subscriber.getSubscriptions().add(subscription);

                userRepository.save(user);
                userRepository.save(subscriber);
            }
            else{
                throw new IllegalArgumentException("User with id " + subscriber.getID() + " does not exists");
            }

            return;
        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    @Override
    public void removeSubscriptionToUsers(User user, User subscriber, Subscription subscription) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (subscriptionRepository.existsById(subscription.getID())) {
                if (userRepository.existsById(subscriber.getID())) {
                    user.getSubscribers().remove(subscription);
                    subscriber.getSubscriptions().remove(subscription);
                    userRepository.save(user);
                    userRepository.save(subscriber);
                    subscriptionRepository.delete(subscription);
                }
                else{
                    throw new IllegalArgumentException("User with id " + subscriber.getID() + " does not exists");
                }

                return;
            }

            throw new IllegalArgumentException("Subscription with ID " + subscription.getID() + " does not exists");

        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    @Override
    public void addRoleToUser(User user, Role role) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (roleRepository.existsById(role.getID())) {
                user.getRoles().add(role);
                role.getUsers().add(user);
                userRepository.save(user);
                roleRepository.save(role);
                return;
            }

            throw new IllegalArgumentException("Role with ID " + role.getID() + " does not exists");

        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    @Override
    public void removeRoleFromUser(User user, Role role) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (roleRepository.existsById(role.getID())) {
                user.getRoles().remove(role);
                role.getUsers().remove(user);
                userRepository.save(user);
                roleRepository.save(role);
                return;
            }

            throw new IllegalArgumentException("Role with ID " + role.getID() + " does not exists");

        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    public List<User> getSubscribers(User user){
        if (userRepository.existsById(user.getID())){
            List<User> subscribers = new ArrayList<>();

            for (Subscription sub : user.getSubscribers()){
                subscribers.add(sub.getSubscriber());
            }

            return subscribers;
        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    public List<User> getSubscribedUsers(User user){
        if (userRepository.existsById(user.getID())){
            List<User> subscribedUsers = new ArrayList<>();

            for (Subscription sub : user.getSubscriptions()){
                subscribedUsers.add(sub.getUser());
            }

            return subscribedUsers;
        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }


    @Override
    protected CrudRepository<User, Long> getRepository() {
        return userRepository;
    }
}

