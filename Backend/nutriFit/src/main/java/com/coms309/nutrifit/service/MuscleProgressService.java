package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.UserMuscleProgress;
import com.coms309.nutrifit.entity.UserMuscleProgressDto;
import com.coms309.nutrifit.repo.ProfileRepository;
import com.coms309.nutrifit.repo.UserMuscleProgressRepository;
import com.coms309.nutrifit.util.UserMuscles;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MuscleProgressService {

    private final ProfileServiceHandler profileServiceHandler;
    private final UserMuscleProgressRepository userMuscleProgressRepository;
private final ObjectMapper objectMapper;
private final ProfileRepository profileRepository;
    @Autowired
    public MuscleProgressService(ProfileServiceHandler profileServiceHandler, UserMuscleProgressRepository userMuscleProgressRepository, ObjectMapper objectMapper, ProfileRepository profileRepository) {
        this.profileServiceHandler = profileServiceHandler;
        this.userMuscleProgressRepository = userMuscleProgressRepository;

        this.objectMapper = objectMapper;
        this.profileRepository = profileRepository;
    }


    public String createProgress(UserMuscleProgressDto progressDto, String username) {

    Profile profile =  profileServiceHandler.getUserProfile(username);
    if(profile != null) {
        profile = profileServiceHandler.createProfileByName(username);
    }
        if(UserMuscles.valueOf(progressDto.getMuscle().toUpperCase()) ==null){
            return "invalid muscle name";
        }
        UserMuscleProgress progress = new UserMuscleProgress();
        String muscleName = progressDto.getMuscle().toUpperCase();
        if(UserMuscles.valueOf(muscleName) != null){
            progress = objectMapper.convertValue(progressDto, UserMuscleProgress.class);
            progress.setProfile(profile);
            Map<String, UserMuscleProgress> progressMap = profile.getMuscleProgress();
            if(progressMap.containsKey(progress.getMuscle())){
                UserMuscleProgress existingProgress = progressMap.get(progress.getMuscle());
                existingProgress.updateProgress(progress.getPercentage(), progress.getTier());
                profile.setMuscleProgress(progressMap);
                userMuscleProgressRepository.save(existingProgress);
                return "Muscle " + progress.getMuscle()+ "progress updated";
            }

            progressMap.put(progress.getMuscle(), progress);
            profile.setMuscleProgress(progressMap);



            userMuscleProgressRepository.save(progress);

            return "Muscle " + progress.getMuscle()+ "progress created";
        }


    return "muscle progress not created";}
}
