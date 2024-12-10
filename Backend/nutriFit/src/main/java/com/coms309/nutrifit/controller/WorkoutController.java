package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.fitness.Workout;
import com.coms309.nutrifit.entity.fitness.WorkoutSetDto;
import com.coms309.nutrifit.service.ProfileServiceHandler;
import com.coms309.nutrifit.service.WorkoutServiceHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Workout controller.
 */
@Tag(name = "Workout Management")
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
	 * @param date     the date
	 *
	 * @return the workout
	 */
	@Operation(summary = "Create a new workout",
			description = "Takes a username and date as input and creates a new blank workout for the specifed user on that date." +
					              " If a workout already exists for that user on the date specified, that workout is returned instead")
	@PostMapping("/{username}/{date}")
	public Workout createWorkout(@PathVariable String username, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

		return workoutServiceHandler.createWorkout(username, date);
	}

	/**
	 * Add activity workout.
	 *
	 * @param date     the date
	 * @param username the username
	 * @param set      the set
	 *
	 * @return the workout
	 *
	 * @throws Exception the exception
	 */
	@Operation(summary = "Add an activity  to a workout",
			description = "Takes a username and date as input and adds a workout activity to the workout on that date." +
					              " If a workout doesn't exist for that user on the date specified, EntityNotFoundException is thrown. If " +
					              "an activity with the same name already exists, that activity's values are updated to match the activity provided.")
	@PostMapping("/add/{date}/{username}")
	public Workout addActivity(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
	                           @PathVariable String username, @RequestBody WorkoutSetDto set) throws Exception
	{

		if (date == null)
		{
			date = LocalDate.now();
		}

		return workoutServiceHandler.addSet(date, username, set);
	}

	/**
	 * Gets all workouts.
	 *
	 * @return the all workouts
	 */
	@Operation(summary = "Get all workouts",
			description = "Returns all workouts for all users")
	@GetMapping("/")
	public List<Workout> getAllWorkouts() {
		return workoutServiceHandler.getAllWorkouts();
	}

	/**
	 * Get all workouts list.
	 *
	 * @param username the username
	 *
	 * @return the list
	 */
	@Operation(summary = "Get all workouts for a specific user",
			description = "Takes a username as input and returns all workouts for that user.")
	@GetMapping("/{username}")
	public List<Workout> getAllWorkoutsForUser(@PathVariable String username) {
		return workoutServiceHandler.getWorkoutsByUser(username);
	}

	/**
	 * Get workout workout.
	 *
	 * @param workoutId the workout id
	 *
	 * @return the workout
	 */
	@Operation(summary = "Get workout by workout id",
			description = "Takes workoutId as input and returns the corresponding workout.")
	@GetMapping("/id/{workoutId}")
	public Workout getWorkout(@PathVariable int workoutId) {
		return workoutServiceHandler.getWorkoutById(workoutId);
	}

	/**
	 * Update workout workout.
	 *
	 * @param workoutId the workout id
	 * @param workout   the workout
	 *
	 * @return the workout
	 */
	@Operation(summary = "Update workout by Id",
			description = "Takes workoutId as input and replaces the workout found with the workout provided.")
	@PutMapping("/id/{workoutId}")
	public Workout updateWorkout(@PathVariable int workoutId, @RequestBody Workout workout) {
		return workoutServiceHandler.updateWorkout(workoutId, workout);
	}

	/**
	 * Delete workout string.
	 *
	 * @param workoutId the workout id
	 *
	 * @return the string
	 */
	@Operation(summary = "Delete workout by Id",
			description = "Takes workoutId as input and deletes the workout found.")
	@DeleteMapping("/id/{workoutId}")
	public String deleteWorkout(@PathVariable int workoutId) {
		return workoutServiceHandler.removeWorkout(workoutId);
	}

}
