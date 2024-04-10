package com.marketplace.demo.service.PostService;

import com.marketplace.demo.domain.Image;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Product;

import jakarta.persistence.EntityNotFoundException;

public interface PostServiceInterface {

    public Post getPostById(Long id) throws EntityNotFoundException;

    public Post createPost(Post post) throws IllegalArgumentException;

    public Post updatePost(Post post);

    public void deletePost(Post post);

    public Post addProductToPost(Post post, Product products) throws IllegalArgumentException; 

    public Post addPostImage(Post post, Image image) throws IllegalArgumentException;
}
