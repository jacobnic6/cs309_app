package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.Workout;
import com.coms309.nutrifit.repo.WorkoutRepository;
import com.coms309.nutrifit.service.ProfileServiceHandler;
import com.coms309.nutrifit.service.WorkoutServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout")
public class WorkoutController
    {
        @Autowired
        ProfileServiceHandler profileServiceHandler;
        @Autowired
        WorkoutServiceHandler workoutServiceHandler;


        @PostMapping("/{username}")
        public Workout createWorkout(@PathVariable String username, @RequestBody Workout workout){


            return workoutServiceHandler.addWorkoutByUsername(username, workout);
        }


        @GetMapping("/{username}")
        public List<Workout> getAllWorkouts(@PathVariable String username){
        return workoutServiceHandler.getWorkoutsByUser(username);
        }

        @GetMapping("/id/{workoutId}")
        public Workout getWorkout(@PathVariable int workoutId){
            return workoutServiceHandler.getWorkoutById(workoutId);
        }

        @PutMapping("/id/{workoutId}")
        public Workout updateWorkout(@PathVariable int workoutId, @RequestBody Workout workout){
            return workoutServiceHandler.updateWorkout(workoutId, workout);
        }

        @DeleteMapping("/id/{workoutId}")
        public String deleteWorkout(@PathVariable int workoutId){
            return workoutServiceHandler.removeWorkout(workoutId);
        }

    }
