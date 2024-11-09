package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.dto.UserDto;
import com.coms309.nutrifit.entity.Friend;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.service.ServiceHandler;
import com.coms309.nutrifit.service.UserServiceHandler;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Friends controller.
 */
@RestController
@RequestMapping("/friends")
public class FriendsController {

    @Autowired
    private UserServiceHandler userServiceHandler;


    /**
     * Add friend string.
     *
     * @param userId    the user id
     * @param friendDto the friend dto
     * @return the string
     */
    @PostMapping(path = "/{userId}/add")
    public String addFriend(@PathVariable int userId, @RequestBody UserDto friendDto) {
      return userServiceHandler.addFriend(userId, friendDto);


    }

    /**
     * Gets friends by id.
     *
     * @param userId the user id
     * @return the friends by id
     */
    @GetMapping(path = "/{userId}")
    public List<User> getFriendsById(@PathVariable int userId) {
       return userServiceHandler.getFriendsById(userId);
    }


    /**
     * Gets list of friends. Path looks like /friends/get?id=bob123
     *
     * @param id the id
     * @return the friends
     */
    @GetMapping(path = "/get")
    public List<User> getFriends(@RequestParam String id) {

    if(ServiceHandler.isNumeric(id)){
        int userId = Integer.parseInt(id);
        return userServiceHandler.getFriendsById(userId);
    }
        return userServiceHandler.getFriendsByUsername(id);
    }

    /**
     * Gets friends by username.
     *
     * @param username the username
     * @return the friends by username
     */
    @GetMapping(path = "username/{username}")
    public List<User> getFriendsByUsername(@PathVariable String username) {
        return userServiceHandler.getFriendsByUsername(username);
    }

    /**
     * Gets all friendships.
     *
     * @return the all friendships
     */
    @GetMapping
    public List<Friend> getAllFriendships() {
       return userServiceHandler.getAllFriends();
    }


}
