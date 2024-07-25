package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.ImageDTO;
import com.marketplace.demo.domain.Image;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.service.PostService.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ImageDTOConverter implements DTOConverter<ImageDTO, Image> {

    private final PostService postService;

    @Override
    public ImageDTO toDTO(Image image) {

        Long postId = Optional.ofNullable(image.getPost()).map(Post::getID).orElse(null);

        return new ImageDTO(image.getID(), image.getPath(), postId);
    }

    @Override
    public Image toEntity(ImageDTO imageDTO) {
        Image image = new Image();

        image.setId(imageDTO.id());
        image.setPath(imageDTO.path());

        Optional<Long> optPostId = Optional.ofNullable(imageDTO.postId());
        Optional<Post> optPost = Optional.empty();

        if (optPostId.isPresent()) {
            optPost = postService.readById(optPostId.get());
        }

        if (optPost.isPresent()) {
            image.setPost(optPost.get());
        }
        else{
            image.setPost(null);
        }

        return image;
    }
}
