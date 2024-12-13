package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.UserSettings;
import com.coms309.nutrifit.service.UserServiceHandler;
import com.coms309.nutrifit.service.UserSettingsServiceHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserServiceHandler userServiceHandler;

	@MockBean
	private UserSettingsServiceHandler userSettingsServiceHandler;

	@Autowired
	private ObjectMapper objectMapper;

	private User testUser;

	private UserSettings testSettings;

	@BeforeEach
	void setUp() {
		testUser = new User();
		testUser.setId(1);
		testUser.setUsername("testUser");
		testUser.setEmail("test@example.com");
		testUser.setFirstName("Test");
		testUser.setLastName("User");
		testUser.setPassword("password123");
		testUser.setLastLogin(LocalDateTime.now());

		testSettings = new UserSettings();
		testUser.setSettings(testSettings);
	}

	@Test
	void createUser_ShouldReturnCreatedUser() throws Exception {
		when(userServiceHandler.createUser(any(User.class))).thenReturn(testUser);

		mockMvc.perform(post("/users")
				                .contentType(MediaType.APPLICATION_JSON)
				                .content(objectMapper.writeValueAsString(testUser)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(testUser.getId()))
				.andExpect(jsonPath("$.username").value(testUser.getUsername()));
	}

	@Test
	void getUserByUsername_ShouldReturnUser() throws Exception {
		when(userServiceHandler.getByUsername("testUser")).thenReturn(testUser);

		mockMvc.perform(get("/users/username/testUser"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(testUser.getId()))
				.andExpect(jsonPath("$.username").value(testUser.getUsername()));
	}

	@Test
	void getUserById_ShouldReturnUser() throws Exception {
		when(userServiceHandler.getUserById(1)).thenReturn(testUser);

		mockMvc.perform(get("/users/userId/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(testUser.getId()))
				.andExpect(jsonPath("$.username").value(testUser.getUsername()));
	}

	@Test
	void updateUser_ShouldReturnUpdatedUser() throws Exception {
		when(userServiceHandler.updateUser(eq(1), any(User.class))).thenReturn(testUser);

		mockMvc.perform(put("/users/userId/1")
				                .contentType(MediaType.APPLICATION_JSON)
				                .content(objectMapper.writeValueAsString(testUser)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(testUser.getId()))
				.andExpect(jsonPath("$.username").value(testUser.getUsername()));
	}

	@Test
	void deleteUser_ShouldReturnSuccessMessage() throws Exception {
		when(userServiceHandler.deleteUser(1)).thenReturn("User 1 has been deleted");

		mockMvc.perform(delete("/users/1"))
				.andExpect(status().isOk())
				.andExpect(content().string("User 1 has been deleted"));
	}

	@Test
	void getAllUsers_ShouldReturnListOfUsers() throws Exception {
		when(userServiceHandler.listAllUsers()).thenReturn(Arrays.asList(testUser));

		mockMvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(testUser.getId()))
				.andExpect(jsonPath("$[0].username").value(testUser.getUsername()));
	}

	@Test
	void updateUserSettings_ShouldReturnUpdatedSettings() throws Exception {
		when(userSettingsServiceHandler.updateUserSettings(eq("testUser"), any(UserSettings.class)))
				.thenReturn(testSettings);

		mockMvc.perform(put("/users/testUser/settings")
				                .contentType(MediaType.APPLICATION_JSON)
				                .content(objectMapper.writeValueAsString(testSettings)))
				.andExpect(status().isOk());
	}
}