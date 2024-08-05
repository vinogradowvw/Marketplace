package com.marketplace.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.demo.domain.Image;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.domain.Tag;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.service.ImageService.ImageService;
import com.marketplace.demo.service.PostService.PostService;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.TagService.TagService;
import com.marketplace.demo.service.UserService.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {
    
    private PostService postService;
    private DTOConverter<PostDTO, Post> postConverter;
    private ImageService imageService;
    private ProductService productService;
    private UserService userService;
    private TagService tagService;

    @GetMapping
    public List<PostDTO> getAllPosts() {
        Iterable<Post> posts = postService.readAll();
        List<PostDTO> postDTOs = new ArrayList<>();
        
        for (Post post: posts) {
            postDTOs.add(postConverter.toDTO(post));
        }
        
        return postDTOs;
    }

    @GetMapping(path = "/{id}")
    public PostDTO getPostById(@PathVariable("id")Long id){
        return postConverter.toDTO(postService.readById(id).get());
    }

    @PostMapping
    public PostDTO createPost(PostDTO postDTO) {
        return postConverter.toDTO(postService.create(postConverter.toEntity(postDTO)));
    }

    @PutMapping(path = "/{id}")
    public PostDTO updatePost(@PathVariable("id") Long id, @RequestBody PostDTO postDTO) {
        Post post = postConverter.toEntity(postDTO);
        Optional<Post> oldPost = postService.readById(id);

        if (oldPost.isPresent()) {
            post.setImages(oldPost.get().getImages());
            post.setLikedUsers(oldPost.get().getLikedUsers());
            post.setTags(oldPost.get().getTags());
            post.setUser(oldPost.get().getUser());
        }

        post.setId(id);
        postService.update(id, post);

        return postConverter.toDTO(post);
    }

    @PostMapping(path = "/{id}/image")
    public PostDTO addImageToPost(@PathVariable("id") Long postId, @RequestParam Long imageId) {
        Post post = postService.addImageToPost(postService.readById(postId).get(), imageService.readById(imageId).get());
        return postConverter.toDTO(post);
    }

    @DeleteMapping(path = "/{postId}/image/{imageId}")
    public PostDTO removeImageFromPost(@PathVariable("postId") Long postId, @PathVariable("imageId") Long imageId) {
        Post post = postService.addImageToPost(postService.readById(postId).get(), imageService.readById(imageId).get());
        return postConverter.toDTO(post);
    }

    @PostMapping(path = "/{id}/product")
    public PostDTO addProductToPost(@PathVariable("id") Long postId, @RequestParam Long productId) {
        Post post = postService.addProductToPost(postService.readById(postId).get(), productService.readById(productId).get());
        return postConverter.toDTO(post);
    }

    @DeleteMapping(path = "/{postId}/product/{productId}")
    public PostDTO removeProductFromPost(@PathVariable("postId") Long postId, @PathVariable("productId") Long productId) {
        Post post = postService.removeProductFromPost(postService.readById(postId).get(), productService.readById(productId).get());
        return postConverter.toDTO(post);
    }

    @PostMapping(path = "/{id}/user")
    public PostDTO addUserToPost(@PathVariable("id") Long postId, @RequestParam Long userId) {
        Post post = postService.addUserToPost(postService.readById(postId).get(), userService.readById(userId).get());
        return postConverter.toDTO(post);
    }

    @DeleteMapping(path = "/{postId}/user/{userId}")
    public PostDTO removeUserFromPost(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
        Post post = postService.removeUserFromPost(postService.readById(postId).get(), userService.readById(userId).get());
        return postConverter.toDTO(post);
    }

    @PostMapping(path = "/{id}/tag")
    public PostDTO addTagToPost(@PathVariable("id") Long postId, @RequestParam Long tagId) {
        Post post = postService.addTagToPost(postService.readById(postId).get(), tagService.readById(tagId).get());
        return postConverter.toDTO(post);
    }

    @DeleteMapping(path = "/{postId}/tag/{tagId}")
    public PostDTO removeTagFromPost(@PathVariable("postId") Long postId, @PathVariable("tagId") Long tagId) {
        Post post = postService.removeTagFromPost(postService.readById(postId).get(), tagService.readById(tagId).get());
        return postConverter.toDTO(post);
    }

    @PostMapping(path = "/{postId}/likeByUser/{userId}")
    public PostDTO likePost(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
        Post post = postService.likePost(postService.readById(postId).get(), userService.readById(userId).get());
        return postConverter.toDTO(post);
    }

    @DeleteMapping(path = "/{id}")
    public void deletePost(@PathVariable("id") Long id) {

        Optional<Post> post = postService.readById(id);
        
        if (post.isPresent()) {
            for (User user : post.get().getLikedUsers()) {
                postService.likePost(post.get(), user);
            }

            for (Tag tag : post.get().getTags()) {
                postService.removeTagFromPost(post.get(), tag);
            }

            for (Image image : post.get().getImages()) {
                imageService.deleteById(image.getID());
            }

            productService.deleteById(post.get().getProduct().getID());
        }
        
        postService.deleteById(id);
    }

}
