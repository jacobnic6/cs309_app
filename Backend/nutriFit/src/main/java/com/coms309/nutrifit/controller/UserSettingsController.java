package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.service.UserSettingsServiceHandler;
import com.coms309.nutrifit.users.User;
import com.coms309.nutrifit.users.UserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserSettingsController {

    @Autowired
    private UserSettingsServiceHandler settingsServiceHandler;


    //CREATE
    @PostMapping(path = "/settings")
    String createUser(@RequestBody UserSettings settings) {

        return settingsServiceHandler.createUserSettings(settings);
    }

    //READ
    @GetMapping(path = "/settings/{id}")
    public UserSettings getUserSettings(@PathVariable int id) {
        return settingsServiceHandler.getSettingsById(id);
    }





    //LIST
    @GetMapping(path = "/settings")
    public List<UserSettings> getAllUserSettings() {

        return  settingsServiceHandler.listAllUserSettings();
    }
}
