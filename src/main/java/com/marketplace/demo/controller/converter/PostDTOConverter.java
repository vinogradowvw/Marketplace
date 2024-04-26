package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.domain.Post;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostDTOConverter implements DTOConverter<PostDTO, Post> {


    @Override
    public PostDTO toDTO(Post post) {
        return null;
    }

    @Override
    public Post toEntity(PostDTO postDTO) {
        return null;
    }
}
