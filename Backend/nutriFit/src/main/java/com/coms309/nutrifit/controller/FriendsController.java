package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.dto.UserDto;
import com.coms309.nutrifit.entity.Friend;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.service.ServiceHandler;
import com.coms309.nutrifit.service.UserServiceHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Friends controller.
 */
@RestController
@Tag(name = "Friend Management")
@RequestMapping("/friends")
public class FriendsController {


    private final UserServiceHandler userServiceHandler;

    @Autowired
    public FriendsController(UserServiceHandler userServiceHandler) {
        this.userServiceHandler = userServiceHandler;
    }


    /**
     * Add friend string.
     *
     * @param userId    the user id
     * @param friendDto the friend dto
     * @return the string
     */
    @Operation(summary = "Add a friend to a user", description = "Finds user by Id and adds the friend in the request body.")
    @PostMapping(path = "/{userId}/add")
    public String addFriend(@PathVariable int userId, @RequestBody UserDto friendDto) {
        return userServiceHandler.addFriend(userId, friendDto);


    }
    /**
     * Add friend string.
     *
     * @param userId    the user id
     * @param friendDto the friend dto
     * @return the string
     */
    @Operation(summary = "Add a friend to a user", description = "Finds user by Id or username and adds the friend in the request body.")
    @PostMapping(path = "/add")
    public String addFriendBody(@RequestParam String userId, @RequestBody UserDto friendDto) {
        if(userId.isEmpty()  ){
            return "UserId cannot be empty";
        }
        if (friendDto.getUsername().isEmpty()) {
            return "Friend username cannot be empty";
        }

        if(ServiceHandler.isNumeric(userId)){
            return userServiceHandler.addFriend(Integer.parseInt(userId), friendDto);
        }
        User user = userServiceHandler.getByUsername(userId);
        return userServiceHandler.addFriend(user.getId(), friendDto);


    }

//newest
@Operation(summary = "Add a friend to a user", description = "Finds user by Id or username and adds the friend by id or username.")
    @PostMapping("/addFriend/{userId}/{friendId}")
    public String addFriendByUsernameOrId(@PathVariable String userId,
                                   @PathVariable String friendId) {
        if(userId.isEmpty() || friendId.isEmpty()){
            return "Incorrect path. User and friend cannot be empty";

        }
        if(ServiceHandler.isNumeric(userId) ){
            int userIdInt = Integer.parseInt(userId);
            userId = userServiceHandler.getUserById(userIdInt).getUsername();
        }
        if(ServiceHandler.isNumeric(friendId)){
            int friendIdInt = Integer.parseInt(friendId);
            friendId = userServiceHandler.getUserById(friendIdInt).getUsername();
        }
        return userServiceHandler.addFriends(userId, friendId);


    }


    /**
     * Gets friends by id.
     *
     * @param userId the user id
     * @return the friends by id
     */
    @Operation(summary = "Get all friends of specific user", description = "Finds user by Id and returns a list of their friends.")
    @GetMapping(path = "/{userId}")
    public List<UserDto> getFriendsById(@PathVariable int userId) {
        return userServiceHandler.getFriendsById(userId);
    }

    /**
     * Gets friends by id.
     *
     * @param userId the user id
     * @return the friends by id
     */
    @Operation(summary = "Get all friends of specific user", description = "Finds user by Id or username and returns a list of their friends.")
    @GetMapping(path = "/get")
    public List<UserDto> getUserFriends(@RequestParam String userId) {
        if(userId.isEmpty()  ){
            throw new IllegalArgumentException("UserId cannot be empty");
        }
        String id = "";
        if (ServiceHandler.isNumeric(userId)) {
           id = userServiceHandler.getUserById(Integer.parseInt(userId)).getUsername();
            return userServiceHandler.getFriendsByUsername(id);
        }
        return userServiceHandler.getFriendsByUsername(userId);

    }



    /**
     * Gets friends by username.
     *
     * @param username the username
     * @return the friends by username
     */
    @Operation(summary = "Get all friends of specific user", description = "Finds user by Id or username and returns a list of their friends.")
    @GetMapping(path = "/list/{username}")
    public List<UserDto> getFriendsByUsername(@PathVariable String username) {
        if(ServiceHandler.isNumeric(username)){
            return userServiceHandler.getFriendsById(Integer.parseInt(username));
        }
        return userServiceHandler.getFriendsByUsername(username);
    }

    /**
     * Gets all friendships.
     *
     * @return the all friendships
     */
    @Operation(summary = "Get all friendships", description = "Returns a list of all friendships.")
    @GetMapping
    public List<Friend> getAllFriendships() {
        return userServiceHandler.getAllFriends();
    }

    @Operation(summary = "Remove friend", description = "Removes friend from friends list")
    @DeleteMapping("/{userId}/{friendId}")
    public String removeFriendship(@PathVariable String userId,
                                   @PathVariable String friendId) {
        if(userId.isEmpty() || friendId.isEmpty()){
            return "Incorrect path. User and friend cannot be empty";

        }
        User user;
        User friend;
        if(ServiceHandler.isNumeric(userId) ){
            int userIdInt = Integer.parseInt(userId);
            //userId = userServiceHandler.getUserById(userIdInt).getUsername();
            user = userServiceHandler.getUserById(userIdInt);
        }else{
            user = userServiceHandler.getByUsername(userId);
        }
        if(ServiceHandler.isNumeric(friendId)){
            int friendIdInt = Integer.parseInt(friendId);
            //friendId = userServiceHandler.getUserById(friendIdInt).getUsername();
            friend = userServiceHandler.getUserById(friendIdInt);
        }else{
            friend = userServiceHandler.getByUsername(friendId);
        }
        return userServiceHandler.removeFriend(user, friend);


    }



}
