package com.marketplace.demo.service.ImageService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.Image;
import com.marketplace.demo.persistance.ImageRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ImageService implements ImageServiceInterface {
    
    private ImageRepository imageRepository;

    @Override
    public Image getImageById(Long id) throws EntityNotFoundException {
        return imageRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException("No image with id " + id + " found."));
    }

    @Override
    public Image createImage(Image image) throws IllegalArgumentException {
        return imageRepository.save(image);
    }

    @Override
    public Image updateImage(Image image) {

        if (imageRepository.existsById(image.getID())) {
            return imageRepository.save(image);
        }
        throw new IllegalArgumentException("Image with this ID does not exists");

    }

    @Override
    public void deleteImage(Image image) {
        
        if (imageRepository.existsById(image.getID())) {
            imageRepository.delete(image);
        }
        throw new IllegalArgumentException("Image with this ID does not exists");
    }

}
