package com.marketplace.demo.service.PostService;

import com.marketplace.demo.domain.Image;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.domain.Tag;
import com.marketplace.demo.service.CrudService;

public interface PostServiceInterface extends CrudService<Post, Long> {
    
    public Post addImageToPost(Post post, Image image) throws IllegalArgumentException;
    
    public Post removeImageFromPost(Post post, Image image) throws IllegalArgumentException;

    public Post addProductToPost(Post post, Product product) throws IllegalArgumentException;
    
    public Post removeProductFromPost(Post post, Product product) throws IllegalArgumentException;
    
    public Post addTagToPost(Post post, Tag tag) throws IllegalArgumentException;

    public Post removeTagFromPost(Post post, Tag tag) throws IllegalArgumentException;

}
