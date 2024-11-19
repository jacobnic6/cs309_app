package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.fitness.Workout;
import com.coms309.nutrifit.entity.fitness.WorkoutSet;
import com.coms309.nutrifit.entity.fitness.WorkoutSetDto;
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

	/**
	 * The Object mapper.
	 */
	@Autowired
	ObjectMapper objectMapper;

	/**
	 * Add workout by username workout.
	 *
	 * @param username the username
	 * @param date     the date
	 *
	 * @return the workout
	 */
	public Workout createWorkout(String username, LocalDate date) {
		if (!userRepository.existsByUsername(username))
		{
			throw new NullPointerException("User not found");
		}
		User user = userRepository.findByUsername(username);
		Profile profile = profileRepository.findByUser(user);
		if (workoutRepository.existsByProfile_NameAndDateTracked(username, date))
		{
			return workoutRepository.findWorkoutByProfileAndDateTracked(profile, date);
		}

		Workout workout = new Workout(profile);

		profile.addWorkout(workout);

		workout.updateTotalWeight();

		return workoutRepository.save(workout);

	}

	/**
	 * Gets workouts by user.
	 *
	 * @param username the username
	 *
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
	 *
	 * @return the workout by id
	 */
	public Workout getWorkoutById(int workoutId) {

		if (workoutRepository.existsById(workoutId))
		{
			return workoutRepository.findById(workoutId).get();
		}
		return null;
	}

	/**
	 * Update workout workout.
	 *
	 * @param workoutId the workout id
	 * @param workout   the workout
	 *
	 * @return the workout
	 */
	public Workout updateWorkout(int workoutId, Workout workout) {

		if (workout == null)
		{
			return null;
		}

		if (workoutRepository.existsById(workoutId))
		{

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
	 *
	 * @return the string
	 */
	public String removeWorkout(int workoutId) {
		if (!workoutRepository.existsById(workoutId))
		{
			return "Workout with id " + workoutId + " does not exist";
		}
		workoutRepository.deleteById(workoutId);

		return "Workout with id " + workoutId + " has been deleted";

	}

	/**
	 * Add set workout.
	 *
	 * @param date     the date
	 * @param username the username
	 * @param set      the set
	 *
	 * @return the workout
	 *
	 * @throws Exception the exception
	 */
	public Workout addSet(LocalDate date, String username, WorkoutSetDto set) throws Exception {
		Profile profile = profileRepository.findByUser(userRepository.findByUsername(username));
		Workout workout;
		if (workoutRepository.existsByProfileAndDateTracked(profile, date))
		{
			workout = workoutRepository.findWorkoutByProfileAndDateTracked(profile, date);
		} else
		{
			throw new EntityNotFoundException("No workout exists for user "
					                                  + username + " on date " + date.toString() + " yet");

		}

		if (workout.getActivities().size() > 0)
		{
			for (WorkoutSet ws : workout.getActivities())
			{
				if (ws.getExerciseName().equalsIgnoreCase(set.getExerciseName()))
				{
					ws.setSets(set.getSets());
					ws.setReps(set.getReps());
					ws.setWeight(set.getWeight());
					ws.setCategory(set.getCategory());
					workout.updateTotalWeight();
					return workoutRepository.saveAndFlush(workout);

				}
			}
		}
		WorkoutSet workoutSet = objectMapper.convertValue(set, WorkoutSet.class);

		workoutSet.setWorkout(workout);
		workout.addActivity(workoutSet);
		workout.updateTotalWeight();

		return workoutRepository.save(workout);

	}

	/**
	 * Gets workouts by user and date.
	 *
	 * @param username the username
	 * @param date     the date
	 *
	 * @return the workouts by user and date
	 */
	public Workout getWorkoutsByUserAndDate(String username, LocalDate date) {

		return workoutRepository.findByProfile_User_UsernameAndDateTracked(username, date).get();

	}

	/**
	 * Gets all workouts.
	 *
	 * @return the all workouts
	 */
	public List<Workout> getAllWorkouts() {
		return workoutRepository.findAll();
	}

}
