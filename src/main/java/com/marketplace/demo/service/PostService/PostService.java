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
    public void addImageToPost(Long postId, Long imageId) throws IllegalArgumentException {
        if (postRepository.findById(postId).isPresent()) {
            Post post = postRepository.findById(postId).get();

            if (imageRepository.findById(imageId).isPresent()) {
                Image image = imageRepository.findById(imageId).get();

                post.getImages().add(image);
                postRepository.save(post);
                image.setPost(post);
                imageRepository.save(image);
            }
            else{
                throw new IllegalArgumentException("Image does not exist");
            }

            return;
        }

        throw new IllegalArgumentException("Post does not exist");
    }

    @Override
    public void removeImageFromPost(Long postId, Long imageId) throws IllegalArgumentException {
        if (postRepository.findById(postId).isPresent()) {
            Post post = postRepository.findById(postId).get();

            if (imageRepository.findById(imageId).isPresent()) {
                Image image = imageRepository.findById(imageId).get();

                post.getImages().remove(image);
                image.setPost(null);
                imageRepository.save(image);
                postRepository.save(post);
            }
            else{
                throw new IllegalArgumentException("Image does not exist");
            }

            return;
        }

        throw new IllegalArgumentException("Post does not exist");
    }

    @Override
    public void addProductToPost(Long postId, Long productId) throws IllegalArgumentException {
        if (postRepository.existsById(postId)) {
            Post post = postRepository.findById(postId).get();

            if (!productRepository.existsById(productId)) {
                throw new IllegalArgumentException("Product with ID " + productId +" does not exists");
            }
            else {
                Product product = productRepository.findById(productId).get();

                product.setPost(post);
                post.getProductsInPost().add(product);
                productRepository.save(product);
                postRepository.save(post);
            }
            return;
        }
        throw new IllegalArgumentException("Post with this ID does not exists");
    }

    @Override
    public void removeProductFromPost(Long postId, Long productId) throws IllegalArgumentException {
        if (postRepository.existsById(postId)) {
            Post post = postRepository.findById(postId).get();

            if (!productRepository.existsById(productId)) {
                throw new IllegalArgumentException("Product with ID " + productId +" does not exists");
            }
            else{
                Product product = productRepository.findById(productId).get();

                product.setPost(null);
                post.getProductsInPost().remove(product);
                productRepository.save(product);
                postRepository.save(post);
            }
            return;
        }
        throw new IllegalArgumentException("Post with this ID does not exists");
    }

    @Override
    protected CrudRepository<Post, Long> getRepository() {
        return postRepository;
    }
}
