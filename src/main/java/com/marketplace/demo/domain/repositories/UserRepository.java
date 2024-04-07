package com.marketplace.demo.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.demo.domain.classes.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
