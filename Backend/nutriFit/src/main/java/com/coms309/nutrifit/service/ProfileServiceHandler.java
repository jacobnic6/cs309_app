package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.ImageData;
import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.repo.ImageRepository;
import com.coms309.nutrifit.repo.ProfileRepository;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.util.ImageUtils;
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
        @Autowired
        ImageRepository imageRepository;


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
                Profile profile = profileRepository.findByUser(user);
                if(profile != null && profile.getProfileImageData() != null){
                    ImageData imageData = profile.getProfileImageData();
                    byte[] img = ImageUtils.decompressImage(imageData.getPictureData());

                }
                return profile;
            }

        public Profile addProfileByName(String username)
            {
              User user =  userRepository.findByUsername(username);
              Profile profile = new Profile(user);
                user.setProfile(profile);
             user =  userRepository.save(user);
              return profileRepository.findByUser(user);
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


        public void assignImage(ImageData upload, String username) {
            User user = userRepository.findByUsername(username);
            Profile profile = profileRepository.findByUser(user);

            ImageData imageData = imageRepository.findByName(upload.getName());

            profile.setProfileImageData(imageData);

            profileRepository.save(profile);

        }
    }
