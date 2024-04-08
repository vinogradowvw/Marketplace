package com.marketplace.demo.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.demo.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
