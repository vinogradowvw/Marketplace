package com.marketplace.demo.service.PostService;

import com.marketplace.demo.domain.Post;

import com.marketplace.demo.service.CrudService;
import jakarta.persistence.EntityNotFoundException;

public interface PostServiceInterface extends CrudService<Post, Long> {
    public void addImageToPost(Long postId, Long imageId) throws IllegalArgumentException;
    public void removeImageFromPost(Long postId, Long imageId) throws IllegalArgumentException;

    public void addProductToPost(Long postId, Long productId) throws IllegalArgumentException;
    public void removeProductFromPost(Long postId, Long productId) throws IllegalArgumentException;
}
