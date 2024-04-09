package com.marketplace.demo.service.PostService;

import java.util.List;

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


    public Post addProductsToPost(Post post, List<Product> products) {

        if (postRepository.existsById(post.getID())) {
            
            for (Product product:products){
                if (!productRepository.existsById(product.getID())) {
                    throw new IllegalArgumentException("Product with ID " + product.getID() +" does not exists");
                }
                else {
                    product.getPostsWithProduct().add(post);
                }
            }

            post.setProductsInPost(products);

            productRepository.saveAll(products);

            return postRepository.save(post);
            
        }

        throw new IllegalArgumentException("Post with this ID does not exists");

    }


    public Post addPostImages(Post post, List<Image> images) {
        if (postRepository.existsById(post.getID())) {
            
            for (Image image:images){
                if (!imageRepository.existsById(image.getID())) {
                    throw new IllegalArgumentException("Image with ID " + image.getID() +" does not exists");
                }
                else {
                    image.getPostsWithImg().add(post);
                }
            }

            post.setImages(images);

            imageRepository.saveAll(images);

            return postRepository.save(post);
            
        }

        throw new IllegalArgumentException("Post with this ID does not exists");

    }

}
