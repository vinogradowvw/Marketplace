package com.marketplace.demo.service.ImageService;

import com.marketplace.demo.domain.Image;

import com.marketplace.demo.service.CrudService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.repository.CrudRepository;

public interface ImageServiceInterface extends CrudService<Image, Long> {
}
