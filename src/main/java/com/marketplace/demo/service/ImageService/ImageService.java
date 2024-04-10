package com.marketplace.demo.service.ImageService;

import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.Image;
import com.marketplace.demo.persistance.ImageRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ImageService extends CrudServiceImpl<Image, Long> implements ImageServiceInterface {
    
    private ImageRepository imageRepository;

    @Override
    protected CrudRepository<Image, Long> getRepository() {
        return imageRepository;
    }
}
