package com.marketplace.demo.service.ImageService;

import com.marketplace.demo.config.MinIOComponent;
import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.Image;
import com.marketplace.demo.persistance.ImageRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
@AllArgsConstructor
public class ImageService extends CrudServiceImpl<Image, Long> implements ImageServiceInterface {
    
    private ImageRepository imageRepository;
    private MinIOComponent minIOComponent;

    @Override
    public Image create(Image e) throws IllegalArgumentException {
        Optional<Long> id = Optional.ofNullable(e.getID());
        if (id.isPresent()){
            if (imageRepository.existsById(id.get())){
                throw new IllegalArgumentException("Entity with id " + id.get() + " already exists");
            }
        }

        String name = UUID.randomUUID().toString();
        e.setName(name);

        return imageRepository.save(e);
    }

    @Override
    public void deleteById(Long id) throws IllegalArgumentException {
        if (!imageRepository.existsById(id)){
            throw new IllegalArgumentException("Image with id " + id + " does not exist");
        }

        Image image = imageRepository.findById(id).get();
        minIOComponent.deleteObject(image.getName());

        getRepository().deleteById(id);
    }

    @Override
    protected CrudRepository<Image, Long> getRepository() {
        return imageRepository;
    }
}
