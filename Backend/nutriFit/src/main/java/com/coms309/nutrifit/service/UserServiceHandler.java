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


    private final UserRepository userRepository;
    private final UserSettingsRepository userSettingsRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private ObjectMapper mapper;


    /**
     * Instantiates a new User service handler.
     *
     * @param userRepository         the user repository
     * @param userSettingsRepository the user settings repository
     */
    public UserServiceHandler(UserRepository userRepository, UserSettingsRepository userSettingsRepository) {
        this.userRepository = userRepository;
        this.userSettingsRepository = userSettingsRepository;
    }


    /**
     * Create user string.
     *
     * @param user the user
     * @return the string
     */
//CREATE
    //Creates a user with a default settings entity
    public String createUser(User user) {
        if (user == null || userRepository.existsUserByIdOrEmailOrUsername(user.getId(), user.getEmail(), user.getUsername())) {
            return failure;
        }
        user.setLastLogin(LocalDateTime.now());
        UserSettings settings = new UserSettings();

        user.setSettings(settings);

        Profile profile = new Profile();
        user.setProfile(profile);
        profile.setUser(user);

        //user.setProfile(new Profile());
        userRepository.saveAndFlush(user);
        //userSettingsRepository.saveAndFlush(settings);
        if (userRepository.existsByUsername(user.getUsername())) {
            return success;
        }

        return failure;
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
        if (userRepository.findById(id) == null) {
            return "User " + id + " does not exist";
        }
        String deleteMessage = "User " + id + " has been deleted";
        userRepository.deleteById(id);

        return deleteMessage;
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
        User user = userRepository.findById(userId);
        UserDto userDto = mapper.convertValue(user, UserDto.class);

        Friend friend = new Friend();
        User user2 = userRepository.findByUsername(friendDto.getUsername());
        User temp1 = user;
        User temp2 = user2;
        if (user.getId() > user2.getId()) {
            temp1 = user2;
            temp2 = user;
        }
        if (!(friendRepository.existsByFirstUserAndSecondUser(temp1, temp2))) {

            friend.setDateAdded(LocalDate.now());
            friend.setFirstUser(temp1);
            friend.setSecondUser(temp2);
            friendRepository.save(friend);
        }
        if (friendRepository.existsByFirstUserAndSecondUser(temp1, temp2)) {
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
    public List<User> getFriendsById(int userId) {

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

        return friends;
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
    public List<User> getFriendsByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return getFriendsById(user.getId());
    }


}
