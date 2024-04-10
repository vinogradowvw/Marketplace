package com.marketplace.demo.service.LikeService;

import com.marketplace.demo.domain.Likes;
import com.marketplace.demo.service.CrudService;
import jakarta.persistence.EntityNotFoundException;

public interface LikeServiceInterface extends CrudService<Likes, Long> {
}
