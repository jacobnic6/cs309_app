package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.UserSettings;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.repo.UserSettingsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type User settings service handler.
 */
@Service
public class UserSettingsServiceHandler {

	private final UserSettingsRepository userSettingsRepository;

	private final UserRepository userRepository;

	private final ObjectMapper mapper;

	private final String success = "{\"message\":\"success\"}";

	private final String failure = "{\"message\":\"failure\"}";

	/**
	 * Instantiates a new User settings service handler.
	 *
	 * @param userSettingsRepository the user settings repository
	 * @param userRepository         the user repository
	 * @param mapper                 the mapper
	 */
	@Autowired
	public UserSettingsServiceHandler(UserSettingsRepository userSettingsRepository, UserRepository userRepository, ObjectMapper mapper) {
		this.userSettingsRepository = userSettingsRepository;
		this.userRepository = userRepository;
		this.mapper = mapper;
	}

	/**
	 * Gets settings by id.
	 *
	 * @param id the id
	 *
	 * @return the settings by id
	 */
//READ
	public UserSettings getSettingsById(int id) {

		return userSettingsRepository.findById(id);
	}

	/**
	 * Create user settings string.
	 *
	 * @param settings the settings
	 *
	 * @return the string
	 */
//CREATE
	public String createUserSettings(UserSettings settings) {

		if (settings == null)
		{
			return failure;
		}
		userSettingsRepository.saveAndFlush(settings);
		return success;
	}

	/**
	 * List all user settings list.
	 *
	 * @return the list
	 */
//LIST
	public List<UserSettings> listAllUserSettings() {
		return userSettingsRepository.findAll();
	}

	/**
	 * Delete settings string.
	 *
	 * @param id the id
	 *
	 * @return the string
	 */
//DELETE
	@Transactional
	public String deleteSettings(int id) {
		if (!userSettingsRepository.existsById(id))
		{
			return "User " + id + " does not exist";
		}
		if (userRepository.existsById(id))
		{
			return "Cannot delete existing user " + id + " settings. ";
		}

		userSettingsRepository.removeUserSettingsById(id);

		return "Settings " + id + " successfully deleted";
	}

	/**
	 * Update settings user settings.
	 *
	 * @param id       the id
	 * @param settings the settings
	 *
	 * @return the user settings
	 */
	public UserSettings updateSettings(int id, UserSettings settings) {
		String username = userRepository.findById(id).getUsername();
		if (username.isEmpty())
		{
			return null;
		}
		return updateUserSettings(username, settings);
	}

	/**
	 * Update settings user settings.
	 *
	 * @param username the username
	 * @param settings the settings
	 *
	 * @return the user settings
	 */
//UPDATE
	@Transactional
	public UserSettings updateUserSettings(String username, UserSettings settings) {
		if (!userRepository.existsByUsername(username))
		{
			throw new EntityNotFoundException("No user with : " + username + " exists");

		}
		User u = userRepository.findByUsername(username);

		UserSettings existingSettings = u.getSettings();
		if (existingSettings == null)
		{
			u.setSettings(settings);
			settings.setId(u.getId());
		} else
		{
			existingSettings.setBiometricVisibility(settings.getBiometricVisibility());
			existingSettings.setMeasurementUnits(settings.getMeasurementUnits());
			existingSettings.setProfileVisibility(settings.getProfileVisibility());
			existingSettings.setMessageNotifications(settings.isMessageNotifications());
			existingSettings.setFriendRequestNotifications(settings.isFriendRequestNotifications());
			existingSettings.setWorkoutRemindersEnabled(settings.isWorkoutRemindersEnabled());
		}

		userRepository.saveAndFlush(u);
		return userSettingsRepository.saveAndFlush(existingSettings);

	}
}
