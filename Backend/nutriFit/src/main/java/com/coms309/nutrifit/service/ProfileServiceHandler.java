package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.ImageData;
import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.exercises.Muscle;
import com.coms309.nutrifit.exercises.MuscleGroup;
import com.coms309.nutrifit.repo.*;
import com.coms309.nutrifit.util.ImageUtils;
import com.coms309.nutrifit.util.UserMuscles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfileServiceHandler
    {
        @Autowired
        private ProfileRepository profileRepository;

        @Autowired
        UserRepository userRepository;
        @Autowired
        ImageRepository imageRepository;
        @Autowired
        MuscleRepository muscleRepository;
        @Autowired
        MuscleGroupRepository groupRepository;
        @Autowired
        private MuscleGroupRepository muscleGroupRepository;

        public Profile addProfile(Profile profile)
            {

                if(profileRepository.findByUser(profile.getUser()) == null){
                    profile.setMuscleProgress(getMusclesMap());
                    return profileRepository.save(profile);
                }
                return profileRepository.findByUser(profile.getUser());

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

        public Profile createProfileByName(String username)
            {


              User user =  userRepository.findByUsername(username);
                Profile profile = user.getProfile();
              if(user == null ){
                  return null;
              }

              if(profile == null){
                  profile = new Profile(user);
                    profile.setMuscleProgress(getMusclesMap());
                  user.setProfile(profile);
                   userRepository.saveAndFlush(user);
              }else {

              }


              return userRepository.findByUsername(username).getProfile();
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

        private Map<String, Integer> getMusclesMap(){
            Map<String, Integer> muscleMap = new HashMap<>();
            UserMuscles muscles;

            for(UserMuscles muscle : UserMuscles.values()){

                muscleMap.put(muscle.name(), 0);
            }
            return muscleMap;

        }


//        private Map<String, Integer> getMusclesMap(){
//            List<Muscle> muscles = muscleRepository.findAll();
//            Map<String, Integer> map = new HashMap<>();
//            for (Muscle muscle : muscles){
//                map.put(muscle.getName(), 0);
//            }
//            return map;
//        }
//        private Map<String, Integer> getMuscleGroupMap(){
//            List<MuscleGroup> muscles = muscleGroupRepository.findAll();
//            Map<String, Integer> map = new HashMap<>();
//            for (MuscleGroup group: muscles){
//                map.put(group.getGroupName(), 0);
//            }
//            return map;
//        }
    }
