package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.service.UserServiceHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	private final static String USERNAME = "johndoe";

	private final static String PASSWORD = "<PASSWORD>";

	private final static String EMAIL = "<EMAIL>";

	private final static String FIRST_NAME = "John";

	private final static String LAST_NAME = "Doe";

	@Mock
	UserServiceHandler userServiceHandler;

	User user;

	@InjectMocks
	UserController userController;

	@Test
	void createUser() throws Exception {

		Mockito.when(userServiceHandler.createUser(user)).thenReturn(user);
		User createdUser = userController.createUser(user);
		Assertions.assertNotNull(createdUser);
		assertUserEquality(user, createdUser);
		Assertions.assertNotNull(createdUser.getId());

	}

	private void assertUserEquality(User expected, User actual) {
		Assertions.assertEquals(expected.getFirstName(), actual.getFirstName());
		Assertions.assertEquals(expected.getLastName(), actual.getLastName());
		Assertions.assertEquals(expected.getUsername(), actual.getUsername());
		Assertions.assertEquals(expected.getPassword(), actual.getPassword());
		Assertions.assertEquals(expected.getEmail(), actual.getEmail());
	}

	@BeforeEach void setUp() {
		user = new User();
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setEmail(EMAIL);
	}

	@AfterEach void tearDown() {
		user = null;
	}

	@Test
	void getUserByUsername() throws Exception {

		Mockito.when(userServiceHandler.getByUsername(USERNAME)).thenReturn(user);
		User foundUser = userController.getUserByUsername(USERNAME);
		Assertions.assertNotNull(foundUser);
		assertUserEquality(user, foundUser);
		Assertions.assertNotNull(foundUser.getId());
	}

	@Test
	void getUser() {

	}

	@Test
	void updateUser() {
	}

	@Test
	void deleteUser() {
	}

	@Test
	void getAllUsers() {
	}

	@Test
	void updateUserSettings() {
	}
}