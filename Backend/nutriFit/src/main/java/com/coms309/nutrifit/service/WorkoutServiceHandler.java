package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.Workout;
import com.coms309.nutrifit.entity.WorkoutSet;
import com.coms309.nutrifit.repo.ProfileRepository;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.repo.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WorkoutServiceHandler {
    @Autowired
    WorkoutRepository workoutRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProfileRepository profileRepository;

    public Workout addWorkoutByUsername(String username, Workout workout) {

        Profile profile = profileRepository.findByUser(userRepository.findByUsername(username));

        double weightLifted = 0;

       List<WorkoutSet> setList = workout.getActivities();
       for (WorkoutSet set : setList) {
           double temp = set.getWeightLifted() * set.getRepetitions();
           weightLifted += temp;
       }
       workout.setTotalWeight(weightLifted);


        profile.AddWorkout(workout);
       workout.setProfile(profile);
      return workoutRepository.save(workout);


    }

    public List<Workout> getWorkoutsByUser(String username) {

      Profile profile =  profileRepository.findByUser(userRepository.findByUsername(username));
        return workoutRepository.findWorkoutsByProfile(profile);
    }

    public Workout getWorkoutById(int workoutId) {

        if (workoutRepository.existsById(workoutId)) {
            return workoutRepository.findById(workoutId).get();
        }
        return null;
    }

    public Workout updateWorkout(int workoutId, Workout workout) {

        if(workout == null) {
            return null;
        }

        if (workoutRepository.existsById(workoutId)) {

            Workout oldWorkout = workoutRepository.findById(workoutId).get();
            oldWorkout.setActivities(workout.getActivities());
            oldWorkout.setDateTracked(LocalDate.now());
            oldWorkout.setActivities(workout.getActivities());
            oldWorkout.updateTotalWeight();
            workoutRepository.saveAndFlush(oldWorkout);


        }
        return workoutRepository.findById(workoutId).get();
    }

    public String removeWorkout(int workoutId) {
        if (!workoutRepository.existsById(workoutId)) {
            return "Workout with id " + workoutId + " does not exist";
        }
       workoutRepository.deleteById(workoutId);

           return "Workout with id " + workoutId + " has been deleted";


    }
}
