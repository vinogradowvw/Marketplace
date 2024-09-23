package com.marketplace.demo.service.UserService;

import com.marketplace.demo.domain.*;
import com.marketplace.demo.persistance.*;
import com.marketplace.demo.service.CartService.CartService;
import com.marketplace.demo.service.CrudServiceImpl;
import com.marketplace.demo.service.ImageService.ImageService;
import com.marketplace.demo.service.JWTService;
import com.marketplace.demo.service.SubscriptionService.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService extends CrudServiceImpl<User, Long> implements UserServiceInterface, UserDetailsService {

    private final ReviewRepository reviewRepository;
    private final SubscriptionService subscriptionService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final RoleRepository roleRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    private final TagRepository tagRepository;
    private final CartProductRepository cartProductRepository;
    private final ImageService imageService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTService jwtService;

    @Autowired
    public UserService(ReviewRepository reviewRepository, SubscriptionService subscriptionService, @Value("${bcrypt.rounds}") int rounds,
                       PostRepository postRepository, UserRepository userRepository, SubscriptionRepository subscriptionRepository,
                       RoleRepository roleRepository, CartService cartService, OrderRepository orderRepository, ProductRepository productRepository,
                       OrderProductRepository orderProductRepository, PaymentRepository paymentRepository, CartRepository cartRepository,
                       TagRepository tagRepository, CartProductRepository cartProductRepository, ImageService imageService, JWTService jwtService) {
        this.reviewRepository = reviewRepository;
        this.subscriptionService = subscriptionService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.roleRepository = roleRepository;
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
        this.paymentRepository = paymentRepository;
        this.cartRepository = cartRepository;
        this.tagRepository = tagRepository;
        this.cartProductRepository = cartProductRepository;
        this.imageService = imageService;
        this.jwtService = jwtService;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(rounds);
    }



    @Override
    public User create(User user){
        Optional<Long> id = Optional.ofNullable(user.getID());
        if (id.isPresent()){
            if (userRepository.existsById(id.get())){
                throw new IllegalArgumentException("User with id " + id.get() + " already exists");
            }
        }
        else{
            if (userRepository.existsByUsername(user.getUsername())){
                throw new IllegalArgumentException("User with username " + user.getUsername() + " already exists");
            }

            if (userRepository.existsByEmail(user.getEmail())){
                throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
            }
        }

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);
        cart.setTimestamp(new Timestamp(System.currentTimeMillis()));
        cartService.create(cart);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return getRepository().save(user);
    }

    @Override
    public void deleteById(Long id){
        if (!userRepository.existsById(id)){
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }

        User user = userRepository.findById(id).get();

        Cart cart = user.getCart();
        cartService.clearCart(cart);
        cartService.deleteById(cart.getID());

        user.getSubscribers().forEach(s -> {
            s.getSubscriber().getSubscriptions().remove(s);
            userRepository.save(s.getSubscriber());

            subscriptionRepository.delete(s);
        });

        user.getSubscriptions().forEach(s -> {
            s.getUser().getSubscribers().remove(s);
            userRepository.save(s.getUser());

            subscriptionRepository.delete(s);
        });

        for (Post likes : user.getLikes()){
            likes.getLikedUsers().remove(likes.getUser());
            postRepository.save(likes);
        }

        for (Order order : user.getOrders()){
            for (OrderProduct oP : order.getProducts()){
                oP.getProduct().getOrders().remove(oP);
                productRepository.save(oP.getProduct());

                orderProductRepository.deleteById(oP.getId());
            }
            paymentRepository.deleteById(order.getPayment().getID());

            orderRepository.deleteById(id);
        }

        for (Post post : user.getPosts()){
            for (User us : post.getLikedUsers()) {
                us.getLikes().remove(post);
                userRepository.save(us);
            }

            for (Tag tag : post.getTags()) {
                tag.getPosts().remove(post);
                tagRepository.save(tag);
            }

            for (Image image : post.getImages()) {
                imageService.deleteById(image.getID());
            }

            for (Review review : post.getReviews()){
                User author = review.getAuthor();
                author.getReviews().remove(review);
                userRepository.save(author);

                reviewRepository.deleteById(review.getID());
            }

            Product product = post.getProduct();
            for (OrderProduct oP : product.getOrders()){
                oP.getOrder().getProducts().remove(oP);
                orderRepository.save(oP.getOrder());

                orderProductRepository.deleteById(oP.getId());
            }
            for (CartProduct cP : product.getCarts()){
                cP.getCart().getProducts().remove(cP);
                cartRepository.save(cP.getCart());

                cartProductRepository.deleteById(cP.getId());
            }
            productRepository.deleteById(post.getProduct().getID());

            postRepository.deleteById(post.getID());
        }

        for (Role role : user.getRoles()){
            role.getUsers().remove(user);
            roleRepository.save(role);
        }

        for (Review review : user.getReviews()){
            review.getPost().getReviews().remove(review);
            postRepository.save(review.getPost());

            reviewRepository.deleteById(review.getID());
        }

        userRepository.deleteById(id);
    }

    @Override
    public void addSubscriptionToUsers(User user, User subscriber) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (userRepository.existsById(subscriber.getID())) {

                if (subscriptionRepository.existsByUserIdAndSubscriberId(user.getID(), subscriber.getID())){
                    throw new IllegalArgumentException("Subscription with user: " + user.getID() + " and subscriber: " + subscriber.getID() + " already exists");
                }

                if (user.getID().equals(subscriber.getID())){
                    throw new IllegalArgumentException("User cannot follow himself.");
                }

                Subscription subscription = new Subscription();
                subscription.setUser(user);
                subscription.setSubscriber(subscriber);
                subscription.setTimestamp(new Timestamp(System.currentTimeMillis()));
                subscriptionService.create(subscription);

                user.getSubscribers().add(subscription);
                subscriber.getSubscriptions().add(subscription);

                userRepository.save(user);
                userRepository.save(subscriber);
            }
            else{
                throw new IllegalArgumentException("User with id " + subscriber.getID() + " does not exists");
            }

            return;
        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    @Override
    public void removeSubscriptionToUsers(User user, User subscriber, Subscription subscription) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (subscriptionRepository.existsById(subscription.getID())) {
                if (userRepository.existsById(subscriber.getID())) {
                    user.getSubscribers().remove(subscription);
                    subscriber.getSubscriptions().remove(subscription);
                    userRepository.save(user);
                    userRepository.save(subscriber);
                    subscriptionRepository.delete(subscription);
                }
                else{
                    throw new IllegalArgumentException("User with id " + subscriber.getID() + " does not exists");
                }

                return;
            }

            throw new IllegalArgumentException("Subscription with ID " + subscription.getID() + " does not exists");

        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    @Override
    public void addRoleToUser(User user, Role role) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (roleRepository.existsById(role.getID())) {
                user.getRoles().add(role);
                role.getUsers().add(user);
                userRepository.save(user);
                roleRepository.save(role);
                return;
            }

            throw new IllegalArgumentException("Role with ID " + role.getID() + " does not exists");

        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    @Override
    public void removeRoleFromUser(User user, Role role) throws IllegalArgumentException {
        if (userRepository.existsById(user.getID())) {

            if (roleRepository.existsById(role.getID())) {
                user.getRoles().remove(role);
                role.getUsers().remove(user);
                userRepository.save(user);
                roleRepository.save(role);
                return;
            }

            throw new IllegalArgumentException("Role with ID " + role.getID() + " does not exists");

        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    public List<Subscription> getSubscribers(User user){
        if (userRepository.existsById(user.getID())){

            return new ArrayList<>(user.getSubscribers());
        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    public List<Subscription> getSubscription(User user){
        if (userRepository.existsById(user.getID())){

            return new ArrayList<>(user.getSubscriptions());
        }

        throw new IllegalArgumentException("User with ID " + user.getID() + " does not exists");
    }

    public User findByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()){
            return user.get();
        } else{
            throw new IllegalArgumentException("User with username " + username + " does not exists");
        }
    }

    public String verify(User user, AuthenticationManager authManager){
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        if (authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }

        throw new IllegalArgumentException("Failed authentication.");
    }

    @Override
    protected CrudRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(usernameOrEmail);

        if (user.isEmpty()){
            user = userRepository.findByEmail(usernameOrEmail);

            if (user.isEmpty()){
                throw new UsernameNotFoundException("There is no user with this email or username: " + usernameOrEmail);
            }
        }

        return new CustomUserDetails(
                user.get().getUsername(),
                user.get().getPassword(),
                user.get().getID(),
                user.get().getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }
}

