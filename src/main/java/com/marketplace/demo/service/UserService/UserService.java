package com.marketplace.demo.service.UserService;

import com.marketplace.demo.domain.Subscription;
import com.marketplace.demo.persistance.SubscriptionRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.marketplace.demo.domain.User;
import com.marketplace.demo.persistance.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService extends CrudServiceImpl<User, Long> implements UserServiceInterface {

    private UserRepository userRepository;
    private SubscriptionRepository subscriptionRepository;

    @Override
    protected CrudRepository<User, Long> getRepository() {
        return userRepository;
    }


    @Override
    public User addSubcriberToUser(User user, User subscriber, Subscription subscription) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (subscriptionRepository.existsById(subscription.getID())) {
                if (userRepository.existsById(subscriber.getID())) {
                    subscription.setUser(user);
                    subscription.setSubscriber(subscriber);
                    user.getSubscribers().add(subscriber);
                    post.getTags().remove(tag);
                    tag.getPosts().remove(post);
                    tagRepository.save(tag);
                    return postRepository.save(post);
                }
            }

            throw new IllegalArgumentException("Tag with ID " + tag.getID() + " does not exists");

        }

        throw new IllegalArgumentException("Post with ID " + post.getID() + " does not exists");
    }

    @Override
    public User removeSubcriberToUser(User user, Subscription subscription) throws IllegalArgumentException {
        return null;
    }

    @Override
    public User addSubcribeToUser(User user, Subscription subscription) throws IllegalArgumentException {
        return null;
    }

    @Override
    public User removeSubcribeToUser(User user, Subscription subscription) throws IllegalArgumentException {
        return null;
    }
}

