package com.marketplace.demo.controller;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.marketplace.demo.domain.*;
import com.marketplace.demo.service.ReviewService.ReviewService;
import com.marketplace.demo.service.kafkaService.KafkaSender;
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
    private ObjectMapper objectMapper;
    private KafkaSender kafkaSender;

    @Autowired
    PostController(PostService postService, DTOConverter<PostDTO, Post> postConverter, KafkaSender kafkaSender,
                   ImageService imageService, ProductService productService, ObjectMapper objectMapper,
                   UserService userService, TagService tagService, ReviewService reviewService) {
        this.postService = postService;
        this.postConverter = postConverter;
        this.imageService = imageService;
        this.productService = productService;
        this.userService = userService;
        this.tagService = tagService;
        this.reviewService = reviewService;
        this.kafkaSender = kafkaSender;
        this.objectMapper = objectMapper;
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

        return postConverter.toDTO(postObj);
    }

    @PostMapping(path = "/{id}")
    public void sendPostToRecSys(@PathVariable("id") Long postId){

        Optional<Post> postOpt = postService.readById(postId);

        if (postOpt.isPresent()) {
            String key = "post: " + postId;

            JsonNode node = objectMapper.valueToTree(postOpt.get());
            ObjectNode obj = objectMapper.createObjectNode();
            obj.put("type", "post_service.upsert");
            obj.set("post", node);

            kafkaSender.recSysSend(key, obj);
        }
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

        return postConverter.toDTO(oldPost.get());
    }

    @PostMapping(path = "/{id}/image")
    public PostDTO addImageToPost(@PathVariable("id") Long postId, @RequestParam Long imageId) {
        Post post = postService.addImageToPost(postService.readById(postId).get(), imageService.readById(imageId).get());
        return postConverter.toDTO(post);
    }

    @GetMapping(path = "/recommendations/post/{postId}")
    public List<PostDTO> getRecPostByPost(@PathVariable("postId") Long postId,
                                          @RequestParam(defaultValue = "20") int limit) {

        List<Long> postIds = new ArrayList<>();

        String key = "post: " + postId;
        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("type", "post_service.get_recommended_posts_by_post_id");
        obj.put("post_id", postId);
        obj.put("limit", limit);

        Optional<JsonNode> ansNode = kafkaSender.sendRecSysRequest(key, obj);
        if (ansNode.isPresent()) {
            JsonNode idsNode = ansNode.get().get("data");

            if (idsNode.isArray()) {
                for (JsonNode idNode : idsNode) {
                    postIds.add(idNode.asLong());
                }
            }
        }

        return postService.getEntities(postIds)
                .stream().map(postConverter::toDTO).toList();
    }

    @GetMapping(path = "/recommendations/user/{userId}")
    public List<PostDTO> getRecPostByUser(@PathVariable("userId") Long userId,
                                          @RequestParam(defaultValue = "20") int limit) {

        List<Long> postIds = new ArrayList<>();

        String key = "userId: " + userId;
        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("type", "post_service.get_recommended_posts_by_user_id");
        obj.put("user_id", userId);
        obj.put("limit", limit);

        Optional<JsonNode> ansNode = kafkaSender.sendRecSysRequest(key, obj);
        if (ansNode.isPresent()) {
            JsonNode idsNode = ansNode.get().get("data");

            if (idsNode.isArray()) {
                for (JsonNode idNode : idsNode) {
                    postIds.add(idNode.asLong());
                }
            }
        }

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

        String key = "user: " + userId;

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("type", "user_service.update_users_vector");
        rootNode.put("user_id", userId);
        rootNode.put("post_id", postId);

        kafkaSender.recSysSend(key, rootNode);

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

        String key = "post: " + id;

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("type", "post_service.delete_by_id");
        rootNode.put("post_id", id);

        kafkaSender.recSysSend(key, rootNode);

    }

}
