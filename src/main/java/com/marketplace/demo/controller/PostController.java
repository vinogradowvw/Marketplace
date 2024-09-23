package com.marketplace.demo.controller;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.demo.domain.*;
import com.marketplace.demo.service.ReviewService.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.service.ImageService.ImageService;
import com.marketplace.demo.service.PostService.PostService;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.TagService.TagService;
import com.marketplace.demo.service.UserService.UserService;

import org.springframework.web.client.RestClient;

@RestController
@RequestMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {
    
    private PostService postService;
    private DTOConverter<PostDTO, Post> postConverter;
    private ImageService imageService;
    private ProductService productService;
    private UserService userService;
    private TagService tagService;
    private ReviewService reviewService;
    private String baseUrl;
    private RestClient postClient;
    private ObjectMapper objectMapper;

    @Autowired
    PostController(PostService postService, DTOConverter<PostDTO, Post> postConverter,
                   ImageService imageService, ProductService productService,
                   UserService userService, TagService tagService, ReviewService reviewService, @Value("${api.url}") String baseUrl) {
        this.postService = postService;
        this.postConverter = postConverter;
        this.imageService = imageService;
        this.productService = productService;
        this.userService = userService;
        this.tagService = tagService;
        this.reviewService = reviewService;
        this.baseUrl = baseUrl + "/post";
        postClient = RestClient.builder().baseUrl(this.baseUrl).build();
        objectMapper = new ObjectMapper();
    }

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
    public PostDTO createPost(@RequestBody PostDTO postDTO) throws JsonProcessingException {

        Post postObj = postService.create(postConverter.toEntity(postDTO));
        PostDTO post = postConverter.toDTO(postObj);

        String postJSON = objectMapper.writeValueAsString(post);

        postClient.post()
                .uri("/save")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(postJSON)
                .retrieve()
                .toBodilessEntity();

        return post;
    }

    @PutMapping(path = "/{id}")
    public PostDTO updatePost(@PathVariable("id") Long id, @RequestBody PostDTO postDTO) {
        Post post = postConverter.toEntity(postDTO);
        Optional<Post> oldPost = postService.readById(id);

        oldPost.ifPresent(value -> {
            value.setName(post.getName());
            value.setDescription(post.getDescription());
        });

        postService.update(id, oldPost.get());

        postClient.post()
                .uri("/save")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(oldPost.get())
                .retrieve()
                .toBodilessEntity();

        return postConverter.toDTO(oldPost.get());
    }

    @PostMapping(path = "/{id}/image")
    public PostDTO addImageToPost(@PathVariable("id") Long postId, @RequestParam Long imageId) {
        Post post = postService.addImageToPost(postService.readById(postId).get(), imageService.readById(imageId).get());
        return postConverter.toDTO(post);
    }

    @GetMapping(path = "/post/recommendations/post/{postId}")
    public List<PostDTO> getRecPostByPost(@PathVariable("postId") Long postId) {

        List<Long> postIds = Arrays.asList(Objects.requireNonNull(postClient.get()
                .uri("/recommendations/post/" + postId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Long[].class)
                .getBody()));

        return postService.getEntities(postIds).stream().map(postConverter::toDTO).toList();
    }

    @GetMapping(path = "/post/recommendations/user/{userId}")
    public List<PostDTO> getRecPostByUser(@PathVariable("userId") Long userId) {

        List<Long> postIds = Arrays.asList(Objects.requireNonNull(postClient.get()
                .uri("/recommendations/user/" + userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Long[].class)
                .getBody()));

        return postService.getEntities(postIds).stream().map(postConverter::toDTO).toList();
    }

    @DeleteMapping(path = "/{postId}/image/{imageId}")
    public PostDTO removeImageFromPost(@PathVariable("postId") Long postId, @PathVariable("imageId") Long imageId) {
        Post post = postService.removeImageFromPost(postService.readById(postId).get(), imageService.readById(imageId).get());
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

    @PostMapping(path = "/{postId}/like")
    public PostDTO likePost(@PathVariable("postId") Long postId, @RequestParam Long userId) {
        Post post = postService.likePost(postService.readById(postId).get(), userService.readById(userId).get());

        postClient.post()
                .uri(baseUrl + "/user/like/user/" + userId + "/post/" + postId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity();

        return postConverter.toDTO(post);
    }

    @PostMapping(path = "/{postId}/review")
    public PostDTO addReviewToPost(@PathVariable("postId") Long postId, @RequestParam Long reviewId, @RequestParam Long userId) {
        Post post = postService.addReviewToPost(postService.readById(postId).get(), reviewService.readById(reviewId).get(), userService.readById(userId).get());
        return postConverter.toDTO(post);
    }

    @DeleteMapping(path = "/{postId}/review/{reviewId}")
    public PostDTO removeReviewFromPost(@PathVariable("postId") Long postId, @PathVariable("reviewId") Long reviewId, @RequestParam Long userId) {
        Post post = postService.deleteReviewFromPost(postService.readById(postId).get(), reviewService.readById(reviewId).get(), userService.readById(userId).get());
        return postConverter.toDTO(post);
    }

    @GetMapping(path = "/{id}/rating")
    public double getAVGPostRating(@PathVariable("id") Long id){
        return postService.getAVGPostRating(postService.readById(id).get());
    }

    @GetMapping(path = "/{id}/views")
    public Long getPostViews(@PathVariable("id") Long id){
        return postService.getPostViews(postService.readById(id).get());
    }

    @PostMapping(path = "/{id}/views")
    public void addView(@PathVariable("id") Long id, @RequestParam Long userId){
        postService.addView(postService.readById(id).get(), userService.readById(userId).get());
    }

    @DeleteMapping(path = "/{id}")
    public void deletePost(@PathVariable("id") Long id) {
        postService.deleteById(id);
    }

}
