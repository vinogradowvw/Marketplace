package com.marketplace.demo.service.PostService;

import com.marketplace.demo.domain.*;
import com.marketplace.demo.service.CrudService;

public interface PostServiceInterface extends CrudService<Post, Long> {
    
    public Post addImageToPost(Post post, Image image) throws IllegalArgumentException;
    public Post removeImageFromPost(Post post, Image image) throws IllegalArgumentException;

    public Post addProductToPost(Post post, Product product) throws IllegalArgumentException;
    public Post removeProductFromPost(Post post, Product product) throws IllegalArgumentException;
    
    public Post addTagToPost(Post post, Tag tag) throws IllegalArgumentException;
    public Post removeTagFromPost(Post post, Tag tag) throws IllegalArgumentException;

    public Post addUserToPost(Post post, User user) throws IllegalArgumentException;
    public Post removeUserFromPost(Post post, User user) throws IllegalArgumentException;

    public Post likePost(Post post, User user) throws IllegalArgumentException;

}
