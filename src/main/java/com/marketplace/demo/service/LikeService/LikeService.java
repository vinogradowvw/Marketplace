package com.marketplace.demo.service.LikeService;

import com.marketplace.demo.domain.Likes;
import com.marketplace.demo.persistance.LikesRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class LikeService extends CrudServiceImpl<Likes, Long> implements LikeServiceInterface{
    private LikesRepository likesRepository;

    @Override
    protected CrudRepository<Likes, Long> getRepository() {
        return likesRepository;
    }
}
