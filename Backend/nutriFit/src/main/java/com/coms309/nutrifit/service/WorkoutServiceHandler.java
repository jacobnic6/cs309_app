package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.Workout;
import com.coms309.nutrifit.entity.WorkoutSet;
import com.coms309.nutrifit.repo.ProfileRepository;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.repo.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    /**
     * Add workout by username workout.
     *
     * @param username the username
     * @param workout  the workout
     * @return the workout
     */
    public Workout addWorkoutByUsername(String username, Workout workout) {

        Profile profile = profileRepository.findByUser(userRepository.findByUsername(username));

       if(!workout.getActivities().isEmpty()){
           workout.updateTotalWeight();
       }


//       List<WorkoutSet> setList = workout.getActivities();
//       for (WorkoutSet set : setList) {
//           double temp = set.getWeightLifted() * set.getRepetitions();
//           weightLifted += temp;
//       }
//       workout.setTotalWeight(weightLifted);


        profile.AddWorkout(workout);
       workout.setProfile(profile);
      return workoutRepository.save(workout);


    }

    /**
     * Gets workouts by user.
     *
     * @param username the username
     * @return the workouts by user
     */
    public List<Workout> getWorkoutsByUser(String username) {

      Profile profile =  profileRepository.findByUser(userRepository.findByUsername(username));
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

    public Workout addSet(LocalDate date, String username, WorkoutSet set) {

        Workout workout = workoutRepository
                .findByProfile_User_UsernameAndDateTracked(username, date).orElse(new Workout());
        Profile profile = profileRepository
                .findByUser_Username(username).orElse(new Profile(userRepository.findByUsername(username)));
        profileRepository.save(profile);

        workout.setProfile(profile);
        workout.setDateTracked(date);
        workout.addActivity(set);
        workout.updateTotalWeight();
        return   workoutRepository.save(workout);

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
