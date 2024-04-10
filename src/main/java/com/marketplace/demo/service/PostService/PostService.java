package com.marketplace.demo.service.PostService;


import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.Image;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.persistance.ImageRepository;
import com.marketplace.demo.persistance.PostRepository;
import com.marketplace.demo.persistance.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PostService implements PostServiceInterface {

    private PostRepository postRepository;
    private ProductRepository productRepository;
    private ImageRepository imageRepository;


    @Override
    public Post getPostById(Long id) throws EntityNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No post with id " + id + " found."));
    }


    @Override
    public Post createPost(Post post) throws IllegalArgumentException {
        if (post.getDescription().length() <= 300) {
            return postRepository.save(post);
        }

        throw new IllegalArgumentException("Description is too long must be less than 300 characters");
    }


    @Override
    public Post updatePost(Post post) {

        if (postRepository.existsById(post.getID())) {
            
            if (post.getDescription().length() <= 300) {
                return postRepository.save(post);
            }

            throw new IllegalArgumentException("Description is too long must be less than 300 characters");

        }
        
        throw new IllegalArgumentException("Post with this ID does not exists");
    
    }


    @Override
    public void deletePost(Post post) {
        if (postRepository.existsById(post.getID())) {
            postRepository.delete(post);
        }

        throw new IllegalArgumentException("Post with this ID does not exists");
    }



    @Override
    public Post addProductToPost(Post post, Product product) throws IllegalArgumentException {

        if (postRepository.existsById(post.getID())) {

            if (productRepository.existsById(product.getID())) {
                product.getPostsWithProduct().add(post);
            }
            else {
                throw new IllegalArgumentException("Product with ID " + product.getID() +" does not exists");
            }

            post.getProductsInPost().add(product);

            productRepository.save(product);

            return postRepository.save(post);
            
        }

        throw new IllegalArgumentException("Post with this ID does not exists");

    }


    @Override
    public Post addPostImage(Post post, Image image) {
        if (postRepository.existsById(post.getID())) {
            
            if (imageRepository.existsById(image.getID())) {
                image.getPostsWithImg().add(post);
            }
            else {
                throw new IllegalArgumentException("Image with ID " + image.getID() +" does not exists");
            }

            post.getImages().add(image);

            imageRepository.save(image);

            return postRepository.save(post);
            
        }

        throw new IllegalArgumentException("Post with this ID does not exists");

    }

}
