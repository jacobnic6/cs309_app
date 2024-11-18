package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.fitness.Workout;
import com.coms309.nutrifit.entity.fitness.WorkoutDto;
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
public class WorkoutController {
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
     * Create workout.
     *
     * @param username the username
     * @return the workout
     */

    @PostMapping("/{username}/{date}")
    public Workout createWorkout(@PathVariable String username, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {


        return workoutServiceHandler.createWorkout(username, date);
    }

    @PostMapping("/add/{date}/{username}")
    public Workout addActivity(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") String date,
                               @PathVariable String username, @RequestBody WorkoutDto set) throws Exception {
        LocalDate d = LocalDate.parse(date);
        if (d == null) {
            d= LocalDate.now();
        }


            return workoutServiceHandler.addSet(d, username, set);
    }

//    @PostMapping("/addSet/{date}/{username}")
//    public Workout addActivity(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
//                               @PathVariable String username, @RequestBody WorkoutSet set) {
//
//
//        return workoutServiceHandler.addSet(date, username, set);
//    }

    @GetMapping("/")
    public List<Workout> getAllWorkouts() {
        return workoutServiceHandler.getAllWorkouts();
    }

    /**
     * Get all workouts list.
     *
     * @param username the username
     * @return the list
     */
    @GetMapping("/{username}")
    public List<Workout> getAllWorkoutsForUser(@PathVariable String username) {
        return workoutServiceHandler.getWorkoutsByUser(username);
    }

    /**
     * Get workout workout.
     *
     * @param workoutId the workout id
     * @return the workout
     */
    @GetMapping("/id/{workoutId}")
    public Workout getWorkout(@PathVariable int workoutId) {
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
    public Workout updateWorkout(@PathVariable int workoutId, @RequestBody Workout workout) {
        return workoutServiceHandler.updateWorkout(workoutId, workout);
    }

    /**
     * Delete workout string.
     *
     * @param workoutId the workout id
     * @return the string
     */
    @DeleteMapping("/id/{workoutId}")
    public String deleteWorkout(@PathVariable int workoutId) {
        return workoutServiceHandler.removeWorkout(workoutId);
    }

}
