package com.coms309.nutrifit.service;

import com.coms309.nutrifit.dto.UserDto;
import com.coms309.nutrifit.entity.*;
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

@Service
public class UserServiceHandler extends ServiceHandler
    {


        private final UserRepository userRepository;
        private final UserSettingsRepository userSettingsRepository;

        @Autowired
        private FriendRepository friendRepository;

        @Autowired
        private ObjectMapper mapper;




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
            //user.setProfile(new Profile());
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



        public String addFriend(int userId, UserDto friendDto) {
            User user = userRepository.findById(userId);
            UserDto userDto = mapper.convertValue(user, UserDto.class);

            Friend friend = new Friend();
            User user2 = userRepository.findByUsername(friendDto.getUsername());
            User temp1 = user;
            User temp2 = user2;
            if(user.getId() > user2.getId()) {
                temp1 = user2;
                temp2 = user;
            }
            if( !(friendRepository.existsByFirstUserAndSecondUser(temp1, temp2)) ){

                friend.setDateAdded(LocalDate.now());
                friend.setFirstUser(temp1);
                friend.setSecondUser(temp2);
                friendRepository.save(friend);
            }
            if(friendRepository.existsByFirstUserAndSecondUser(temp1, temp2)) {
                return success;
            }


            return failure;
        }

        public List<User> getFriendsById(int userId) {

            User user = userRepository.findById(userId);

            List<Friend> friendsByFirst = friendRepository.findByFirstUser(user);
            List<Friend> friendsBySecond = friendRepository.findBySecondUser(user);
            List<User> friends = new ArrayList<>();

            for( Friend friend : friendsByFirst) {
                friends.add(userRepository.findById(friend.getSecondUser().getId()));
            }
            for( Friend friend : friendsBySecond) {
                friends.add(userRepository.findById(friend.getFirstUser().getId()));
            }

            return friends;
        }

        public List<Friend> getAllFriends() {
            return friendRepository.findAll();
        }

        public List<User> getFriendsByUsername(String username) {
            User user = userRepository.findByUsername(username);
            return getFriendsById(user.getId());
        }


    }
