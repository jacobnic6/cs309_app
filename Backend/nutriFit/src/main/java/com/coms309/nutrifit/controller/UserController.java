package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.service.UserSettingsServiceHandler;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.service.UserServiceHandler;
import com.coms309.nutrifit.entity.UserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {



    @Autowired
    private UserServiceHandler userServiceHandler;

    @Autowired
    private UserSettingsServiceHandler settingsServiceHandler;
    @Autowired
    private UserRepository userRepository;


    //CREATE
    @PostMapping("/users")
    String createUser(@RequestBody User user) {

       return userServiceHandler.createUser(user);
    }



    //READ
    @GetMapping(path = "/users/{id}")
    public User getUserById(@PathVariable int id) {


                return userServiceHandler.getUserById(id);
    }



    @RequestMapping(path = "/users/username/{username}")
    public User getUserByUsername(@PathVariable String username) {

        return userServiceHandler.getByUsername(username);
    }




    //UPDATE
    @PutMapping(path = "/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User user) {

        return userServiceHandler.updateUser(id, user);
    }

    //DELETE
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id) {
       // userRepository.deleteById(id);


        return userServiceHandler.deleteUser(id);
    }


    //LIST
    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
       // return userRepository.findAll();
        return  userServiceHandler.listAllUsers();
    }



    //update settings
    @PutMapping(path = "users/{userId}/settings/{settingsId}")
    public String updateUserSettings(@PathVariable int userId, @PathVariable int settingsId, @RequestBody UserSettings userSettings) {


        return userServiceHandler.updateUserSettings(userId, settingsId, userSettings);
    }
}
