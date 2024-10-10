package com.coms309.nutrifit.service;

import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.repo.UserSettingsRepository;
import com.coms309.nutrifit.entity.UserSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserSettingsServiceHandler {

    private final UserSettingsRepository userSettingsRepository;
    private final UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    public UserSettingsServiceHandler(UserSettingsRepository userSettingsRepository, UserRepository userRepository) {
        this.userSettingsRepository = userSettingsRepository;
        this.userRepository = userRepository;
    }

    //READ
    public UserSettings getSettingsById(int id) {

        return userSettingsRepository.findById(id);
    }

    //CREATE
    public String createUserSettings(UserSettings settings) {

        if(settings == null){
            return failure;
        }
        userSettingsRepository.saveAndFlush(settings);
        return success;
    }


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


    //LIST
    public List<UserSettings> listAllUserSettings() {
        return userSettingsRepository.findAll();
    }

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
