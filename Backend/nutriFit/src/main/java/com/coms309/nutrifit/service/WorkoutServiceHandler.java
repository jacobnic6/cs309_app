package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.fitness.*;
import com.coms309.nutrifit.exercises.Exercise;
import com.coms309.nutrifit.exercises.Muscle;
import com.coms309.nutrifit.repo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

	private final MuscleProgressService muscleProgressService;

	private final UserMuscleProgressRepository userMuscleProgressRepository;

	private final SocialService socialService;

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
	public WorkoutServiceHandler(WorkoutRepository workoutRepository, WorkoutSetRepository workoutSetRepository, MuscleProgressService muscleProgressService, UserMuscleProgressRepository userMuscleProgressRepository, SocialService socialService, UserRepository userRepository, ProfileRepository profileRepository, ObjectMapper objectMapper, ExerciseRepository exerciseRepository) {
		this.workoutRepository = workoutRepository;
		this.workoutSetRepository = workoutSetRepository;
		this.muscleProgressService = muscleProgressService;
		this.userMuscleProgressRepository = userMuscleProgressRepository;
		this.socialService = socialService;
		this.userRepository = userRepository;
		this.profileRepository = profileRepository;
		this.objectMapper = objectMapper;
		this.exerciseRepository = exerciseRepository;
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

		Workout workout = createWorkout(username, date);
		WorkoutSet workoutSet = convertSet(workout, set);
		workout.getActivities().add(workoutSet);

		updateMuscleProgressFromSet(workoutSet, username);
		workoutSetRepository.saveAndFlush(workoutSet);

		Profile profile = profileRepository.findByName(username);
		socialService.postWorkout(workoutRepository.saveAndFlush(workout), username);
		return workoutRepository.findWorkoutByProfileAndDateTracked(profile, date);
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

		Profile profile = profileRepository.findByName(username);
		if (workoutRepository.existsByProfileAndDateTracked(profile, date))
		{
			return workoutRepository.findWorkoutByProfileAndDateTracked(profile, date);
		} else
		{
			Workout workout = new Workout(profile);
			workout.setDateTracked(date);

			profile.addWorkout(workout);

			return workoutRepository.saveAndFlush(workout);
		}

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
		getPrimaryProgress(workoutSet);
		getSecondaryProgress(workoutSet);

		Exercise exercise = exerciseRepository.findByNameIgnoreCase(set.getExerciseName());

		workoutSet.setExercise(exercise);
		return workoutSet;
	}

	private Map<String, UserMuscleProgress> updateMuscleProgressFromSet(WorkoutSet workoutSet, String username) {
		double primaryProgress = getPrimaryProgress(workoutSet);
		double secondaryProgress = getSecondaryProgress(workoutSet);

		List<Muscle> secondaryMuscles = workoutSet.getExercise().getSecondaryMuscles();
		List<Muscle> primaryMuscles = workoutSet.getExercise().getPrimaryMuscles();

		updateList(primaryMuscles, username, primaryProgress);
		updateList(secondaryMuscles, username, secondaryProgress);
//		Profile profile = profileRepository.findByName(username);
//		Map<String, UserMuscleProgress> muscleProgress = profile.getMuscleProgress();

		return profileRepository.findByName(username).getMuscleProgress();
	}

	private int getPrimaryProgress(WorkoutSet workoutSet) {
		int primaryDiv = 100;
		int primaryProgress = (workoutSet.getWeight() * workoutSet.getSets() * workoutSet.getReps()) / primaryDiv;
		workoutSet.setPrimaryProgress(primaryProgress);
		return primaryProgress;

	}

	private int getSecondaryProgress(WorkoutSet workoutSet) {
		int secondaryDiv = 200;
		int secondaryProgress = (workoutSet.getWeight() * workoutSet.getSets() * workoutSet.getReps()) / secondaryDiv;
		workoutSet.setSecondaryProgress(secondaryProgress);
		return secondaryProgress;

	}

	private void updateList(List<Muscle> musclesList, String username, double progressAmount) {
		Profile profile = profileRepository.findByName(username);
		Map<String, UserMuscleProgress> muscleProgress = profile.getMuscleProgress();
		for (Muscle muscle : musclesList)
		{

			String muscleName = muscle.getName();
			UserMuscleProgressDto dto = new UserMuscleProgressDto(muscleName, progressAmount);
			if (muscleProgress.containsKey(muscleName))
			{
				UserMuscleProgress oldProgress = muscleProgress.get(muscleName);
				oldProgress.setTotalProgress(oldProgress.getTotalProgress() + progressAmount);
				muscleProgressService.checkValues(oldProgress);

			} else
			{
				UserMuscleProgress progress = objectMapper.convertValue(dto, UserMuscleProgress.class);
				muscleProgressService.checkValues(progress);
				progress.setProfile(profile);
				muscleProgress.put(muscleName, progress);
				userMuscleProgressRepository.saveAndFlush(progress);
			}
			profileRepository.saveAndFlush(profile);

		}
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
