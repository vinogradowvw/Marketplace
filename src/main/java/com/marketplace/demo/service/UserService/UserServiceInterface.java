package com.marketplace.demo.service.UserService;

import com.marketplace.demo.domain.User;

import com.marketplace.demo.service.CrudService;
import jakarta.persistence.EntityNotFoundException;

public interface UserServiceInterface extends CrudService<User, Long> {
}
