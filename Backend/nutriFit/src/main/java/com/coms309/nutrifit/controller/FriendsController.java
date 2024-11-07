package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.dto.UserDto;
import com.coms309.nutrifit.entity.Friend;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.service.UserServiceHandler;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendsController {

    @Autowired
    private UserServiceHandler userServiceHandler;


    @PostMapping(path = "/{userId}/add")
    public String addFriend(@PathVariable int userId, @RequestBody UserDto friendDto) {
      return userServiceHandler.addFriend(userId, friendDto);


    }

    @GetMapping(path = "/{userId}")
    public List<User> getFriends(@PathVariable int userId) {
       return userServiceHandler.getFriends(userId);
    }
    @GetMapping
    public List<Friend> getAllFriendships() {
       return userServiceHandler.getAllFriends();
    }


}
