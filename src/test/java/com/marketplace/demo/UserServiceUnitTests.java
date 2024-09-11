package com.marketplace.demo;


import java.util.List;

import com.marketplace.demo.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marketplace.demo.persistance.UserRepository;
import com.marketplace.demo.persistance.CartRepository;
import com.marketplace.demo.persistance.RoleRepository;
import com.marketplace.demo.persistance.SubscriptionRepository;
import com.marketplace.demo.service.UserService.UserService;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceUnitTests {

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;
	@MockBean
	private CartRepository cartRepository;
	@MockBean
	private SubscriptionRepository subscriptionRepository;
	@MockBean
	private RoleRepository roleRepository;
	
	private User user;
	private Role role;
	private User subscriber;

	@BeforeEach
	void setRules() {
		Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
		Mockito.when(userRepository.existsById(user.getID())).thenReturn(true);
		Mockito.when(userRepository.existsById(subscriber.getID())).thenReturn(true);
		Mockito.when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
		Mockito.when(roleRepository.existsById(role.getID())).thenReturn(true);
	}

	@BeforeEach
	public void setUp() {
		user = new User();
		user.setId(1L);
		user.setPassword("myNewPassword");
		user.setUsername("newUsername");

		role = new Role();

		subscriber = new User();
		subscriber.setId(2L);
	}

	private boolean checkSubscriptions(User user, User subscriber) {
		
		int counter = 0;

		for (Subscription subscription : subscriber.getSubscriptions()) {
			if (subscription.getUser() == user) {
				counter++;
			}
		}

		return counter == 1;
	}

	private boolean checkSubscribers(User user, User subscriber) {
		
		int counter = 0;

		for (Subscription subscription : user.getSubscribers()) {
			if (subscription.getSubscriber() == subscriber) {
				counter++;
			}
		}

		return counter == 1;
	}

	@Test
	public void create() {
		Mockito.when(userRepository.existsById(user.getID())).thenReturn(false);

		user = userService.create(user);

		Mockito.verify(cartRepository, Mockito.atLeastOnce()).save(user.getCart());
		Mockito.verify(userRepository, Mockito.atLeastOnce()).save(user);
	}

	@Test
	public void addSubscriptionToUser() {

		Mockito.when(subscriptionRepository.existsByUserIdAndSubscriberId(user.getID(), subscriber.getID())).thenReturn(false);

		userService.addSubscriptionToUsers(user, subscriber);

		Assertions.assertTrue(checkSubscriptions(user, subscriber));
		Assertions.assertTrue(checkSubscribers(user, subscriber));
		Mockito.verify(userRepository, Mockito.atLeastOnce()).save(user);
		Mockito.verify(userRepository, Mockito.atLeastOnce()).save(subscriber);
	}

	@Test
	public void addRoleToUser() {

		userService.addRoleToUser(user, role);
		Assertions.assertTrue(user.getRoles().contains(role));
		Assertions.assertTrue(role.getUsers().contains(user));

		Mockito.verify(roleRepository, Mockito.atLeastOnce()).save(role);
		Mockito.verify(userRepository, Mockito.atLeastOnce()).save(user);
	}

	@Test
	public void getSubscribers() {
		List<Subscription> subs = userService.getSubscribers(user);

		for (Subscription sub : subs) {
			Assertions.assertTrue(checkSubscribers(user, sub.getSubscriber()));
		}
	}

	@Test
	public void getSubscribedUsers() {
		List<Subscription> subscribedUsers = userService.getSubscription(user);

		for (Subscription sub : subscribedUsers) {
			Assertions.assertTrue(checkSubscriptions(sub.getUser(), user));
		}
	}

}

