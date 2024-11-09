package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.service.ServiceHandler;
import com.coms309.nutrifit.service.UserSettingsServiceHandler;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.service.UserServiceHandler;
import com.coms309.nutrifit.entity.UserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/users")
public class UserController {



    @Autowired
    private UserServiceHandler userServiceHandler;

    @Autowired
    private UserSettingsServiceHandler settingsServiceHandler;
    @Autowired
    private UserRepository userRepository;


    /**
     * Create user string.
     *
     * @param user the user
     * @return the string
     */
//CREATE
    @PostMapping
    String createUser(@RequestBody User user) {

       return userServiceHandler.createUser(user);
    }


    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     */
//READ
    @GetMapping(path = "/{id}")
    public User getUserById(@PathVariable int id) {


                return userServiceHandler.getUserById(id);
    }
    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     */
//READ
    @GetMapping(path = "/")
    public User getUser(@RequestParam String id) {

        if(ServiceHandler.isNumeric(id)){
            int userId = Integer.parseInt(id);

            return userServiceHandler.getUserById(userId);
        }
        return userServiceHandler.getByUsername(id);
    }


    /**
     * Gets user by username.
     *
     * @param username the username
     * @return the user by username
     */
    @GetMapping(path = "/username/{username}")
    public User getUserByUsername(@PathVariable String username) {

        return userServiceHandler.getByUsername(username);
    }


    /**
     * Update user user.
     *
     * @param id   the id
     * @param user the user
     * @return the user
     */
//UPDATE
    @PutMapping(path = "/{id}")
    User updateUser(@PathVariable int id, @RequestBody User user) {

        return userServiceHandler.updateUser(id, user);
    }

    /**
     * Delete user string.
     *
     * @param id the id
     * @return the string
     */
//DELETE
    @DeleteMapping(path = "/{id}")
    String deleteUser(@PathVariable int id) {
       // userRepository.deleteById(id);


        return userServiceHandler.deleteUser(id);
    }


    /**
     * Gets all users.
     *
     * @return the all users
     */
//LIST
    @GetMapping()
    public List<User> getAllUsers() {
       // return userRepository.findAll();
        return  userServiceHandler.listAllUsers();
    }


    /**
     * Update user settings string.
     *
     * @param userId       the user id
     * @param settingsId   the settings id
     * @param userSettings the user settings
     * @return the string
     */
//update settings
    @PutMapping(path = "/{userId}/settings/{settingsId}")
    public String updateUserSettings(@PathVariable int userId, @PathVariable int settingsId, @RequestBody UserSettings userSettings) {


        return userServiceHandler.updateUserSettings(userId, settingsId, userSettings);
    }
}
