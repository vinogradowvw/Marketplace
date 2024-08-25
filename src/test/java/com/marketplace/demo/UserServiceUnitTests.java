package com.marketplace.demo;


import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marketplace.demo.domain.User;
import com.marketplace.demo.domain.Subscription;
import com.marketplace.demo.domain.Role;
import com.marketplace.demo.persistance.UserRepository;
import com.marketplace.demo.persistance.CartRepository;
import com.marketplace.demo.persistance.RoleRepository;
import com.marketplace.demo.persistance.SubscriptionRepository;
import com.marketplace.demo.service.UserService.UserService;

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
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Mockito.when(userRepository.existsById(user.getID())).thenReturn(true);
		Mockito.when(userRepository.save(subscriber)).thenReturn(subscriber);
		Mockito.when(userRepository.existsById(subscriber.getID())).thenReturn(true);
		Mockito.when(cartRepository.save(user.getCart())).thenReturn(user.getCart());
		Mockito.when(subscriptionRepository.existsByUserIdAndSubscriberId(user.getID(), subscriber.getID())).thenReturn(true);
		Mockito.when(roleRepository.existsById(role.getID())).thenReturn(true);
	}

	@BeforeEach
	public void setUp() {
		user = new User();
		role = new Role();
	}

	@Test
	public void create() {


		this.user = userService.create(user);

		Mockito.verify(cartRepository, Mockito.atLeastOnce()).save(user.getCart());
		Mockito.verify(userRepository, Mockito.atLeastOnce()).save(user);
	}

	private Boolean checkSubscriobtions(User user, User subscriber) {
		
		int counter = 0;
		Boolean result = false;

		for (Subscription subscribtion : subscriber.getSubscriptions()) {
			if (subscribtion.getUser() == user) {
				result = true;
				counter++;
			}
		}

		return (counter == 1) && result;
	}

	private Boolean checkSubscribers(User user, User subscriber) {
		
		int counter = 0;
		Boolean result = false;

		for (Subscription subscribtion : user.getSubscribers()) {
			if (subscribtion.getSubscriber() == subscriber) {
				result = true;
				counter++;
			}
		}

		return (counter == 1) && result;
	}

	@Test
	public void addSubscribtionToUser() {

		userService.addSubscriptionToUsers(user, subscriber);

		Assertions.assertTrue(checkSubscriobtions(user, subscriber));
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
		List<User> subs = userService.getSubscribers(user);

		for (User sub : subs) {
			Assertions.assertTrue(checkSubscribers(user, sub));
		}
	}

	@Test
	public void getSubscribedUsers() {
		List<User> subscribedUsers = userService.getSubscribedUsers(user);

		for (User sub : subscribedUsers) {
			Assertions.assertTrue(checkSubscriobtions(user, sub));
		}
	}

}

