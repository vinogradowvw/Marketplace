package com.marketplace.demo.controller;

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.controller.dto.TagDTO;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Tag;
import com.marketplace.demo.service.PostService.PostService;
import com.marketplace.demo.service.TagService.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tag", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class TagController {

    private TagService tagService;
    private DTOConverter<TagDTO, Tag> dtoConverter;
    private DTOConverter<PostDTO, Post> postConverter;
    private PostService postService;

    @GetMapping
    public List<TagDTO> getAllTags(){
        Iterable<Tag> tags = tagService.readAll();
        List<TagDTO> tagDTOs = new ArrayList<>();

        for (Tag tag : tags) {
            tagDTOs.add(dtoConverter.toDTO(tag));
        }

        return tagDTOs;
        
    }

    @GetMapping(path = "/{id}")
    public TagDTO getTagById(@PathVariable("id")Long id){
        return dtoConverter.toDTO(tagService.readById(id).get());
    }

    @PostMapping
    public TagDTO createTag(TagDTO tagDTO){
        return dtoConverter.toDTO(tagService.create(dtoConverter.toEntity(tagDTO)));
    }

    @PutMapping(path = "/{id}")
    public TagDTO updateTag(@PathVariable("id") Long id, @RequestBody TagDTO tagDTO){
        Tag tag = dtoConverter.toEntity(tagDTO);
        Optional<Tag> oldTag = tagService.readById(id);

        oldTag.ifPresent(value -> tag.setPosts(value.getPosts()));
        tag.setId(id);

        tagService.update(id, tag);
        return dtoConverter.toDTO(tag);
    }

    @GetMapping(path = "/{id}/posts")
    public List<PostDTO> getPostByTag(@PathVariable("id") Long id){
        List<PostDTO> posts = new ArrayList<>();
        Optional<Tag> tag = tagService.readById(id);

        if (tag.isPresent()) {
            for (Post post : tag.get().getPosts()) {
                posts.add(postConverter.toDTO(post));
            }
        }

        return posts;
    }

    @DeleteMapping(path = "/{id}")
    public void deleteTag(@PathVariable("id") Long id){
        Optional<Tag> tag = tagService.readById(id);

        if (tag.isPresent()) {
            for (Post post : tag.get().getPosts()) {
                postService.removeTagFromPost(post, tag.get());
            }
        }

        tagService.deleteById(id);
    }
}
