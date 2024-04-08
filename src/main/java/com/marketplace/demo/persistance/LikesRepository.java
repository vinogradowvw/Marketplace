package com.marketplace.demo.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.demo.domain.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long> {

}