package com.marketplace.demo.unitTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.demo.controller.PostController;
import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.service.ImageService.ImageService;
import com.marketplace.demo.service.PostService.PostService;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.ReviewService.ReviewService;
import com.marketplace.demo.service.TagService.TagService;
import com.marketplace.demo.service.UserService.UserService;
import com.marketplace.demo.service.kafkaService.KafkaSender;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class PostControllerUnitTest {

    @MockBean
    private PostService postService;
    @MockBean
    private DTOConverter<PostDTO, Post> postConverter;
    @MockBean
    private ImageService imageService;
    @MockBean
    private ProductService productService;
    @MockBean
    private UserService userService;
    @MockBean
    private TagService tagService;
    @MockBean
    private ReviewService reviewService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private KafkaSender kafkaSender;
    @Autowired
    private PostController postController;

    @Test
    public void SendPostToRecSysTest(){

        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        post.setName("Test Post");

        Mockito.when(postService.readById(postId)).thenReturn(Optional.of(post));

        postController.sendPostToRecSys(postId);

        Mockito.verify(kafkaSender, Mockito.times(1)).recSysSend(Mockito.anyString(), Mockito.any(JsonNode.class));
    }

}
