package com.marketplace.demo;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marketplace.demo.domain.Image;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.persistance.ImageRepository;
import com.marketplace.demo.persistance.PostRepository;
import com.marketplace.demo.service.ImageService.ImageService;
import com.marketplace.demo.service.PostService.PostService;
import com.marketplace.demo.service.ProductService.ProductService;

@SpringBootTest
class PostServiceUnitTests {

	@Autowired
	ProductService productService;
	@Autowired
	ImageService imageService;
	@Autowired
	PostService postService;
	@MockBean
	PostRepository postRepository;
	@MockBean
	ImageRepository imageRepository;

	Post post;
	Image image1;
	Image image2;
	List<Image> images;

	@BeforeEach
	void setUp() {
		image1 = new Image();
		image1.setId(1L);
		image1.setPath("/test/path/1");
		image1.setPostsWithImg(new ArrayList<>());

		image2 = new Image();
		image1.setId(2L);
		image2.setPath("/test/path/2");
		image2.setPostsWithImg(new ArrayList<>());

		post = new Post();
		post.setId(1L);
		post.setDescription("testDescr");
		post.setImages(new ArrayList<>());

		images = new ArrayList<Image>();
		images.add(image1);
		images.add(image2);

		Mockito.when(postRepository.existsById(post.getID())).thenReturn(true);
		Mockito.when(imageRepository.existsById(image1.getID())).thenReturn(true);
		Mockito.when(imageRepository.existsById(image2.getID())).thenReturn(true);

		Mockito.when(imageRepository.findById(image1.getID())).thenReturn(Optional.of(image1));
		Mockito.when(imageRepository.findById(image2.getID())).thenReturn(Optional.of(image2));
	}
	@Test
	void addPostWithImage() {
		postService.addPostImage(post, image1);
		postService.addPostImage(post, image2);

		Assertions.assertTrue(post.getImages().contains(image1));
		Assertions.assertTrue(post.getImages().contains(image2));
        Assertions.assertTrue(image1.getPostsWithImg().contains(post));
		Assertions.assertTrue(image2.getPostsWithImg().contains(post));

		Assertions.assertEquals(2, post.getImages().size());
        Assertions.assertEquals(1, image1.getPostsWithImg().size());
		Assertions.assertEquals(1, image2.getPostsWithImg().size());

		Mockito.verify(postRepository, Mockito.atLeastOnce()).save(post);
        Mockito.verify(imageRepository, Mockito.atLeastOnce()).save(image1);
		Mockito.verify(imageRepository, Mockito.atLeastOnce()).save(image2);

	}
}