package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.*;
import com.coms309.nutrifit.entity.fitness.UserWeight;
import com.coms309.nutrifit.repo.FriendRepository;
import com.coms309.nutrifit.repo.ProfileRepository;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.repo.UserSettingsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceHandlerTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserSettingsRepository userSettingsRepository;

	@Mock
	private FriendRepository friendRepository;

	@Mock
	private ProfileRepository profileRepository;

	@Mock
	private ObjectMapper mapper;

	@InjectMocks
	private UserServiceHandler userServiceHandler;

	private UserSettings testSettings;

	private User testUser;

	private User friendUser;

	private Profile testProfile;

	private Profile friendProfile;

	private Friend testFriendship;

	private ProfileDto testProfileDto;

	@BeforeEach
	void setUp() {
		testUser = new User();
		testUser.setId(1);
		testUser.setUsername("testUser");
		testUser.setEmail("test@example.com");
		testUser.setFirstName("Test");
		testUser.setLastName("User");

		// Set up friend user
		friendUser = new User();
		friendUser.setId(2);
		friendUser.setUsername("friendUser");
		friendUser.setEmail("friend@example.com");
		friendUser.setFirstName("Friend");
		friendUser.setLastName("User");

		// Set up test profile
		testProfile = new Profile(testUser);
		testProfile.setId(1);
		testProfile.setBio("Test bio");
		testProfile.setAge(25);
		testProfile.setHeight(180);
		testProfile.setWeight(75.0);
		testProfile.setFitnessGoal("Get stronger");
		testProfile.setWorkouts(new ArrayList<>());
		testProfile.setMuscleProgress(new HashMap<>());

		// Set up friend profile
		friendProfile = new Profile(friendUser);
		friendProfile.setId(2);

		// Set up test friendship
		testFriendship = new Friend();
		testFriendship.setId(1);
		testFriendship.setDateAdded(LocalDate.now());
		testFriendship.setFirstUser(testUser);
		testFriendship.setSecondUser(friendUser);

		// Set up ProfileDto
		testProfileDto = new ProfileDto();
		testProfileDto.setId(2);
		testProfileDto.setName("friendUser");
		testProfileDto.setBio("Friend bio");
		testProfileDto.setWeight(70.0);
		testProfileDto.setAge(24);
		testProfileDto.setHeight(175);
	}

	@Test
	void createUser_ShouldCreateNewUser() {
		when(userRepository.existsUserByIdOrEmailOrUsername(anyInt(), anyString(), anyString()))
				.thenReturn(false);
		when(userRepository.saveAndFlush(any(User.class))).thenReturn(testUser);

		User result = userServiceHandler.createUser(testUser);

		assertNotNull(result);
		assertEquals(testUser.getUsername(), result.getUsername());
		verify(userRepository).saveAndFlush(any(User.class));
	}

	@Test
	void createUser_ShouldThrowException_WhenUserExists() {
		when(userRepository.existsUserByIdOrEmailOrUsername(anyInt(), anyString(), anyString()))
				.thenReturn(true);

		assertThrows(IllegalArgumentException.class, () -> userServiceHandler.createUser(testUser));
	}

	@Test
	void getUserById_ShouldReturnUser() {
		when(userRepository.existsById(1)).thenReturn(true);
		when(userRepository.findById(1)).thenReturn(testUser);

		User result = userServiceHandler.getUserById(1);

		assertNotNull(result);
		assertEquals(testUser.getId(), result.getId());
	}

	@Test
	void updateUser_ShouldUpdateExistingUser() {
		when(userRepository.findById(1)).thenReturn(testUser);
		when(userRepository.saveAndFlush(any(User.class))).thenReturn(testUser);

		User result = userServiceHandler.updateUser(1, testUser);

		assertNotNull(result);
		verify(userRepository).saveAndFlush(any(User.class));
	}

	@Test
	void deleteUser_ShouldDeleteUserAndFriendships() {
		when(userRepository.findById(1)).thenReturn(testUser);
		when(friendRepository.findByFirstUser(testUser)).thenReturn(Arrays.asList(testFriendship));
		when(friendRepository.findBySecondUser(testUser)).thenReturn(Arrays.asList());

		String result = userServiceHandler.deleteUser(1);

		assertTrue(result.contains("has been deleted"));
		verify(friendRepository).delete(any(Friend.class));
		verify(userRepository).deleteById(1);
	}

	@Test
	void listAllUsers_ShouldReturnAllUsers() {
		List<User> users = Arrays.asList(testUser);
		when(userRepository.findAll()).thenReturn(users);

		List<User> result = userServiceHandler.listAllUsers();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(testUser.getId(), result.get(0).getId());
	}

	@Test
	void getByUsername_ShouldReturnUser() {
		when(userRepository.findByUsername("testUser")).thenReturn(testUser);

		User result = userServiceHandler.getByUsername("testUser");

		assertNotNull(result);
		assertEquals(testUser.getUsername(), result.getUsername());
	}

	@Test
	void existsByUsername_ShouldReturnTrue_WhenUserExists() {
		when(userRepository.existsByUsername("testUser")).thenReturn(true);

		boolean result = userServiceHandler.existsByUsername("testUser");

		assertTrue(result);
	}

	@Test
	void getFriendsById_ShouldReturnFriendsList() {
		// Setup friends lists
		List<Friend> friendsByFirst = Arrays.asList(testFriendship);
		List<Friend> friendsBySecond = new ArrayList<>();

		// Mock repository calls
		when(userRepository.findById(1)).thenReturn(testUser);
		when(friendRepository.findByFirstUser(testUser)).thenReturn(friendsByFirst);
		when(friendRepository.findBySecondUser(testUser)).thenReturn(friendsBySecond);

		// Mock mapper for Profile to ProfileDto conversion
		doReturn(testProfileDto).when(mapper).convertValue(any(), eq(ProfileDto.class));

		// Execute test
		List<ProfileDto> result = userServiceHandler.getFriendsById(1);

		// Verify results
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("friendUser", result.get(0).getName());
		verify(friendRepository).findByFirstUser(testUser);
		verify(friendRepository).findBySecondUser(testUser);
	}

	@Test
	void getFriendsById_ShouldReturnEmptyList_WhenNoFriends() {
		when(userRepository.findById(1)).thenReturn(testUser);
		when(friendRepository.findByFirstUser(testUser)).thenReturn(new ArrayList<>());
		when(friendRepository.findBySecondUser(testUser)).thenReturn(new ArrayList<>());

		List<ProfileDto> result = userServiceHandler.getFriendsById(1);

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	void addFriends_ShouldAddNewFriendship() {
		// Setup
		when(userRepository.findByUsername("testUser")).thenReturn(testUser);
		when(userRepository.findByUsername("friendUser")).thenReturn(friendUser);
		when(friendRepository.findFriendshipBetween(1, 2)).thenReturn(null);
		// First check returns false (friendship doesn't exist), second check returns true (friendship created)
		when(friendRepository.existsByFirstUserAndSecondUser(any(), any()))
				.thenReturn(false)
				.thenReturn(true);

		// Execute
		String result = userServiceHandler.addFriends("testUser", "friendUser");

		// Verify
		assertEquals("{\"message\":\"success\"}", result);
		verify(friendRepository).save(any(Friend.class));
		verify(friendRepository, times(2)).existsByFirstUserAndSecondUser(any(), any());
	}

	@Test
	void addFriends_ShouldSwapUsers_WhenFirstIdGreater() {
		// Setup users with IDs that will trigger the swap
		User user1 = new User();
		user1.setId(2);
		User user2 = new User();
		user2.setId(1);

		when(userRepository.findByUsername("user1")).thenReturn(user1);
		when(userRepository.findByUsername("user2")).thenReturn(user2);
		when(friendRepository.findFriendshipBetween(2, 1)).thenReturn(null);
		// First check returns false (friendship doesn't exist), second check returns true (friendship created)
		when(friendRepository.existsByFirstUserAndSecondUser(any(), any()))
				.thenReturn(false)
				.thenReturn(true);

		// Execute
		String result = userServiceHandler.addFriends("user1", "user2");

		// Verify
		assertEquals("{\"message\":\"success\"}", result);
		verify(friendRepository).save(argThat(friend ->
				                                      friend.getFirstUser().getId() < friend.getSecondUser().getId()
		));
		verify(friendRepository, times(2)).existsByFirstUserAndSecondUser(any(), any());
	}

	@Test
	void addFriends_ShouldReturnError_WhenFriendshipExists() {
		// Setup
		when(userRepository.findByUsername("testUser")).thenReturn(testUser);
		when(userRepository.findByUsername("friendUser")).thenReturn(friendUser);
		when(friendRepository.findFriendshipBetween(1, 2)).thenReturn(testFriendship);

		// Execute
		String result = userServiceHandler.addFriends("testUser", "friendUser");

		// Verify
		assertEquals("Friendship already exists", result);
		verify(friendRepository, never()).save(any(Friend.class));
		verify(friendRepository, never()).existsByFirstUserAndSecondUser(any(), any());
	}

	@Test
	void addFriends_ShouldReturnError_WhenUserNotFound() {
		// Setup
		when(userRepository.findByUsername("testUser")).thenReturn(null);

		// Execute
		String result = userServiceHandler.addFriends("testUser", "friendUser");

		// Verify
		assertEquals("User does not exist", result);
		verify(friendRepository, never()).save(any(Friend.class));
		verify(friendRepository, never()).existsByFirstUserAndSecondUser(any(), any());
	}

	@Test
	void addFriends_ShouldReturnFailure_WhenFriendshipNotCreated() {
		// Setup
		when(userRepository.findByUsername("testUser")).thenReturn(testUser);
		when(userRepository.findByUsername("friendUser")).thenReturn(friendUser);
		when(friendRepository.findFriendshipBetween(1, 2)).thenReturn(null);
		// Both checks return false indicating friendship wasn't created
		when(friendRepository.existsByFirstUserAndSecondUser(any(), any()))
				.thenReturn(false)
				.thenReturn(false);

		// Execute
		String result = userServiceHandler.addFriends("testUser", "friendUser");

		// Verify
		assertEquals("{\"message\":\"failure\"}", result);
		verify(friendRepository).save(any(Friend.class));
		verify(friendRepository, times(2)).existsByFirstUserAndSecondUser(any(), any());
	}

	@Test
	void removeFriend_ShouldRemoveFriendship() {
		when(friendRepository.findFriendshipBetween(1, 2)).thenReturn(testFriendship);

		String result = userServiceHandler.removeFriend(testUser, friendUser);

		assertEquals("Friendship deleted", result);
		verify(friendRepository).delete(testFriendship);
	}

	@Test
	void addWeight_ShouldAddUserWeight() {
		UserWeight bodyWeight = new UserWeight();
		bodyWeight.setWeight(75.0);
		testUser.setBodyWeights(new ArrayList<>());

		when(userRepository.existsByUsername("testUser")).thenReturn(true);
		when(userRepository.findByUsername("testUser")).thenReturn(testUser);

		userServiceHandler.addWeight("testUser", bodyWeight);

		verify(userRepository).saveAndFlush(testUser);
		assertTrue(testUser.getBodyWeights().contains(bodyWeight));
		assertEquals(testUser, bodyWeight.getUser());
	}

	@Test
	void addWeight_ShouldThrowException_WhenUserNotFound() {
		UserWeight bodyWeight = new UserWeight();
		when(userRepository.existsByUsername("nonexistentUser")).thenReturn(false);

		NullPointerException exception = assertThrows(NullPointerException.class, () ->
				                                                                          userServiceHandler.addWeight("nonexistentUser", bodyWeight));

		assertEquals("User does not exist", exception.getMessage());
		verify(userRepository, never()).saveAndFlush(any(User.class));
	}

	@Test
	void addWeight_ShouldThrowException_WhenWeightIsNull() {
		when(userRepository.existsByUsername("testUser")).thenReturn(true);

		NullPointerException exception = assertThrows(NullPointerException.class, () ->
				                                                                          userServiceHandler.addWeight("testUser", null));

		assertEquals("Weight is null", exception.getMessage());
		verify(userRepository, never()).saveAndFlush(any(User.class));
	}
}
