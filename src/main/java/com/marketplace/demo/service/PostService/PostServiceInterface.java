package com.marketplace.demo.service.PostService;

import com.marketplace.demo.domain.Post;

import jakarta.persistence.EntityNotFoundException;

public interface PostServiceInterface {

    public Post getPostById(Long id) throws EntityNotFoundException;

    public Post createPost(Post post) throws IllegalArgumentException;

    public Post updatePost(Post post);

    public void deletePost(Post post);

}
