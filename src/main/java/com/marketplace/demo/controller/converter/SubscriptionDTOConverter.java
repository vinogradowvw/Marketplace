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

        User user = null;
        User sub = null;

        Optional<Long> userId = Optional.ofNullable(subscriptionDTO.user());
        if (userId.isPresent()) {
            user = userService.readById(userId.get()).orElse(null);
        }

        Optional<Long> subId = Optional.ofNullable(subscriptionDTO.subscriber());
        if (subId.isPresent()) {
            sub = userService.readById(subId.get()).orElse(null);
        }

        subscription.setUser(user);
        subscription.setSubscriber(sub);

        return subscription;
    }
}
