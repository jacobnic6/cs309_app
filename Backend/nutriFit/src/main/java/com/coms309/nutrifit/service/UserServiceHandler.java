package com.coms309.nutrifit.service;

import com.coms309.nutrifit.users.User;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.users.UserSettings;
import com.coms309.nutrifit.repo.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            String deleteMessage = "User " + id + " has been deleted";
            userRepository.deleteById(id);

            return deleteMessage;
        }
        //LIST
        public List<User> listAllUsers() {
            return userRepository.findAll();
        }


        public String updateUserSettings(int userId, int settingsId, UserSettings userSettings) {
            User u = userRepository.findById(userId);
            UserSettings settings = userSettingsRepository.findById(settingsId);
            if (u == null || settings == null) {
                return failure;
            }
            u.setSettings(userSettings);

            userRepository.saveAndFlush(u);


            return success;

        }
    }
