package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.UserSettings;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.service.ServiceHandler;
import com.coms309.nutrifit.service.UserServiceHandler;
import com.coms309.nutrifit.service.UserSettingsServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceHandler userServiceHandler;
    private final UserSettingsServiceHandler userSettingsServiceHandler;

    @Autowired
    public UserController(UserServiceHandler userServiceHandler, UserSettingsServiceHandler userSettingsServiceHandler) {
        this.userServiceHandler = userServiceHandler;
        this.userSettingsServiceHandler = userSettingsServiceHandler;
    }




    /**
     * Create user string.
     *
     * @param user the user
     * @return the string
     */
//CREATE
    @PostMapping
   public String createUser(@RequestBody User user) {

        return userServiceHandler.createUser(user);
    }


    /**
     * Gets user by id.
     *
     * @param username the id
     * @return the user by id
     */
//READ
    @GetMapping(path = "/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userServiceHandler.getByUsername(username);
    }

    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     */
//READ
    @GetMapping(path = "/{id}")
    public User getUser(@RequestParam int id) {

        return userServiceHandler.getUserById(id);
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
  public   User updateUser(@PathVariable int id, @RequestBody User user) {

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
    String deleteUser(@PathVariable String id) {
        int userId =0;
        if(!ServiceHandler.isNumeric(id)){
             userId = userServiceHandler.getByUsername(id).getId();
        }else{
            userId = Integer.parseInt(id);
        }

        return userServiceHandler.deleteUser(userId);
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
        return userServiceHandler.listAllUsers();
    }


    /**
     * Update user settings string.
     *
     * @param username      the user id
     * @param userSettings the user settings
     * @return the string
     */
//update settings
    @PutMapping(path = "/{username}/settings")
    public UserSettings updateUserSettings(@PathVariable String username, @RequestBody UserSettings userSettings) {


        return userSettingsServiceHandler.updateUserSettings(username,  userSettings);
    }
}
