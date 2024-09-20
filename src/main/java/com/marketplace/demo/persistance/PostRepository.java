package com.marketplace.demo.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.demo.domain.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT AVG(r.rating) FROM Post p JOIN p.reviews r WHERE p.id = :id")
    Double getAVGRatingById(@Param("id") Long id);

    @Query("SELECT COUNT(r) > 0 FROM Post p JOIN p.reviews r WHERE r.author.username = :username AND p.id = :postId")
    boolean existsPostWithReviewByUsername(String username, Long postId);
}