package com.marketplace.demo.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.demo.domain.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

}