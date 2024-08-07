package com.marketplace.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import com.marketplace.demo.config.MinIOComponent;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.marketplace.demo.controller.converter.ImageDTOConverter;
import com.marketplace.demo.controller.dto.ImageDTO;
import com.marketplace.demo.domain.Image;
import com.marketplace.demo.service.ImageService.ImageService;


@RestController
@RequestMapping(value = "/image", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImageController {

    @Value("${minio.bucket-name}")
    private String bucketName;

    private ImageDTOConverter imageDTOConverter;
    private ImageService imageService;
    private MinIOComponent minIOComponent;

    @Autowired
    ImageController(ImageService imageService, ImageDTOConverter imageDTOConverter, MinIOComponent minIOComponent) {
        this.imageService = imageService;
        this.imageDTOConverter = imageDTOConverter;
        this.minIOComponent = minIOComponent;
    }

    @PostMapping
    public ImageDTO uploadImage(@RequestBody ImageDTO imageDTO, @RequestParam("file") MultipartFile file){
        
        Image image = imageDTOConverter.toEntity(imageDTO);
        Image newImage = imageService.create(image);

        try {
            InputStream in = new ByteArrayInputStream(file.getBytes());
            String fileName = newImage.getName();
            minIOComponent.putObject(fileName, in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageDTOConverter.toDTO(newImage);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable("id") Long id){
        Optional<Image> image = imageService.readById(id);

        if (image.isPresent()) {
            try {
                Optional<InputStream> content = minIOComponent.getObject(image.get().getName());

                if (content.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                String contentType = "image/" + image.get().getExtension();

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.get().available()));

                return new ResponseEntity<>(new InputStreamResource(content.get()), headers, HttpStatus.OK);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping(path = "/{id}")
    public void deleteImage(@PathVariable("id") Long id){
        Optional<Image> image = imageService.readById(id);

        image.ifPresent(value -> imageService.deleteById(value.getID()));
    }
}
