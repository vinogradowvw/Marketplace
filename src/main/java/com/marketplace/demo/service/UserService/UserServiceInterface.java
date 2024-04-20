package com.marketplace.demo.service.UserService;

import com.marketplace.demo.domain.Subscription;
import com.marketplace.demo.domain.User;

import com.marketplace.demo.service.CrudService;
import jakarta.persistence.EntityNotFoundException;

public interface UserServiceInterface extends CrudService<User, Long> {
    public User addSubcriberToUser(User user, Subscription subscription) throws IllegalArgumentException;
    public User removeSubcriberToUser(User user, Subscription subscription) throws IllegalArgumentException;

    public User addSubcribeToUser(User user, Subscription subscription) throws IllegalArgumentException;
    public User removeSubcribeToUser(User user, Subscription subscription) throws IllegalArgumentException;
}
