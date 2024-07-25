package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.PostDTO;
import com.marketplace.demo.domain.*;
import com.marketplace.demo.service.ImageService.ImageService;
import com.marketplace.demo.service.ProductService.ProductService;
import com.marketplace.demo.service.ReviewService.ReviewService;
import com.marketplace.demo.service.TagService.TagService;
import com.marketplace.demo.service.UserService.UserService;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostDTOConverter implements DTOConverter<PostDTO, Post> {

    private final UserService userService;
    private final TagService tagService;
    private final ImageService imageService;
    private final ProductService productService;
    private final ReviewService reviewService;


    @Override
    public PostDTO toDTO(Post post) {

        Long productId = Optional.ofNullable(post.getProduct()).map(Product::getID).orElse(null);

        Optional<List<User>> optLikedUsers = Optional.ofNullable(post.getLikedUsers());
        ArrayList<Long> likedUserIds = new ArrayList<>();
        if (optLikedUsers.isPresent()) {
            for (User likedUser : optLikedUsers.get()) {
                likedUserIds.add(likedUser.getID());
            }
        }

        Optional<List<Tag>> optTags = Optional.ofNullable(post.getTags());
        ArrayList<Long> tagIds = new ArrayList<>();
        if (optTags.isPresent()) {
            for (Tag tag : optTags.get()) {
                tagIds.add(tag.getID());
            }
        }

        Long userId = Optional.ofNullable(post.getUser()).map(User::getID).orElse(null);

        Optional<List<Image>> optImages = Optional.ofNullable(post.getImages());
        ArrayList<Long> imageIds = new ArrayList<>();
        if (optImages.isPresent()) {
            for (Image image : optImages.get()){
                imageIds.add(image.getID());
            }
        }

        Optional<List<Review>> reviewsOpt = Optional.ofNullable(post.getReviews());
        ArrayList<Long> reviewIds = new ArrayList<>();
        if (reviewsOpt.isPresent()) {
            for (Review review : reviewsOpt.get()){
                reviewIds.add(review.getID());
            }
        }

        return new PostDTO(post.getID(), post.getName(), post.getDescription(), 
                            productId, userId, likedUserIds, tagIds, imageIds, reviewIds);
    }

    @Override
    public Post toEntity(PostDTO postDTO) {
        
        Post post = new Post();
        
        post.setId(postDTO.id());
        post.setName(postDTO.name());
        post.setDescription(postDTO.description());

        Product product = productService.readById(postDTO.product()).orElse(null);
        post.setProduct(product);

        User user = userService.readById(postDTO.user()).orElse(null);
        post.setUser(user);

        List<User> likedUsers = new ArrayList<>();
        for (Long userId : postDTO.likedUsers()) {
            User likedUser = userService.readById(userId).orElse(null);

            likedUsers.add(likedUser);
        }
        post.setLikedUsers(likedUsers);


        List<Tag> tags = new ArrayList<>();
        for (Long tagId : postDTO.tags()) {
            Tag tag = tagService.readById(tagId).orElse(null);

            tags.add(tag);
        }
        post.setTags(tags);


        List<Image> images = new ArrayList<>();
        for (Long imageId : postDTO.images()) {
            Image image = imageService.readById(imageId).orElse(null);

            images.add(image);
        }
        post.setImages(images);

        List<Review> reviews = new ArrayList<>();
        for (Long reviewId : postDTO.reviews()) {
            Review review = reviewService.readById(reviewId).orElse(null);

            reviews.add(review);
        }
        post.setReviews(reviews);

        return post;
    }
}
