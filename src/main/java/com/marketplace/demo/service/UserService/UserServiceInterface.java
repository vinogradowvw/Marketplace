package com.marketplace.demo.service.UserService;

import com.marketplace.demo.domain.*;

import com.marketplace.demo.service.CrudService;

public interface UserServiceInterface extends CrudService<User, Long> {
    public void addSubscriptionToUsers(User user, User subscriber, Subscription subscription) throws IllegalArgumentException;
    public void removeSubscriptionToUsers(User user, User subscriber, Subscription subscription) throws IllegalArgumentException;

    public void addRoleToUser(User user, Role role) throws IllegalArgumentException;
    public void removeRoleFromUser(User user, Role role) throws IllegalArgumentException;
}
