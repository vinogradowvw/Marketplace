package com.marketplace.demo.unitTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.demo.controller.PostController;
import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Tag;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    // @Test
    // public void SendPostToRecSysTest(){

    //     Long postId = 1L;
    //     Post post = new Post();
    //     post.setId(postId);
    //     post.setName("Test Post");
    //     Tag tag1 = new Tag();
    //     tag1.setId(1L);
    //     tag1.setName("tag1");
    //     Tag tag2 = new Tag();
    //     tag2.setId(2L);
    //     tag2.setName("tag2");
    //     tag1.setPosts(List.of(post));
    //     tag2.setPosts(List.of(post));
    //     post.setTags(List.of(tag1, tag2));
    //     PostDTO postDTO = new PostDTO(1L, "Test Post", 0L, "desc",
    //             null, null, new ArrayList<Long>(), List.of(1L, 2L), new ArrayList<Long>(),
    //             new ArrayList<Long>());

    //     Mockito.when(postService.readById(postId)).thenReturn(Optional.of(post));
    //     Mockito.when(tagService.getEntitiesbyIds(List.of(1L, 2L))).thenReturn(List.of(tag1, tag2));
    //     Mockito.when(postConverter.toDTO(post)).thenReturn(postDTO);

    //     postController.sendPostToRecSys(postId);

    //     Mockito.verify(kafkaSender, Mockito.times(1)).recSysSend(Mockito.anyString(), Mockito.any(JsonNode.class));
    // }

}
