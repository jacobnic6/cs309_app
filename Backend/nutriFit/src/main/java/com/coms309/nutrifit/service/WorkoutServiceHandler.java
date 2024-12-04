package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.fitness.Workout;
import com.coms309.nutrifit.entity.fitness.WorkoutSet;
import com.coms309.nutrifit.entity.fitness.WorkoutSetDto;
import com.coms309.nutrifit.exercises.Exercise;
import com.coms309.nutrifit.repo.*;
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

	private final WorkoutRepository workoutRepository;

	private final WorkoutSetRepository workoutSetRepository;

	/**
	 * The User repository.
	 */

	private final UserRepository userRepository;

	/**
	 * The Profile repository.
	 */

	private final ProfileRepository profileRepository;

	/**
	 * The Object mapper.
	 */

	private final ObjectMapper objectMapper;

	private final ExerciseRepository exerciseRepository;

	@Autowired
	public WorkoutServiceHandler(WorkoutRepository workoutRepository, WorkoutSetRepository workoutSetRepository, UserRepository userRepository, ProfileRepository profileRepository, ObjectMapper objectMapper, ExerciseRepository exerciseRepository) {
		this.workoutRepository = workoutRepository;
		this.workoutSetRepository = workoutSetRepository;
		this.userRepository = userRepository;
		this.profileRepository = profileRepository;
		this.objectMapper = objectMapper;
		this.exerciseRepository = exerciseRepository;
	}

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

		//workout.updateTotalWeight();

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
			//oldWorkout.updateTotalWeight();
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
		if (set == null || set.getExerciseName().isEmpty())
		{
			throw new IllegalArgumentException("Exercise name cannot be null");
		}

		Workout workout;
		if (workoutRepository.existsByProfile_NameAndDateTracked(username, date))
		{
			workout = workoutRepository.findByProfile_NameAndDateTracked(username, date);
		} else
		{
			throw new EntityNotFoundException("No workout exists for user " + username + " on date " + date.toString() + " yet");
		}

		workoutSetRepository.save(convertSet(workout, set));

		return workoutRepository.saveAndFlush(workout);

	}

	private WorkoutSet convertSet(Workout workout, WorkoutSetDto set) {
		WorkoutSet workoutSet = workoutSetRepository.findByWorkoutAndExerciseName(workout, set.getExerciseName());
		if (workoutSet == null)
		{
			workoutSet = new WorkoutSet();
			workoutSet.setWorkout(workout);
		}

		workoutSet.setSets(set.getSets());
		workoutSet.setReps(set.getReps());
		workoutSet.setWeight(set.getWeight());

		Exercise exercise = exerciseRepository.findByNameIgnoreCase(set.getExerciseName());

		workoutSet.setExercise(exercise);
		return workoutSet;
	}

	private void calculateProgress(Workout workout) {

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
