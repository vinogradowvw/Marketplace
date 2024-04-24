package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.UserDTO;
import com.marketplace.demo.domain.Payment;
import com.marketplace.demo.domain.Post;
import com.marketplace.demo.domain.Role;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.persistance.PaymentRepository;
import com.marketplace.demo.persistance.PostRepository;
import com.marketplace.demo.persistance.RoleRepository;
import com.marketplace.demo.persistance.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserDTOConverter implements DTOConverter<UserDTO, User> {

    private final PostRepository postRepository;
    private final PaymentRepository paymentRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public UserDTO toDTO(User user) {
        List<Long> postIds = new ArrayList<>();
        List<Long> paymentIds = new ArrayList<>();
        List<Long> likeIds = new ArrayList<>();
        List<Long> subscribeIds = new ArrayList<>();
        List<Long> subscriptionIds = new ArrayList<>();
        Long roleId = null;

        Optional<List<Post>> likes = Optional.ofNullable(user.getLikes());
        Optional<List<Post>> posts = Optional.ofNullable(user.getPosts());
        Optional<List<Payment>> payments = Optional.ofNullable(user.getPayments());
        Optional<List<User>> subscribers = Optional.ofNullable(user.getSubscribers());
        Optional<List<User>> subscriptions = Optional.ofNullable(user.getSubscriptions());
        Optional<Role> optRole = Optional.ofNullable(user.getRole());

        if (likes.isPresent()){
            for (Post post : likes.get()) {
                likeIds.add(post.getID());
            }
        }

        if (posts.isPresent()){
            for (Post post : posts.get()) {
                postIds.add(post.getID());
            }
        }

        if (payments.isPresent()){
            for (Payment payment : payments.get()) {
                paymentIds.add(payment.getID());
            }
        }

        if (subscribers.isPresent()){
            for (User userSubscriber : subscribers.get()) {
                subscribeIds.add(userSubscriber.getID());
            }
        }

        if (subscriptions.isPresent()){
            for (User userSubscription : subscriptions.get()) {
                subscriptionIds.add(userSubscription.getID());
            }
        }

        if (optRole.isPresent()){
            roleId = optRole.get().getID();
        }


        return new UserDTO(user.getID(), user.getUsername(), user.getPassword(),
                user.getEmail(), likeIds, paymentIds, postIds,
                subscribeIds, subscriptionIds, roleId);
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        User user = new User();

        user.setId(userDTO.id());
        user.setUsername(userDTO.username());
        user.setPassword(userDTO.password());
        user.setEmail(userDTO.email());

        Optional<List<Long>> likeIds = Optional.ofNullable(userDTO.likes());
        Optional<List<Long>> postIds = Optional.ofNullable(userDTO.posts());
        Optional<List<Long>> paymentIds = Optional.ofNullable(userDTO.payments());
        Optional<List<Long>> subscribeIds = Optional.ofNullable(userDTO.subscribers());
        Optional<List<Long>> subscriptionIds = Optional.ofNullable(userDTO.subscriptions());
        Optional<Long> optRole = Optional.ofNullable(userDTO.roleId());

        List<Post> posts = new ArrayList<>();
        List<Payment> payments = new ArrayList<>();
        List<Post> likes = new ArrayList<>();
        List<User> subscribers = new ArrayList<>();
        List<User> subscriptions = new ArrayList<>();
        Role role = null;

        if (likeIds.isPresent()){
            for (Long likeId : likeIds.get()) {
                if (postRepository.existsById(likeId)) {
                    likes.add(postRepository.findById(likeId).get());
                }
            }
        }

        if (postIds.isPresent()){
            for (Long postId : postIds.get()) {
                if (postRepository.existsById(postId)) {
                    posts.add(postRepository.findById(postId).get());
                }
            }
        }

        if (paymentIds.isPresent()){
            for (Long paymentId : paymentIds.get()) {
                if (paymentRepository.existsById(paymentId)) {
                    payments.add(paymentRepository.findById(paymentId).get());
                }
            }
        }

        if (subscribeIds.isPresent()){
            for (Long subscribeId : subscribeIds.get()) {
                if (userRepository.existsById(subscribeId)){
                    subscribers.add(userRepository.findById(subscribeId).get());
                }
            }
        }

        if (subscriptionIds.isPresent()){
            for (Long subscriptionId : subscriptionIds.get()) {
                if (userRepository.existsById(subscriptionId)){
                    subscriptions.add(userRepository.findById(subscriptionId).get());
                }
            }
        }

        if (optRole.isPresent()){
            if (roleRepository.existsById(optRole.get())){
                role = roleRepository.findById(optRole.get()).get();
            }
        }

        user.setRole(role);
        user.setLikes(likes);
        user.setPayments(payments);
        user.setPosts(posts);
        user.setSubscribers(subscribers);
        user.setSubscriptions(subscriptions);

        return user;
    }
}
