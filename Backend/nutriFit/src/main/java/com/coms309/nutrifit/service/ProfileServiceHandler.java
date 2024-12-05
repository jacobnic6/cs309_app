package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.ImageData;
import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.repo.ImageRepository;
import com.coms309.nutrifit.repo.ProfileRepository;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Profile service handler.
 */
@Service
public class ProfileServiceHandler {

	private final ProfileRepository profileRepository;

	/**
	 * The User repository.
	 */

	private final UserRepository userRepository;

	/**
	 * The Image repository.
	 */

	private final ImageRepository imageRepository;

	/**
	 * Instantiates a new Profile service handler.
	 *
	 * @param profileRepository the profile repository
	 * @param userRepository    the user repository
	 * @param imageRepository   the image repository
	 */
	@Autowired
	public ProfileServiceHandler(ProfileRepository profileRepository, UserRepository userRepository, ImageRepository imageRepository) {
		this.profileRepository = profileRepository;
		this.userRepository = userRepository;
		this.imageRepository = imageRepository;
	}

	/**
	 * Add profile profile.
	 *
	 * @param profile the profile
	 *
	 * @return the profile
	 */
	public Profile addProfile(Profile profile) {
		if (profile == null)
		{
			throw new NullPointerException("profile null");
		}
		User user = profile.getUser();
		user.setProfile(profile);

		return userRepository.save(user).getProfile();

	}

	/**
	 * Gets profiles.
	 *
	 * @return the profiles
	 */
	public List<Profile> getProfiles() {
		return profileRepository.findAll();
	}

	/**
	 * Gets user profile.
	 *
	 * @param username the username
	 *
	 * @return the user profile
	 */
	public Profile getUserProfile(String username) {
		Profile profile = profileRepository.findByName(username);
		if (profile != null && profile.getProfileImageData() != null)
		{
			ImageData imageData = profile.getProfileImageData();
			byte[] img = ImageUtils.decompressImage(imageData.getPictureData());

		}
		return profile;
	}

	/**
	 * Create profile by name profile.
	 *
	 * @param username the username
	 *
	 * @return the profile
	 */
	public Profile createProfileByName(String username) {

		User user = userRepository.findByUsername(username);
		Profile profile = user.getProfile();
		if (user == null)
		{
			return null;
		}

		if (profile == null)
		{
			profile = new Profile(user);
			user.setProfile(profile);
			userRepository.saveAndFlush(user);
		}

		return userRepository.findByUsername(username).getProfile();
	}

	/**
	 * Update profile string.
	 *
	 * @param username the username
	 * @param profile  the profile
	 *
	 * @return the string
	 */
	public String updateProfile(String username, Profile profile) {
		User user = userRepository.findByUsername(username);
		if (user == null)
		{
			return "User not found";
		}
		user.setProfile(profile);
		userRepository.save(user);
		return "Profile updated";
	}

	/**
	 * Assign image.
	 *
	 * @param upload   the upload
	 * @param username the username
	 */
	public void assignImage(ImageData upload, String username) {
		User user = userRepository.findByUsername(username);
		Profile profile = profileRepository.findByUser(user);

		ImageData imageData = imageRepository.findByName(upload.getName());

		profile.setProfileImageData(imageData);

		profileRepository.save(profile);

	}

}
