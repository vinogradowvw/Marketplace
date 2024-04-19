package com.marketplace.demo;


import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marketplace.demo.domain.Image;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.domain.Tag;
import com.marketplace.demo.persistance.ImageRepository;
import com.marketplace.demo.persistance.PostRepository;
import com.marketplace.demo.persistance.ProductRepository;
import com.marketplace.demo.persistance.TagRepository;
import com.marketplace.demo.service.ImageService.ImageService;
import com.marketplace.demo.service.PostService.PostService;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.TagService.TagService;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostServiceUnitTests {
	@Autowired
	PostService postService;
	@Autowired
	TagService tagService;
	@MockBean
	PostRepository postRepository;
	@MockBean
	ImageRepository imageRepository;
	@MockBean
	ProductRepository productRepository;
	@MockBean
	TagRepository tagRepository;

	Post post;
	Product product1;
	Product product2;
	Image image1;
	Image image2;
	Tag tag1;
	Tag tag2;

	@BeforeEach
	public void setUp() {
		image1 = new Image();
		image1.setId(1L);
		image1.setPath("/test/path/1");
		image1.setPost(null);

		image2 = new Image();
		image1.setId(2L);
		image2.setPath("/test/path/2");
		image2.setPost(null);

		tag1 = new Tag();
		tag1.setId(1L);
		tag1.setName("testTag");
		tag1.setPosts(new ArrayList<>());

		tag2 = new Tag();
		tag2.setId(2L);
		tag2.setName("testTag");
		tag2.setPosts(new ArrayList<>());

		post = new Post();
		post.setId(1L);
		post.setDescription("testDescr");
		post.setImages(new ArrayList<>());
		post.setProductsInPost(new ArrayList<>());
		post.setTags(new ArrayList<>());

		product1 = new Product();
		product1.setId(1L);
		product1.setName("testName");
		product1.setDescription("testDescr");
		product1.setPost(null);
		product1.setPrice(100000);

		product2 = new Product();
		product2.setId(2L);
		product2.setName("testName");
		product2.setDescription("testDescr");
		product2.setPost(null);
		product2.setPrice(100000);
	}

	@BeforeEach
	void setRules() {
		Mockito.when(postRepository.save(post)).thenReturn(post);
		Mockito.when(postRepository.existsById(post.getID())).thenReturn(true);
		Mockito.when(productRepository.existsById(product1.getID())).thenReturn(true);
		Mockito.when(productRepository.existsById(product2.getID())).thenReturn(true);
		Mockito.when(tagRepository.existsById(tag1.getID())).thenReturn(true);
		Mockito.when(tagRepository.existsById(tag2.getID())).thenReturn(true);
		Mockito.when(imageRepository.existsById(image1.getID())).thenReturn(true);
		Mockito.when(imageRepository.existsById(image2.getID())).thenReturn(true);
		Mockito.when(imageRepository.findById(image1.getID())).thenReturn(Optional.of(image1));
		Mockito.when(imageRepository.findById(image2.getID())).thenReturn(Optional.of(image2));
	}

	@Test
	void addImageToPost() {
		post = postService.addImageToPost(post, image1);
		post = postService.addImageToPost(post, image2);

		Assertions.assertTrue(post.getImages().contains(image1));
		Assertions.assertTrue(post.getImages().contains(image2));

        Assertions.assertEquals(image1.getPost(), post);
		Assertions.assertEquals(image2.getPost(), post);

		Assertions.assertEquals(2, post.getImages().size());

		Mockito.verify(postRepository, Mockito.atLeastOnce()).save(post);
        Mockito.verify(imageRepository, Mockito.atLeastOnce()).save(image1);
		Mockito.verify(imageRepository, Mockito.atLeastOnce()).save(image2);

	}

	@Test
	void removeImageFromPost() {
		post = postService.addImageToPost(post, image1);
		post = postService.addImageToPost(post, image2);

		post = postService.removeImageFromPost(post, image1);

		Assertions.assertFalse(post.getImages().contains(image1));
		Assertions.assertTrue(post.getImages().contains(image2));

		post = postService.removeImageFromPost(post, image2);

		Assertions.assertFalse(post.getImages().contains(image1));
		Assertions.assertFalse(post.getImages().contains(image2));
		Assertions.assertEquals(post.getImages(), new ArrayList<>());

        Assertions.assertEquals(image1.getPost(), null);
		Assertions.assertEquals(image2.getPost(), null);

		Mockito.verify(postRepository, Mockito.atLeastOnce()).save(post);
        Mockito.verify(imageRepository, Mockito.atLeastOnce()).save(image1);
		Mockito.verify(imageRepository, Mockito.atLeastOnce()).save(image2);

	}
	
	@Test
	void addProductToPost() {
		post = postService.addProductToPost(post, product1);
		post = postService.addProductToPost(post, product2);

		Assertions.assertTrue(post.getProductsInPost().contains(product1));
		Assertions.assertTrue(post.getProductsInPost().contains(product2));

        Assertions.assertEquals(product1.getPost(), post);
		Assertions.assertEquals(product2.getPost(), post);

		Assertions.assertEquals(2, post.getProductsInPost().size());

		Mockito.verify(postRepository, Mockito.atLeastOnce()).save(post);
        Mockito.verify(productRepository, Mockito.atLeastOnce()).save(product1);
		Mockito.verify(productRepository, Mockito.atLeastOnce()).save(product2);
	}

	@Test
	void removeProductFromPost() {
		post = postService.addProductToPost(post, product1);
		post = postService.addProductToPost(post, product2);
		
		post = postService.removeProductFromPost(post, product1);

		Assertions.assertTrue(post.getProductsInPost().contains(product2));

		post = postService.removeProductFromPost(post, product2);

        Assertions.assertEquals(product1.getPost(), null);
		Assertions.assertEquals(product2.getPost(), null);
		Assertions.assertEquals(post.getProductsInPost(), new ArrayList<>());

		Assertions.assertEquals(0, post.getProductsInPost().size());

		Mockito.verify(postRepository, Mockito.atLeastOnce()).save(post);
        Mockito.verify(productRepository, Mockito.atLeastOnce()).save(product1);
		Mockito.verify(productRepository, Mockito.atLeastOnce()).save(product2);
	}

	@Test
	void addTagToPost() {
		post = postService.addTagToPost(post, tag1);
		post = postService.addTagToPost(post, tag2);

		Assertions.assertTrue(post.getTags().contains(tag1));
		Assertions.assertTrue(post.getTags().contains(tag2));

        Assertions.assertTrue(tag1.getPosts().contains(post));
		Assertions.assertTrue(tag2.getPosts().contains(post));

		Assertions.assertEquals(2, post.getTags().size());

		Mockito.verify(postRepository, Mockito.atLeastOnce()).save(post);
        Mockito.verify(tagRepository, Mockito.atLeastOnce()).save(tag1);
		Mockito.verify(tagRepository, Mockito.atLeastOnce()).save(tag2);
	}

	@Test
	void removeTagFromPost() {
		post = postService.addTagToPost(post, tag1);
		post = postService.addTagToPost(post, tag2);

		Assertions.assertTrue(post.getTags().contains(tag1));
		Assertions.assertTrue(post.getTags().contains(tag2));
		
		postService.removeTagFromPost(post, tag1);
		
		Assertions.assertEquals(tag1.getPosts(), new ArrayList<>());
		Assertions.assertFalse(post.getTags().contains(tag1));
		Assertions.assertTrue(post.getTags().contains(tag2));
		Assertions.assertEquals(1, post.getTags().size());
		
		postService.removeTagFromPost(post, tag2);

        Assertions.assertEquals(tag1.getPosts(), new ArrayList<>());
		Assertions.assertEquals(tag2.getPosts(), new ArrayList<>());
		Assertions.assertEquals(post.getTags(), new ArrayList<>());
		Assertions.assertEquals(0, post.getProductsInPost().size());

		Mockito.verify(postRepository, Mockito.atLeastOnce()).save(post);
        Mockito.verify(tagRepository, Mockito.atLeastOnce()).save(tag1);
		Mockito.verify(tagRepository, Mockito.atLeastOnce()).save(tag2);
	
	}

}