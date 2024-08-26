package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.SubscriptionDTO;
import com.marketplace.demo.domain.Subscription;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class SubscriptionDTOConverter implements DTOConverter<SubscriptionDTO, Subscription> {

    private final UserService userService;

    @Override
    public SubscriptionDTO toDTO(Subscription subscription) {

        Long userId = Optional.ofNullable(subscription.getUser()).map(User::getID).orElse(null);
        Long subId = Optional.ofNullable(subscription.getSubscriber()).map(User::getID).orElse(null);

        return new SubscriptionDTO(subscription.getID(), subscription.getTimestamp(), subId, userId);
    }

    @Override
    public Subscription toEntity(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = new Subscription();
        subscription.setId(subscriptionDTO.id());
        subscription.setTimestamp(subscriptionDTO.timestamp());

        User user = userService.readById(subscriptionDTO.user()).orElse(null);
        User sub = userService.readById(subscriptionDTO.subscriber()).orElse(null);

        subscription.setUser(user);
        subscription.setSubscriber(sub);

        return subscription;
    }
}
