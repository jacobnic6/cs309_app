package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.UserWeightDto;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.service.UserSettingsServiceHandler;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.service.UserServiceHandler;
import com.coms309.nutrifit.entity.UserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {



    @Autowired
    private UserServiceHandler userServiceHandler;

    @Autowired
    private UserSettingsServiceHandler settingsServiceHandler;
    @Autowired
    private UserRepository userRepository;


    //CREATE
    @PostMapping
    String createUser(@RequestBody User user) {

       return userServiceHandler.createUser(user);
    }



    //READ
    @GetMapping(path = "/{id}")
    public User getUserById(@PathVariable int id) {


                return userServiceHandler.getUserById(id);
    }



    @GetMapping(path = "/username/{username}")
    public User getUserByUsername(@PathVariable String username) {

        return userServiceHandler.getByUsername(username);
    }

//String url = loginUrl + "/username/?username=" + username + "&password=" + password;
   // http://coms-3090-058.class.las.iastate.edu:8080/users/username/?username=




    //UPDATE
    @PutMapping(path = "/{id}")
    User updateUser(@PathVariable int id, @RequestBody User user) {

        return userServiceHandler.updateUser(id, user);
    }

    //DELETE
    @DeleteMapping(path = "/{id}")
    String deleteUser(@PathVariable int id) {
       // userRepository.deleteById(id);


        return userServiceHandler.deleteUser(id);
    }


    //LIST
    @GetMapping()
    public List<User> getAllUsers() {
       // return userRepository.findAll();
        return  userServiceHandler.listAllUsers();
    }



    //update settings
    @PutMapping(path = "/{userId}/settings/{settingsId}")
    public String updateUserSettings(@PathVariable int userId, @PathVariable int settingsId, @RequestBody UserSettings userSettings) {


        return userServiceHandler.updateUserSettings(userId, settingsId, userSettings);
    }
}
