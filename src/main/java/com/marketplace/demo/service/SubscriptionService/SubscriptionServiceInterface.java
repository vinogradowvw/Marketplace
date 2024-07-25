package com.marketplace.demo.service.SubscriptionService;

import com.marketplace.demo.domain.Subscription;
import com.marketplace.demo.service.CrudService;

import java.util.List;

public interface SubscriptionServiceInterface extends CrudService<Subscription, Long> {
    public List<Subscription> findByUserId(Long userId);

    public List<Subscription> findBySubscriberId(Long subscriberId);

    public Subscription findByUserIdAndSubscriberId(Long userId, Long subscriberId);

    public boolean existsByUserIdAndSubscriberId(Long userId, Long subscriberId);
}
