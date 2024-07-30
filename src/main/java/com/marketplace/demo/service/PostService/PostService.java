package com.marketplace.demo.service.PostService;


import com.marketplace.demo.domain.*;
import com.marketplace.demo.persistance.*;
import com.marketplace.demo.service.CrudServiceImpl;
import com.marketplace.demo.service.ReviewService.ReviewService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class PostService extends CrudServiceImpl<Post, Long> implements PostServiceInterface {

    private ReviewRepository reviewRepository;
    private PostRepository postRepository;
    private ProductRepository productRepository;
    private ImageRepository imageRepository;
    private TagRepository tagRepository;
    private UserRepository userRepository;
    private ReviewService reviewService;

    private boolean checkIfUserBoughtProduct(User user, Product product) {
        boolean flag = false;

        for(var order : user.getOrders()){
            if (order.getProducts().containsKey(product)) {
                flag = true;
                break;
            }
        }

        return flag;
    }

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
                post.setProduct(product);
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
                post.setProduct(null);
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
    public Post addUserToPost(Post post, User user) throws IllegalArgumentException {
        if (postRepository.existsById(post.getID())) {

            if (userRepository.existsById(user.getID())) {
                user.getPosts().add(post);
                userRepository.save(user);
                post.setUser(user);
                return postRepository.save(post);
            }

            throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");

        }
        throw new IllegalArgumentException("Post with ID " + post.getID() + " does not exists");
    }

    @Override
    public Post removeUserFromPost(Post post, User user) throws IllegalArgumentException {
        if (postRepository.existsById(post.getID())) {

            if (userRepository.existsById(user.getID())) {
                post.setUser(null);
                user.getPosts().remove(post);
                userRepository.save(user);
                return postRepository.save(post);
            }

            throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");

        }

        throw new IllegalArgumentException("Post with ID " + post.getID() + " does not exists");
    }

    @Override
    public Post likePost(Post post, User user) throws IllegalArgumentException {
        if (postRepository.existsById(post.getID())) {

            if (userRepository.existsById(user.getID())) {
                if (post.getLikedUsers().contains(user)){
                    user.getLikes().remove(post);
                    post.getLikedUsers().remove(user);
                }
                else{
                    user.getLikes().add(post);
                    post.getLikedUsers().add(user);
                }
                userRepository.save(user);
                return postRepository.save(post);
            }

            throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");

        }
        throw new IllegalArgumentException("Post with ID " + post.getID() + " does not exists");
    }

    @Override
    public Post addReviewToPost(Post post, Review review, User user) throws IllegalArgumentException {

        if (!userRepository.existsById(user.getID())){
            throw new IllegalArgumentException("There is not user with id: " + user.getID());
        }

        if (!postRepository.existsById(post.getID())){
            throw new IllegalArgumentException("There is not post with id: " + post.getID());
        }

        if (!reviewRepository.existsById(review.getID())){
            throw new IllegalArgumentException("There is not review with id: " + review.getID());
        }

        Optional<Product> productOpt = Optional.ofNullable(post.getProduct());
        if (productOpt.isEmpty()){
            throw new IllegalArgumentException("There is no product in the post with id " + post.getID());
        }

        if (!productRepository.existsById(productOpt.get().getID())){
            throw new IllegalArgumentException("There is not product with id: " + productOpt.get().getID());
        }


        if(!checkIfUserBoughtProduct(user, post.getProduct())){
            throw new IllegalArgumentException("User didn't buy a product to write a review.");
        }

        review.setAuthor(user);
        review.setPost(post);
        user.getReviews().add(review);
        post.getReviews().add(review);

        reviewRepository.save(review);
        userRepository.save(user);

        return postRepository.save(post);
    }

    public Post deleteReviewFromPost(Post post, Review review, User user) throws IllegalArgumentException{
        if (!userRepository.existsById(user.getID())){
            throw new IllegalArgumentException("There is not user with id: " + user.getID());
        }

        if (!postRepository.existsById(post.getID())){
            throw new IllegalArgumentException("There is not post with id: " + post.getID());
        }

        if (!reviewRepository.existsById(review.getID())){
            throw new IllegalArgumentException("There is not review with id: " + review.getID());
        }

        if (!user.getReviews().contains(review)){
            throw new IllegalArgumentException("User didn't write this review.");
        }

        if (!post.getReviews().contains(review)){
            throw new IllegalArgumentException("Post doesn't contain this review.");
        }

        user.getReviews().remove(review);
        post.getReviews().remove(review);

        reviewService.deleteById(review.getID());
        userRepository.save(user);

        return postRepository.save(post);
    }

    public double getAVGPostRating(Post post) throws IllegalArgumentException{
        if (!postRepository.existsById(post.getID())){
            throw new IllegalArgumentException("There is not post with id: " + post.getID());
        }

        return postRepository.getAVGRatingById(post.getID());
    }

    public Long getPostViews(Post post) throws IllegalArgumentException{
        if (!postRepository.existsById(post.getID())){
            throw new IllegalArgumentException("There is not post with id: " + post.getID());
        }

        return post.getViews();
    }

    @Override
    protected CrudRepository<Post, Long> getRepository() {
        return postRepository;
    }
}
