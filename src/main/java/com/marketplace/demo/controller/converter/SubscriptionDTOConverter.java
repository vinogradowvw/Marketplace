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
        Optional<User> optUser = Optional.ofNullable(subscription.getUser());
        Optional<User> optSubscriber = Optional.ofNullable(subscription.getSubscriber());

        if (optUser.isPresent() && optSubscriber.isPresent()) {
            return new SubscriptionDTO(subscription.getID(), subscription.getTimestamp(), optSubscriber.get().getID(), optUser.get().getID());
        }
        else if (optUser.isPresent()) {
            return new SubscriptionDTO(subscription.getID(), subscription.getTimestamp(), null, optUser.get().getID());
        }
        else if (optSubscriber.isPresent()) {
            return new SubscriptionDTO(subscription.getID(), subscription.getTimestamp(), optSubscriber.get().getID(), null);
        }
        else{
            return new SubscriptionDTO(subscription.getID(), subscription.getTimestamp(), null, null);
        }
    }

    @Override
    public Subscription toEntity(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = new Subscription();
        subscription.setId(subscriptionDTO.id());
        subscription.setTimestamp(subscriptionDTO.timestamp());

        Optional<Long> optUserId = Optional.ofNullable(subscriptionDTO.user());
        Optional<Long> optSubscriberId = Optional.ofNullable(subscriptionDTO.subscriber());

        Optional<User> user = Optional.empty();
        Optional<User> subscriber = Optional.empty();

        if (optUserId.isPresent()){
            user = userService.readById(optUserId.get());
        }

        if (optSubscriberId.isPresent()){
            subscriber = userService.readById(optSubscriberId.get());
        }

        if (user.isPresent() && subscriber.isPresent()){
            subscription.setUser(user.get());
            subscription.setSubscriber(subscriber.get());
        }
        else if (subscriber.isPresent()){
            subscription.setSubscriber(subscriber.get());
            subscription.setUser(null);
        }
        else if (user.isPresent()){
            subscription.setUser(user.get());
            subscription.setSubscriber(null);
        }
        else{
            subscription.setSubscriber(null);
            subscription.setUser(null);
        }

        return subscription;
    }
}
