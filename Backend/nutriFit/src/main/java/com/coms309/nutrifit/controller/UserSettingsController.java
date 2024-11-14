package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.UserSettings;
import com.coms309.nutrifit.service.UserSettingsServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type User settings controller.
 */
@RestController
public class UserSettingsController {

    @Autowired
    private UserSettingsServiceHandler settingsServiceHandler;


    /**
     * Create user string.
     *
     * @param settings the settings
     * @return the string
     */
//CREATE
    @PostMapping(path = "/settings")
    String createUser(@RequestBody UserSettings settings) {

        return settingsServiceHandler.createUserSettings(settings);
    }

    /**
     * Gets user settings.
     *
     * @param id the id
     * @return the user settings
     */
//READ
    @GetMapping(path = "/settings/{id}")
    public UserSettings getUserSettings(@PathVariable int id) {
        return settingsServiceHandler.getSettingsById(id);
    }

    /**
     * Update user settings user settings.
     *
     * @param id       the id
     * @param settings the settings
     * @return the user settings
     */
//UPDATE
    @PutMapping(path = "/settings/{id}")
    public UserSettings updateUserSettings(@PathVariable int id, @RequestBody UserSettings settings) {

        return settingsServiceHandler.updateSettings(id, settings);
    }


    /**
     * Gets all user settings.
     *
     * @return the all user settings
     */
//LIST
    @GetMapping(path = "/settings")
    public List<UserSettings> getAllUserSettings() {

        return settingsServiceHandler.listAllUserSettings();
    }

    /**
     * Delete user settings string.
     *
     * @param id the id
     * @return the string
     */
//DELETE
    @DeleteMapping("/settings/{id}")
    public String deleteUserSettings(@PathVariable int id) {

        return settingsServiceHandler.deleteSettings(id);
    }

}
