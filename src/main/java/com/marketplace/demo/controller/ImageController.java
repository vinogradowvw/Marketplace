package com.marketplace.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.marketplace.demo.controller.converter.ImageDTOConverter;
import com.marketplace.demo.controller.dto.ImageDTO;
import com.marketplace.demo.domain.Image;
import com.marketplace.demo.service.ImageService.ImageService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/image", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ImageController {

    @Value("${images.path}")
    private String imagesPath;

    public ImageDTOConverter imageDTOConverter;
    public ImageService imageService;

    // @GetMapping("/{id}")
    // public Image

    @PostMapping
    public ImageDTO uploadImage(@RequestBody ImageDTO imageDTO, @RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        
        Image image = imageDTOConverter.toEntity(imageDTO);

        if (file != null) {
            
            File imagesDir = new File(imagesPath);
            if (!imagesDir.exists()) {
                imagesDir.mkdir();
            }
            String uuid = UUID.randomUUID().toString();
            String filename = file.getOriginalFilename() + "_" + uuid;

            file.transferTo(new File(imagesPath + "/"+ filename));

            image.setPath(filename);
        }

        Image newImage = imageService.create(image);
        
        return imageDTOConverter.toDTO(newImage);
    }
}
