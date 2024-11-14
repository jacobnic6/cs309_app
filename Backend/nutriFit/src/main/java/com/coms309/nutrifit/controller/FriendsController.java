package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.dto.UserDto;
import com.coms309.nutrifit.entity.Friend;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.service.ServiceHandler;
import com.coms309.nutrifit.service.UserServiceHandler;
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
     * Add friend string.
     *
     * @param userId    the user id
     * @param friendDto the friend dto
     * @return the string
     */
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

//
//    /**
//     * Gets list of friends. Path looks like /friends/get?id=bob123
//     *
//     * @param id the id
//     * @return the friends
//     */
//    @GetMapping(path = "/get")
//    public List<UserDto> getFriends(@RequestParam String id) {
//        if(id.isEmpty()  ){
//
//        }
//
//        if (ServiceHandler.isNumeric(id)) {
//            int userId = Integer.parseInt(id);
//
//            return userServiceHandler.getFriendsById(userId);
//        }
//        return userServiceHandler.getFriendsByUsername(id);
//    }

    /**
     * Gets friends by username.
     *
     * @param username the username
     * @return the friends by username
     */
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
    @GetMapping
    public List<Friend> getAllFriendships() {
        return userServiceHandler.getAllFriends();
    }

    @DeleteMapping("/{userId}/{friendId}")
    public String removeFriendship(@PathVariable String userId,
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
        return userServiceHandler.removeFriend(userId, friendId);


    }


}
