package com.coms309.nutrifit.service;

import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.repo.UserSettingsRepository;
import com.coms309.nutrifit.entity.UserSettings;
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

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    /**
     * Instantiates a new User settings service handler.
     *
     * @param userSettingsRepository the user settings repository
     * @param userRepository         the user repository
     */
    public UserSettingsServiceHandler(UserSettingsRepository userSettingsRepository, UserRepository userRepository) {
        this.userSettingsRepository = userSettingsRepository;
        this.userRepository = userRepository;
    }

    /**
     * Gets settings by id.
     *
     * @param id the id
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
     * @return the string
     */
//CREATE
    public String createUserSettings(UserSettings settings) {

        if(settings == null){
            return failure;
        }
        userSettingsRepository.saveAndFlush(settings);
        return success;
    }


    /**
     * Update settings user settings.
     *
     * @param id       the id
     * @param settings the settings
     * @return the user settings
     */
//UPDATE
    @Transactional
    public UserSettings updateSettings(int id, UserSettings settings) {

        if(!userSettingsRepository.existsById(id)){
            return null;
        }

        UserSettings existingSettings = userSettingsRepository.findById(id);

        existingSettings.setBiometricVisibility(settings.getBiometricVisibility());
        existingSettings.setMeasurementUnits(settings.getMeasurementUnits());
        existingSettings.setProfileVisibility(settings.getProfileVisibility());

        existingSettings.setMessageNotifications(settings.isMessageNotifications());
        existingSettings.setWorkoutRemindersEnabled(settings.isWorkoutRemindersEnabled());
        existingSettings.setFriendRequestNotifications(settings.isFriendRequestNotifications());
        userSettingsRepository.saveAndFlush(existingSettings);

        return userSettingsRepository.findById(id);

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
     * @return the string
     */
//DELETE
    @Transactional
    public String deleteSettings(int id) {
        if(!userSettingsRepository.existsById(id) ){
            return "User " + id + " does not exist";
        }
        if(userRepository.existsById(id)){
            return "Cannot delete existing user " + id + " settings. ";
        }

        userSettingsRepository.removeUserSettingsById(id);

        return "Settings " + id + " successfully deleted";
    }
}
