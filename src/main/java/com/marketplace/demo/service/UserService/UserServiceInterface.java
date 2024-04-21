package com.marketplace.demo.service.UserService;

import com.marketplace.demo.domain.Subscription;
import com.marketplace.demo.domain.User;

import com.marketplace.demo.service.CrudService;
import jakarta.persistence.EntityNotFoundException;

public interface UserServiceInterface extends CrudService<User, Long> {
    public void addSubscriptionToUsers(User user, User subscriber, Subscription subscription) throws IllegalArgumentException;
    public void removeSubscriptionToUsers(User user, User subscriber, Subscription subscription) throws IllegalArgumentException;
}
