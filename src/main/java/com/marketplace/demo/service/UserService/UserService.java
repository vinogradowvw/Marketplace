package com.marketplace.demo.service.UserService;

import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.User;
import com.marketplace.demo.persistance.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService extends CrudServiceImpl<User, Long> implements UserServiceInterface {

    private UserRepository userRepository;

    @Override
    protected CrudRepository<User, Long> getRepository() {
        return userRepository;
    }
}
