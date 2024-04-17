package com.marketplace.demo.service.PostService;


import com.marketplace.demo.service.CrudServiceImpl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.Image;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.domain.Tag;
import com.marketplace.demo.persistance.ImageRepository;
import com.marketplace.demo.persistance.PostRepository;
import com.marketplace.demo.persistance.ProductRepository;
import com.marketplace.demo.persistance.TagRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PostService extends CrudServiceImpl<Post, Long> implements PostServiceInterface {

    private PostRepository postRepository;
    private ProductRepository productRepository;
    private ImageRepository imageRepository;
    private TagRepository tagRepository;


    @Override
    public Post addImageToPost(Post post, Image image) throws IllegalArgumentException {
        
        if (postRepository.existsById(post.getID())) {

            if (imageRepository.existsById(image.getID())) {
                image.setPost(post);
                imageRepository.save(image);
                post.getImages().add(image);
                return postRepository.save(post);
            }

            throw new IllegalArgumentException("Image does not exist");

        }

        throw new IllegalArgumentException("Post does not exist");
    }

    @Override
    public Post removeImageFromPost(Post post, Image image) throws IllegalArgumentException {
        if (postRepository.existsById(post.getID())) {

            if (imageRepository.existsById(image.getID())) {
                post.getImages().remove(image);
                image.setPost(null);
                imageRepository.save(image);
                return postRepository.save(post);
            }
            
            throw new IllegalArgumentException("Image does not exist");
        
        }

        throw new IllegalArgumentException("Post does not exist");
    }

    @Override
    public Post addProductToPost(Post post, Product product) throws IllegalArgumentException {
        
        if (postRepository.existsById(post.getID())) {

            if (productRepository.existsById(product.getID())) {
                product.setPost(post);
                productRepository.save(product);
                post.getProductsInPost().add(product);
                return postRepository.save(post);
            }
            
            throw new IllegalArgumentException("Product with ID " + product.getID() + " does not exists");

        }

        throw new IllegalArgumentException("Post with this ID does not exists");
    }

    @Override
    public Post removeProductFromPost(Post post, Product product) throws IllegalArgumentException {
        
        if (postRepository.existsById(post.getID())) {

            if (productRepository.existsById(product.getID())) {
                product.setPost(null);
                post.getProductsInPost().remove(product);
                productRepository.save(product);
                return postRepository.save(post);
            }

            throw new IllegalArgumentException("Product with ID " + product.getID() + " does not exists");
        
        }

        throw new IllegalArgumentException("Post with this ID does not exists");
    }
    
    @Override
    public Post addTagToPost(Post post, Tag tag) throws IllegalArgumentException {

        if (postRepository.existsById(post.getID())) {

            if (tagRepository.existsById(tag.getID())) {
                tag.getPosts().add(post);
                tagRepository.save(tag);
                post.getTags().add(tag);
                return postRepository.save(post);
            }

            throw new IllegalArgumentException("Tag with ID " + tag.getID() + " does not exists");
        
        }
        throw new IllegalArgumentException("Post with ID " + post.getID() + " does not exists");
    }

    public Post removeTagFromPost(Post post, Tag tag) throws IllegalArgumentException {
        
        if (postRepository.existsById(post.getID())) {

            if (tagRepository.existsById(tag.getID())) {
                post.getTags().remove(tag);
                tag.getPosts().remove(post);
                tagRepository.save(tag);
                return postRepository.save(post);
            }

            throw new IllegalArgumentException("Tag with ID " + tag.getID() + " does not exists");
        
        }
        
        throw new IllegalArgumentException("Post with ID " + post.getID() + " does not exists");

    }


    @Override
    protected CrudRepository<Post, Long> getRepository() {
        return postRepository;
    }
}
