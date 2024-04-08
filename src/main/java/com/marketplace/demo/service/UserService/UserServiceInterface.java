package com.marketplace.demo.service.UserService;

import com.marketplace.demo.domain.User;

import jakarta.persistence.EntityNotFoundException;

public interface UserServiceInterface {
    
    public User getUserById(Long id) throws EntityNotFoundException;

    public User createUser(User user) throws IllegalArgumentException;

    public User updateUser(User user);

    public void deleteUser(User user);

}
