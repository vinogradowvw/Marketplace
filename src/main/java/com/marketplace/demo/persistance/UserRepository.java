package com.marketplace.demo.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.demo.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
