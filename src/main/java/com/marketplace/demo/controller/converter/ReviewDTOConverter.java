package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.ReviewDTO;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Review;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.service.PostService.PostService;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ReviewDTOConverter implements DTOConverter<ReviewDTO, Review> {

    private final UserService userService;
    private final PostService postService;

    @Override
    public ReviewDTO toDTO(Review review) {

        Long userId = Optional.ofNullable(review.getAuthor()).map(User::getID).orElse(null);
        Long postId = Optional.ofNullable(review.getPost()).map(Post::getID).orElse(null);

        return new ReviewDTO(review.getID(), review.getTitle(), review.getDescription(), review.getRating(), userId, postId);
    }

    @Override
    public Review toEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setId(reviewDTO.id());
        review.setTitle(reviewDTO.title());
        review.setDescription(reviewDTO.description());
        review.setRating(reviewDTO.rating());

        User user = null;
        Post post = null;

        Optional<Long> userId = Optional.ofNullable(reviewDTO.author());
        if (userId.isPresent()) {
            user = userService.readById(userId.get()).orElse(null);
        }

        Optional<Long> postId = Optional.ofNullable(reviewDTO.post());
        if (postId.isPresent()) {
            post = postService.readById(postId.get()).orElse(null);
        }

        review.setAuthor(user);
        review.setPost(post);

        return review;
    }
}
