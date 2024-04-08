package com.marketplace.demo.service.UserService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.marketplace.demo.domain.User;
import com.marketplace.demo.persistance.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserServiceInterface {

    private UserRepository userRepository;


    @Override
    public User getUserById(Long id) throws EntityNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No user with id " + id + " found."));
    }

    @Override
    public User createUser(User user) throws IllegalArgumentException {
        // checking if email is correct
        String email = user.getEmail();
        String regex = "\\w+@\\w+\\.\\w+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find()) {throw new IllegalArgumentException("Email is not in correct form");}
        if (user.getPassword().length() < 4) {throw new IllegalArgumentException("Password must be longer");}

        return userRepository.save(user);
    }


    @Override
    public User updateUser(User user) {

        if (userRepository.existsById(user.getID())) {
            String email = user.getEmail();
            String regex = "\\w+@\\w+\\.\\w+";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.find()) {throw new IllegalArgumentException("Email is not in correct form");}
            if (user.getPassword().length() < 4) {throw new IllegalArgumentException("Password must be longer");}

            return userRepository.save(user);
        }
        throw new IllegalArgumentException("User with this ID does not exists");
    }


    @Override
    public void deleteUser(User user) {
        if (userRepository.existsById(user.getID())) {
            userRepository.delete(user);
            return;
        }
        throw new IllegalArgumentException("User with this ID does not exists");
    }
}
