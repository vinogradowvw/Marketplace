package com.marketplace.demo.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.demo.domain.Subscription;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findBySubscriberId(Long subscriberId);

    List<Subscription> findByUserId(Long userId);

    Subscription findByUserIdAndSubscriberId(Long userId, Long subscriberId);
}