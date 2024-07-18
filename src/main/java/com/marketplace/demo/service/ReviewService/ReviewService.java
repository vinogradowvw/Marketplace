package com.marketplace.demo.service.ReviewService;

import com.marketplace.demo.domain.Review;
import com.marketplace.demo.persistance.ReviewRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ReviewService extends CrudServiceImpl<Review, Long> implements ReviewServiceInterface{

    private ReviewRepository reviewRepository;

    @Override
    protected CrudRepository<Review, Long> getRepository() {
        return reviewRepository;
    }
}
