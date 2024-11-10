package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.Workout;
import com.coms309.nutrifit.entity.WorkoutSet;
import com.coms309.nutrifit.repo.WorkoutRepository;
import com.coms309.nutrifit.service.ProfileServiceHandler;
import com.coms309.nutrifit.service.WorkoutServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Workout controller.
 */
@RestController
@RequestMapping("/workout")
public class WorkoutController
    {
        /**
         * The Profile service handler.
         */
        @Autowired
        ProfileServiceHandler profileServiceHandler;
        /**
         * The Workout service handler.
         */
        @Autowired
        WorkoutServiceHandler workoutServiceHandler;


        /**
         * Create workout workout.
         *
         * @param username the username
         * @param workout  the workout
         * @return the workout
         */
        @PostMapping("/{username}")
        public Workout createWorkout(@PathVariable String username, @RequestBody Workout workout){


            return workoutServiceHandler.addWorkoutByUsername(username, workout);
        }

        @PostMapping("/set/{date}/")
        public Workout addActivity(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                   @RequestParam String username, @RequestBody WorkoutSet set){
            return workoutServiceHandler.addSet(date, username, set);
        }

        @GetMapping()
        public List<Workout> getAllWorkouts(){
            return workoutServiceHandler.getAllWorkouts();
        }

        /**
         * Get all workouts list.
         *
         * @param username the username
         * @return the list
         */
        @GetMapping("/{username}")
        public List<Workout> getAllWorkoutsForUser(@PathVariable String username){
        return workoutServiceHandler.getWorkoutsByUser(username);
        }

        /**
         * Get workout workout.
         *
         * @param workoutId the workout id
         * @return the workout
         */
        @GetMapping("/id/{workoutId}")
        public Workout getWorkout(@PathVariable int workoutId){
            return workoutServiceHandler.getWorkoutById(workoutId);
        }

        /**
         * Update workout workout.
         *
         * @param workoutId the workout id
         * @param workout   the workout
         * @return the workout
         */
        @PutMapping("/id/{workoutId}")
        public Workout updateWorkout(@PathVariable int workoutId, @RequestBody Workout workout){
            return workoutServiceHandler.updateWorkout(workoutId, workout);
        }

        /**
         * Delete workout string.
         *
         * @param workoutId the workout id
         * @return the string
         */
        @DeleteMapping("/id/{workoutId}")
        public String deleteWorkout(@PathVariable int workoutId){
            return workoutServiceHandler.removeWorkout(workoutId);
        }

    }
