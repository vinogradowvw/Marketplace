package com.marketplace.demo.controller;

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.ReviewDTO;
import com.marketplace.demo.domain.Review;
import com.marketplace.demo.service.PostService.PostService;
import com.marketplace.demo.service.ReviewService.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/review", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ReviewController {

    private ReviewService reviewService;
    private DTOConverter<ReviewDTO, Review> dtoConverter;
    private PostService postService;

    @GetMapping
    public List<ReviewDTO> getAllReviews() {

        List<ReviewDTO> reviews = new ArrayList<>();
        reviewService.readAll().forEach(r -> reviews.add(dtoConverter.toDTO(r)));

        return reviews;
    }

    @GetMapping(path = "/{id}")
    public ReviewDTO getReviewById(@PathVariable("id") Long id) {
        return dtoConverter.toDTO(reviewService.readById(id).get());
    }

    @PostMapping
    public ReviewDTO createReview(@RequestBody ReviewDTO reviewDTO) {
        Review review = reviewService.create(dtoConverter.toEntity(reviewDTO));

        return dtoConverter.toDTO(review);
    }

    @PutMapping(path = "/{id}")
    public ReviewDTO updateReview(@PathVariable("id") Long id, @RequestBody ReviewDTO reviewDTO) {
        Review oldReview = reviewService.readById(id).get();

        oldReview.setDescription(reviewDTO.description());
        oldReview.setRating(reviewDTO.rating());
        oldReview.setTitle(reviewDTO.title());

        reviewService.update(id, oldReview);

        return dtoConverter.toDTO(oldReview);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteReview(@PathVariable("id") Long id) {
        Review review = reviewService.readById(id).get();

        postService.deleteReviewFromPost(review.getPost(), review, review.getAuthor());

        reviewService.deleteById(id);
    }
}
