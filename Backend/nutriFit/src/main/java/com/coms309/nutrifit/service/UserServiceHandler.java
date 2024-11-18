package com.coms309.nutrifit.service;

import com.coms309.nutrifit.dto.UserDto;
import com.coms309.nutrifit.entity.Friend;
import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.UserSettings;
import com.coms309.nutrifit.repo.FriendRepository;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.repo.UserSettingsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type User service handler.
 */
@Service
public class UserServiceHandler extends ServiceHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private ObjectMapper mapper;




    /**
     * Create user string.
     *
     * @param user the user
     * @return the string
     */
//CREATE
    //Creates a user with a default settings entity
    public User createUser(User user) {
        if (user == null || userRepository.existsUserByIdOrEmailOrUsername(user.getId(), user.getEmail(), user.getUsername())) {
            return null;
        }
        user.setLastLogin(LocalDateTime.now());
        UserSettings settings = new UserSettings();

        user.setSettings(settings);

        Profile profile = new Profile(user);
        user.setProfile(profile);
        profile.setUser(user);

        //user.setProfile(new Profile());
        return userRepository.saveAndFlush(user);

    }

    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     */
//READ
    public User getUserById(int id) {
        if (!userRepository.existsById(id)) {
            return null;
        }

        return userRepository.findById(id);

    }

    /**
     * Update user user.
     *
     * @param id   the id
     * @param user the user
     * @return the user
     */
//UPDATE
    public User updateUser(int id, User user) {
        User u = userRepository.findById(id);
        if (u == null) {
            return null;
        }
        if (userRepository.findById(id) == null) {
            System.out.println();
        }
        userRepository.saveAndFlush(user);
        return userRepository.findById(id);
    }

    /**
     * Delete user string.
     *
     * @param id the id
     * @return the string
     */
//DELETE
    public String deleteUser(int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            return "User " + id + " does not exist";
        }
        // Delete all friendships where this user is involved
        List<Friend> friendsByFirst = friendRepository.findByFirstUser(user);
        List<Friend> friendsBySecond = friendRepository.findBySecondUser(user);

        for (Friend friend : friendsByFirst) {
            friendRepository.delete(friend);
        }
        for (Friend friend : friendsBySecond) {
            friendRepository.delete(friend);
        }

        // Now it's safe to delete the user
        userRepository.deleteById(id);

        return "User " + id + " has been deleted";
    }

    /**
     * List all users list.
     *
     * @return the list
     */
//LIST
    public List<User> listAllUsers() {
        return userRepository.findAll();
    }


    /**
     * Update user settings string.
     *
     * @param userId     the user id
     * @param settingsId the settings id
     * @param settings   the settings
     * @return the string
     */
    public String updateUserSettings(int userId, int settingsId, UserSettings settings) {
        User u = userRepository.findById(userId);
        UserSettings existingSettings = userSettingsRepository.findById(settingsId);
        if (u == null || existingSettings == null) {
            return failure;
        }
        u.setSettings(settings);

        userRepository.saveAndFlush(u);


        return success;

    }


    /**
     * Gets by username.
     *
     * @param username the username
     * @return the by username
     */
    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    /**
     * Add friend string.
     *
     * @param userId    the user id
     * @param friendDto the friend dto
     * @return the string
     */
    public String addFriend(int userId, UserDto friendDto) {
        User friendUser = userRepository.findByUsername(friendDto.getUsername());
        if (friendRepository.findFriendshipBetween(userId, friendUser.getId()) != null) {
            return "Friendship already exists";
        }

        User user = userRepository.findById(userId);
        if (user == null) {
            return failure;
        }

        UserDto userDto = mapper.convertValue(user, UserDto.class);
        Friend newFriend = new Friend();

        User initiatingUser = user;
        User receivingUser = friendUser;
        if (user.getId() > friendUser.getId()) {
            initiatingUser = friendUser;
            receivingUser = user;
        }

        if (!friendRepository.existsByFirstUserAndSecondUser(initiatingUser, receivingUser)) {
            newFriend.setDateAdded(LocalDate.now());
            newFriend.setFirstUser(initiatingUser);
            newFriend.setSecondUser(receivingUser);
            friendRepository.save(newFriend);
        }

        if (friendRepository.existsByFirstUserAndSecondUser(initiatingUser, receivingUser)) {
            return success;
        }

        return failure;
    }

    /**
     * Gets friends by id.
     *
     * @param userId the user id
     * @return the friends by id
     */
    public List<UserDto> getFriendsById(int userId) {

        User user = userRepository.findById(userId);

        List<Friend> friendsByFirst = friendRepository.findByFirstUser(user);
        List<Friend> friendsBySecond = friendRepository.findBySecondUser(user);
        List<User> friends = new ArrayList<>();

        for (Friend friend : friendsByFirst) {
            friends.add(userRepository.findById(friend.getSecondUser().getId()));
        }
        for (Friend friend : friendsBySecond) {
            friends.add(userRepository.findById(friend.getFirstUser().getId()));
        }
        List<UserDto> userDtoList = new ArrayList<>();
        for(User u : friends){
            userDtoList.add(mapper.convertValue(u, UserDto.class));

        }

        return userDtoList;
    }

    /**
     * Gets all friends.
     *
     * @return the all friends
     */
    public List<Friend> getAllFriends() {
        return friendRepository.findAll();
    }

    /**
     * Gets friends by username.
     *
     * @param username the username
     * @return the friends by username
     */
    public List<UserDto> getFriendsByUsername(String username) {
      User user = userRepository.findByUsername(username);
    return getFriendsById(user.getId());


    }

    public String removeFriend(String userId, String friendId) {
        User user = userRepository.findByUsername(userId);

//        for(Friend friend : user.getFriends()){
//            if(friend.getSecondUser().getUsername().equals(friendId)){}
//        }
//
        User friend = userRepository.findByUsername(friendId);

        Friend friendship = friendRepository.findFriendshipBetween(user.getId(), friend.getId());

        friendRepository.delete(friendship);
        return "Friendship deleted";
    }

    public String addFriends(String userId, String friendId) {
        User user = userRepository.findByUsername(userId);
        User friendUser = userRepository.findByUsername(friendId);

        if(user == null || friendUser  == null){
            return "User does not exist";
        }

        if (friendRepository.findFriendshipBetween(user.getId(), friendUser.getId()) != null) {
            return "Friendship already exists";
        }



        Friend newFriend = new Friend();

        User initiatingUser = user;
        User receivingUser = friendUser;
        if (user.getId() > friendUser.getId()) {
            initiatingUser = friendUser;
            receivingUser = user;
        }

        if (!friendRepository.existsByFirstUserAndSecondUser(initiatingUser, receivingUser)) {
            newFriend.setDateAdded(LocalDate.now());
            newFriend.setFirstUser(initiatingUser);
            newFriend.setSecondUser(receivingUser);
            friendRepository.save(newFriend);
        }

        if (friendRepository.existsByFirstUserAndSecondUser(initiatingUser, receivingUser)) {
            return success;
        }
        return failure;
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }


}
