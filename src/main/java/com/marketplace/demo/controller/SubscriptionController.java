package com.marketplace.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.demo.domain.Subscription;
import com.marketplace.demo.controller.converter.SubscriptionDTOConverter;
import com.marketplace.demo.controller.dto.SubscriptionDTO;
import com.marketplace.demo.service.SubscriptionService.SubscriptionService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/subscription", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubscriptionController {

    private SubscriptionDTOConverter subscriptionConverter;
    private SubscriptionService subscriptionService;

    @GetMapping(path = "/user/{id}")//ok
    public List<SubscriptionDTO> getAllSubscriptionsForUser(@PathVariable("id") Long id) {
        List<SubscriptionDTO> subscriptionDTOs = new ArrayList<>();
        List<Subscription> subscriptions = subscriptionService.findByUserId(id);

        for (Subscription subscription : subscriptions) {
            subscriptionDTOs.add(subscriptionConverter.toDTO(subscription));
        }
        
        return subscriptionDTOs;
    }

    @GetMapping(path = "/subscriber/{id}")
    public List<SubscriptionDTO> getAllSubscriptionsForSubscriber(@PathVariable("id") Long id) {
        List<SubscriptionDTO> subscriptionDTOs = new ArrayList<>();
        List<Subscription> subscriptions = subscriptionService.findBySubscriberId(id);

        for (Subscription subscription : subscriptions) {
            subscriptionDTOs.add(subscriptionConverter.toDTO(subscription));
        }
        
        return subscriptionDTOs;
    }

}
