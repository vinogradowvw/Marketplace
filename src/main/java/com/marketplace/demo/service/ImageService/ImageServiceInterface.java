package com.marketplace.demo.service.ImageService;

import com.marketplace.demo.domain.Image;

import jakarta.persistence.EntityNotFoundException;

public interface ImageServiceInterface {
    
    public Image getImageById(Long id) throws EntityNotFoundException;

    public Image createImage(Image image) throws IllegalArgumentException;

    public Image updateImage(Image image);

    public void deleteImage(Image image);
}
