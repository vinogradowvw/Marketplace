package com.marketplace.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import com.marketplace.demo.domain.classes.User;
import com.marketplace.demo.domain.repositories.UserRepository;

@RestController
public class UserController {
  
  @Autowired
  private UserRepository userRepository;

  @PostMapping("/insertTestUser")
  public String insertTestUser() {
      User user = new User();
      user.setUsername("testuser");
      user.setEmail("test@example.com");
      user.setPassword("password");
      userRepository.save(user);
      return "Test user inserted successfully";
  }
}
