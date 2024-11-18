package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.fitness.UserMuscleProgress;
import com.coms309.nutrifit.util.UserMuscles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMuscleProgressRepository extends JpaRepository<UserMuscleProgress, Integer> {
    boolean findByMuscleAndProfile(UserMuscles muscle, Profile profile);

    List<UserMuscleProgress> findAllByProfile_Name(String username);

    UserMuscleProgress findUserMuscleProgressByProfile_NameAndAndMuscle(String username, String musclename);
}