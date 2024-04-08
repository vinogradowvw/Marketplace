package com.marketplace.demo.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.demo.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}