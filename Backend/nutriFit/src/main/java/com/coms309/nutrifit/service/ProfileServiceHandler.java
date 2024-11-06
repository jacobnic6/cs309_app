package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.repo.ProfileRepository;
import com.coms309.nutrifit.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProfileServiceHandler
    {
        @Autowired
        private ProfileRepository profileRepository;

        @Autowired
        UserRepository userRepository;


        public Profile addProfile(Profile profile)
            {
                return profileRepository.save(profile);
            }

        public List<Profile> getProfiles()
            {
                return profileRepository.findAll();
            }

        public Profile getUserProfile(String username)
            {
                User user = userRepository.findByUsername(username);
                return user.getProfile();

            }

        public Profile addProfileByName(String username)
            {
              User user =  userRepository.findByUsername(username);
              Profile profile = new Profile(user);
              return profileRepository.save(profile);
            }

        public String updateProfile(String username, Profile profile)
            {
                User user = userRepository.findByUsername(username);
                if(user == null){
                    return "User not found";
                }
                user.setProfile(profile);
                userRepository.save(user);
                return "Profile updated";
            }


    }
