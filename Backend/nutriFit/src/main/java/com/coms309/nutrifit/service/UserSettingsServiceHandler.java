package com.coms309.nutrifit.service;

import com.coms309.nutrifit.repo.UserSettingsRepository;
import com.coms309.nutrifit.users.UserSettings;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSettingsServiceHandler {

    private final UserSettingsRepository userSettingsRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    public UserSettingsServiceHandler(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    public UserSettings getSettingsById(int id) {

        return userSettingsRepository.findById(id);
    }

    public String createUserSettings(UserSettings settings) {

        if(settings == null){
            return failure;
        }
        userSettingsRepository.saveAndFlush(settings);
        return success;
    }

    public UserSettings updateSettings(int id, UserSettings settings) {

        if(!userSettingsRepository.existsById(id)){
            return null;
        }
        userSettingsRepository.saveAndFlush(settings);
        return userSettingsRepository.findById(id);

    }

    public List<UserSettings> listAllUserSettings() {
        return userSettingsRepository.findAll();
    }
}
