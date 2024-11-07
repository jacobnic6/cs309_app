package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.entity.UserSettings;
import com.coms309.nutrifit.repo.UserSettingsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceHandler
    {


        private final UserRepository userRepository;
        private final UserSettingsRepository userSettingsRepository;


       // private String nullUserMessage = "{\"message\":\"User is null\"}";
        private String success = "{\"message\":\"success\"}";
        private String failure = "{\"message\":\"failure\"}";

        public UserServiceHandler(UserRepository userRepository, UserSettingsRepository userSettingsRepository) {
            this.userRepository = userRepository;
            this.userSettingsRepository = userSettingsRepository;
        }


        //CREATE
        //Creates a user with a default settings entity
        public String createUser(User user) {
            if (user == null || userRepository.existsUserByIdOrEmailOrUsername(user.getId(), user.getEmail(), user.getUsername()) ){
                return failure;
            }
            user.setLastLogin(LocalDateTime.now());


            UserSettings settings = new UserSettings();
            user.setSettings(settings);
            userRepository.saveAndFlush(user);
            userSettingsRepository.saveAndFlush(settings);

            return success;
        }
        //READ
        public User getUserById(int id) {
            if (!userRepository.existsById(id)){
                return null;
            }

            return userRepository.findById(id);

        }

        //UPDATE
        public User updateUser(int id, User user) {
            User u = userRepository.findById(id);
            if (u == null ){
                return null;
            }
            if (userRepository.findById(id) == null) {
                System.out.println();
            }
            userRepository.saveAndFlush(user);
            return userRepository.findById(id);
        }

        //DELETE
        public String deleteUser(int id) {
            if (userRepository.findById(id) == null) {
                return "User " + id + " does not exist";
            }
            String deleteMessage = "User " + id + " has been deleted";
            userRepository.deleteById(id);

            return deleteMessage;
        }
        //LIST
        public List<User> listAllUsers() {
            return userRepository.findAll();
        }


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



        public User getByUsername(String username) {
            return  userRepository.findByUsername(username);
        }

        public String addFriend(int userId, int friendId) {
            User user = userRepository.findById(userId);
            User friend = userRepository.findById(friendId);
            if(user != null && friend != null) {
                user.getFriends().add(friend);
                friend.getFriends().add(user);
                userRepository.saveAndFlush(user);
                return success;
            }

            return failure;
        }

        public List<User> getFriends(int userId) {
            User user = userRepository.findById(userId);
            return new ArrayList<>(user.getFriends());
        }

    }
