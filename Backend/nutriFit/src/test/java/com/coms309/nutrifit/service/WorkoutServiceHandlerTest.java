package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.fitness.UserMuscleProgress;
import com.coms309.nutrifit.entity.fitness.Workout;
import com.coms309.nutrifit.entity.fitness.WorkoutSet;
import com.coms309.nutrifit.entity.fitness.WorkoutSetDto;
import com.coms309.nutrifit.exercises.Category;
import com.coms309.nutrifit.exercises.Exercise;
import com.coms309.nutrifit.exercises.Muscle;
import com.coms309.nutrifit.repo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceHandlerTest {
	// Mock declarations remain the same...
	@Mock
	private WorkoutRepository workoutRepository;

	@Mock
	private WorkoutSetRepository workoutSetRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ProfileRepository profileRepository;

	@Mock
	private ExerciseRepository exerciseRepository;

	@Mock
	private MuscleProgressService muscleProgressService;

	@Mock
	private UserMuscleProgressRepository userMuscleProgressRepository;

	@Mock
	private SocialService socialService;

	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private WorkoutServiceHandler workoutServiceHandler;

	private Profile testProfile;

	private Exercise testExercise;

	private WorkoutSetDto testWorkoutSetDto;

	private Workout testWorkout;

	private Category testCategory;

	@BeforeEach
	void setUp() {
		// Setup remains the same...
		testProfile = new Profile();
		testProfile.setName("testUser");

		Map<String, UserMuscleProgress> muscleProgressMap = new HashMap<>();
		UserMuscleProgress chestProgress = new UserMuscleProgress(testProfile, "Chest");
		muscleProgressMap.put("Chest", chestProgress);
		testProfile.setMuscleProgress(muscleProgressMap);

		testCategory = new Category("Strength");
		testCategory.setId(1);

		testExercise = Exercise.builder()
				               .id(1)
				               .name("Bench Press")
				               .category(testCategory)
				               .primaryMuscles(Arrays.asList(new Muscle("Chest")))
				               .secondaryMuscles(new ArrayList<>())
				               .equipment(new ArrayList<>())
				               .build();

		testWorkoutSetDto = WorkoutSetDto.builder()
				                    .exerciseName("Bench Press")
				                    .category("Strength")
				                    .reps(10)
				                    .sets(3)
				                    .weight(135)
				                    .build();

		testWorkout = new Workout(testProfile);
		testWorkout.setId(1);
		testWorkout.setDateTracked(LocalDate.now());
		testWorkout.setActivities(new ArrayList<>());
	}

	@Test
	void getWorkoutsByUser_ShouldReturnWorkouts() {
		// Mock repository responses
		User testUser = new User();
		when(userRepository.findByUsername("testUser")).thenReturn(testUser);
		when(profileRepository.findByUser(testUser)).thenReturn(testProfile);
		when(workoutRepository.findWorkoutsByProfile(testProfile)).thenReturn(Arrays.asList(testWorkout));

		// Execute test
		List<Workout> result = workoutServiceHandler.getWorkoutsByUser("testUser");

		// Verify results
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(testWorkout.getId(), result.get(0).getId());
		verify(userRepository).findByUsername("testUser");
		verify(profileRepository).findByUser(testUser);
		verify(workoutRepository).findWorkoutsByProfile(testProfile);
	}

	@Test
	void getWorkoutById_ShouldReturnNull_WhenWorkoutDoesNotExist() {
		when(workoutRepository.existsById(999)).thenReturn(false);

		Workout result = workoutServiceHandler.getWorkoutById(999);

		assertNull(result);
		verify(workoutRepository).existsById(999);
		verify(workoutRepository, never()).findById(anyInt());
	}

	@Test
	void createWorkout_ShouldCreateNewWorkout() throws Exception {
		LocalDate testDate = LocalDate.now();

		// Mock repository responses
		when(userRepository.existsByUsername("testUser")).thenReturn(true);
		when(profileRepository.findByName("testUser")).thenReturn(testProfile);
		when(workoutRepository.existsByProfileAndDateTracked(testProfile, testDate)).thenReturn(false);
		when(workoutRepository.saveAndFlush(any(Workout.class))).thenReturn(testWorkout);

		// Execute test
		Workout result = workoutServiceHandler.createWorkout("testUser", testDate);

		// Verify results
		assertNotNull(result);
		assertEquals(testWorkout.getId(), result.getId());
		verify(userRepository).existsByUsername("testUser");
		verify(profileRepository).findByName("testUser");
		verify(workoutRepository).saveAndFlush(any(Workout.class));
	}

	@Test
	void createWorkout_ShouldReturnExistingWorkout_WhenWorkoutExists() throws Exception {
		LocalDate testDate = LocalDate.now();

		// Mock repository responses
		when(userRepository.existsByUsername("testUser")).thenReturn(true);
		when(profileRepository.findByName("testUser")).thenReturn(testProfile);
		when(workoutRepository.existsByProfileAndDateTracked(testProfile, testDate)).thenReturn(true);
		when(workoutRepository.findWorkoutByProfileAndDateTracked(testProfile, testDate)).thenReturn(testWorkout);

		// Execute test
		Workout result = workoutServiceHandler.createWorkout("testUser", testDate);

		// Verify results
		assertNotNull(result);
		assertEquals(testWorkout.getId(), result.getId());
		verify(workoutRepository).findWorkoutByProfileAndDateTracked(testProfile, testDate);
		verify(workoutRepository, never()).saveAndFlush(any(Workout.class));
	}

	@Test
	void createWorkout_ShouldThrowException_WhenUserNotFound() {
		LocalDate testDate = LocalDate.now();

		when(userRepository.existsByUsername("nonexistentUser")).thenReturn(false);

		NullPointerException exception = assertThrows(NullPointerException.class, () ->
				                                                                          workoutServiceHandler.createWorkout("nonexistentUser", testDate));

		assertEquals("User not found", exception.getMessage());
		verify(userRepository).existsByUsername("nonexistentUser");
		verify(workoutRepository, never()).saveAndFlush(any(Workout.class));
	}

	@Test
	void updateWorkout_ShouldReturnNull_WhenWorkoutIsNull() {
		Workout result = workoutServiceHandler.updateWorkout(1, null);

		assertNull(result);
		verify(workoutRepository, never()).existsById(anyInt());
		verify(workoutRepository, never()).saveAndFlush(any(Workout.class));
	}

	@Test
	void removeWorkout_ShouldReturnNotFoundMessage_WhenWorkoutDoesNotExist() {
		when(workoutRepository.existsById(999)).thenReturn(false);

		String result = workoutServiceHandler.removeWorkout(999);

		assertEquals("Workout with id 999 does not exist", result);
		verify(workoutRepository).existsById(999);
		verify(workoutRepository, never()).deleteById(anyInt());
	}

	@Test
	void removeWorkout_ShouldDeleteWorkout_WhenWorkoutExists() {
		when(workoutRepository.existsById(1)).thenReturn(true);

		String result = workoutServiceHandler.removeWorkout(1);

		assertEquals("Workout with id 1 has been deleted", result);
		verify(workoutRepository).existsById(1);
		verify(workoutRepository).deleteById(1);
	}

	@Test
	void addSet_ShouldAddWorkoutSet() throws Exception {
		// Test remains the same...
		LocalDate testDate = LocalDate.now();

		when(userRepository.existsByUsername("testUser")).thenReturn(true);
		when(profileRepository.findByName("testUser")).thenReturn(testProfile);
		when(exerciseRepository.findByNameIgnoreCase("Bench Press")).thenReturn(testExercise);
		when(workoutRepository.existsByProfileAndDateTracked(any(Profile.class), any(LocalDate.class))).thenReturn(true);
		when(workoutRepository.findWorkoutByProfileAndDateTracked(any(Profile.class), any(LocalDate.class))).thenReturn(testWorkout);
		when(workoutSetRepository.findByWorkoutAndExerciseName(any(Workout.class), eq("Bench Press"))).thenReturn(null);

		Workout result = workoutServiceHandler.addSet(testDate, "testUser", testWorkoutSetDto);

		assertNotNull(result);
		verify(workoutRepository, times(2)).findWorkoutByProfileAndDateTracked(any(Profile.class), any(LocalDate.class));
		verify(workoutSetRepository).saveAndFlush(any(WorkoutSet.class));
	}

	@Test
	void addSet_ShouldUpdateExistingSet() throws Exception {
		// Test remains the same...
		LocalDate testDate = LocalDate.now();

		when(userRepository.existsByUsername("testUser")).thenReturn(true);
		when(profileRepository.findByName("testUser")).thenReturn(testProfile);
		when(exerciseRepository.findByNameIgnoreCase("Bench Press")).thenReturn(testExercise);
		when(workoutRepository.existsByProfileAndDateTracked(any(Profile.class), any(LocalDate.class))).thenReturn(true);
		when(workoutRepository.findWorkoutByProfileAndDateTracked(any(Profile.class), any(LocalDate.class))).thenReturn(testWorkout);

		WorkoutSet existingSet = new WorkoutSet();
		existingSet.setExercise(testExercise);
		existingSet.setWorkout(testWorkout);
		when(workoutSetRepository.findByWorkoutAndExerciseName(any(Workout.class), eq("Bench Press"))).thenReturn(existingSet);

		Workout result = workoutServiceHandler.addSet(testDate, "testUser", testWorkoutSetDto);

		assertNotNull(result);
		verify(workoutRepository, times(2)).findWorkoutByProfileAndDateTracked(any(Profile.class), any(LocalDate.class));
		verify(workoutSetRepository).saveAndFlush(any(WorkoutSet.class));
	}

	@Test
	void addSet_ShouldFailWithNullSet() {
		LocalDate testDate = LocalDate.now();

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
				                                                                                  workoutServiceHandler.addSet(testDate, "testUser", null));

		assertEquals("Exercise name cannot be null", exception.getMessage());

		// Verify no repository methods were called
		verifyNoInteractions(userRepository, profileRepository, exerciseRepository,
		                     workoutRepository, workoutSetRepository);
	}

	@Test
	void getWorkoutById_ShouldReturnWorkout() {
		// Test remains the same...
		when(workoutRepository.existsById(1)).thenReturn(true);
		when(workoutRepository.findById(1)).thenReturn(Optional.of(testWorkout));

		Workout result = workoutServiceHandler.getWorkoutById(1);

		assertNotNull(result);
		assertEquals(testWorkout.getId(), result.getId());
		verify(workoutRepository).findById(1);
	}
}