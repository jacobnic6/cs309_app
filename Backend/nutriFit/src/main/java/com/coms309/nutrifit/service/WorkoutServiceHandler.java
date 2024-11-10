package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.*;
import com.coms309.nutrifit.repo.ProfileRepository;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.repo.WorkoutRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Workout service handler.
 */
@Service
public class WorkoutServiceHandler {
    /**
     * The Workout repository.
     */
    @Autowired
    WorkoutRepository workoutRepository;
    /**
     * The User repository.
     */
    @Autowired
    UserRepository userRepository;
    /**
     * The Profile repository.
     */
    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * Add workout by username workout.
     *
     * @param username the username
     * @param date
     * @return the workout
     */
    public Workout createWorkout(String username, LocalDate date) {
    if(!userRepository.existsByUsername(username)){
        throw new NullPointerException("User not found");
    }
    User user = userRepository.findByUsername(username);
    Profile profile = profileRepository.findByUser(user);
    if(workoutRepository.existsByProfileAndDateTracked(profile, date)){
        return workoutRepository.findWorkoutByProfileAndDateTracked(profile, date);
    }

    Workout workout = new Workout(profile);

    profile.addWorkout(workout);





        return workoutRepository.save(workout);


    }

    /**
     * Gets workouts by user.
     *
     * @param username the username
     * @return the workouts by user
     */
    public List<Workout> getWorkoutsByUser(String username) {

        Profile profile = profileRepository.findByUser(userRepository.findByUsername(username));
        return workoutRepository.findWorkoutsByProfile(profile);
    }

    /**
     * Gets workout by id.
     *
     * @param workoutId the workout id
     * @return the workout by id
     */
    public Workout getWorkoutById(int workoutId) {

        if (workoutRepository.existsById(workoutId)) {
            return workoutRepository.findById(workoutId).get();
        }
        return null;
    }

    /**
     * Update workout workout.
     *
     * @param workoutId the workout id
     * @param workout   the workout
     * @return the workout
     */
    public Workout updateWorkout(int workoutId, Workout workout) {

        if (workout == null) {
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

    /**
     * Remove workout string.
     *
     * @param workoutId the workout id
     * @return the string
     */
    public String removeWorkout(int workoutId) {
        if (!workoutRepository.existsById(workoutId)) {
            return "Workout with id " + workoutId + " does not exist";
        }
        workoutRepository.deleteById(workoutId);

        return "Workout with id " + workoutId + " has been deleted";


    }

    public Workout addSet(LocalDate date, String username, WorkoutDto set) throws Exception {
        Profile profile = profileRepository.findByUser(userRepository.findByUsername(username));
        Workout workout;
      if(workoutRepository.existsByProfileAndDateTracked(profile, date)){
          workout = workoutRepository.findWorkoutByProfileAndDateTracked(profile, date);
      }else{
          workout = objectMapper.convertValue(set, Workout.class);
          profile.addWorkout(workout);

      }
//     for ()
//
//        Workout w =  objectMapper.convertValue(set, Workout.class);
//
//      workout.setDateTracked(date);
//      workout.
//        // Fetch user by username
//        User user = userRepository.findByUsername(username);
//        if(!userRepository.existsByUsername(username)){
//            return null;
//        }
//
//        // Fetch profile by user
//        Profile profile = profileRepository.findByUser(user);
//        if (profile == null) {
//            throw new EntityNotFoundException("Profile not found for user");
//        }
//        if(!profile.getWorkouts().isEmpty()){
//
//        }
//
//        // Fetch or create new Workout
//        Workout workout = workoutRepository
//                .findByProfile_User_UsernameAndDateTracked(username, date)
//                .orElseGet(() -> {
//                    Workout newWorkout = new Workout(profile);
//                    if (set != null) {
//                        set.setWorkout(newWorkout);
//                    }
//                    profile.addWorkout(newWorkout);
//                    workoutRepository.save(newWorkout);
//                    return newWorkout;
//                });
//
//        // Add set and update weights if `set` is not null
//        if (set != null) {
//            set.setWorkout(workout);
//            workout.addActivity(set);
//            workout.updateTotalWeight();
//        }

        return workoutRepository.save(workout);

    }

    public Workout getWorkoutsByUserAndDate(String username, LocalDate date) {


        return workoutRepository.findByProfile_User_UsernameAndDateTracked(username, date).get();

    }

    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

//    public Workout addSet(String username, WorkoutSet set) {
//        User user = userRepository.findByUsername(username);
//        if(user == null){
//            return null;
//        }
//        Profile profile = profileRepository.findByUser(user);
//        if(profile == null){
//            profile = new Profile(user);
//        }
//    }
}
