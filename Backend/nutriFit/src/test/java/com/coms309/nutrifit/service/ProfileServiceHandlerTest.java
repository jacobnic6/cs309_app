package com.coms309.nutrifit.service;

import com.coms309.nutrifit.dto.ProfileUpdateDto;
import com.coms309.nutrifit.entity.ImageData;
import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.repo.ImageRepository;
import com.coms309.nutrifit.repo.ProfileRepository;
import com.coms309.nutrifit.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileServiceHandlerTest {

	@Mock
	private ProfileRepository profileRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ImageRepository imageRepository;

	@InjectMocks
	private ProfileServiceHandler profileServiceHandler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddProfile() {
		User user = new User();
		user.setUsername("testUser");

		Profile profile = new Profile();
		profile.setUser(user);
		user.setProfile(profile);

		when(userRepository.save(user)).thenReturn(user);

		Profile result = profileServiceHandler.addProfile(profile);

		assertNotNull(result);
		assertEquals("testUser", result.getUser().getUsername());
		verify(userRepository, times(1)).save(user);
	}

	@Test
	void testGetUserProfile_ProfileFound() {
		Profile profile = new Profile();
		profile.setName("testUser");

		when(profileRepository.findByName("testUser")).thenReturn(profile);

		Profile result = profileServiceHandler.getUserProfile("testUser");

		assertNotNull(result);
		assertEquals("testUser", result.getName());
		verify(profileRepository, times(1)).findByName("testUser");
	}

	@Test
	void testGetUserProfile_ProfileNotFound() {
		when(profileRepository.findByName("testUser")).thenReturn(null);

		assertThrows(NullPointerException.class, () -> profileServiceHandler.getUserProfile("testUser"));
	}

	@Test
	void testUpdateProfile() {
		Profile profile = new Profile();
		profile.setName("testUser");

		ProfileUpdateDto updateDto = new ProfileUpdateDto();
		updateDto.setBio("Updated Bio");
		updateDto.setAge(30);
		updateDto.setHeight(175);

		when(profileRepository.findByName("testUser")).thenReturn(profile);
		when(profileRepository.saveAndFlush(profile)).thenReturn(profile);

		Profile result = profileServiceHandler.updateProfile("testUser", updateDto);

		assertNotNull(result);
		assertEquals("Updated Bio", result.getBio());
		assertEquals(30, result.getAge());
		assertEquals(175, result.getHeight());
		verify(profileRepository, times(1)).saveAndFlush(profile);
	}

	@Test
	void testAssignImage() {
		Profile profile = new Profile();
		profile.setName("testUser");
		profile.setImageData(new ArrayList<>()); // Initialize the imageData list

		ImageData oldImage = new ImageData();
		oldImage.setProfilePicture(true);

		ImageData newImage = new ImageData();

		when(profileRepository.findByName("testUser")).thenReturn(profile);
		when(imageRepository.findByProfile_NameAndIsProfilePictureTrue("testUser")).thenReturn(oldImage);

		profileServiceHandler.assignImage(newImage, "testUser");

		assertFalse(oldImage.isProfilePicture());
		assertTrue(newImage.isProfilePicture());
		verify(imageRepository, times(1)).save(oldImage);
		verify(profileRepository, times(1)).save(profile);
	}
}