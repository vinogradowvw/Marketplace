package com.marketplace.demo.service.SubscriptionService;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.Subscription;
import com.marketplace.demo.persistance.SubscriptionRepository;
import com.marketplace.demo.service.CrudServiceImpl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Service
@Transactional
@AllArgsConstructor
public class SubscriptionService extends CrudServiceImpl<Subscription, Long> implements SubscriptionServiceInterface {
    
    private SubscriptionRepository subscriptionRepository;
    
    @Override
    protected CrudRepository<Subscription, Long> getRepository() {
        return subscriptionRepository;
    }

}
