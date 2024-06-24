package com.marketplace.demo.controller;

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.service.PostService.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PostController {

    private PostService postService;
    private DTOConverter<PostDTO, Post> postDTOConverter;

    @GetMapping
    public List<PostDTO> getAllPosts() {
        Iterable<Post> posts = postService.readAll();

        List<PostDTO> postDTOs = new ArrayList<>();

        for (Post post : posts) {
            postDTOs.add(postDTOConverter.toDTO(post));
        }

        return postDTOs;
    }

    @GetMapping(path = "/{id}")
    public PostDTO getPostById(@PathVariable Long id) {
        return postDTOConverter.toDTO(postService.readById(id).get());
    }

    //TODO: complete the class
}
