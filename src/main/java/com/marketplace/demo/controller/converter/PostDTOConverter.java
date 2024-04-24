package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.domain.Post;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostDTOConverter implements DTOConverter<Post, PostDTO> {



    @Override
    public Post toDTO(PostDTO postDTO) {
        return null;
    }

    @Override
    public PostDTO toEntity(Post post) {
        return null;
    }
}
