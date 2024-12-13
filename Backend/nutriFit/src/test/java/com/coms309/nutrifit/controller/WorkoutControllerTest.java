package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.fitness.Workout;
import com.coms309.nutrifit.entity.fitness.WorkoutSetDto;
import com.coms309.nutrifit.service.ProfileServiceHandler;
import com.coms309.nutrifit.service.WorkoutServiceHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkoutControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WorkoutServiceHandler workoutServiceHandler;

	@MockBean
	private ProfileServiceHandler profileServiceHandler;

	@Autowired
	private ObjectMapper objectMapper;

	private Workout testWorkout;

	private WorkoutSetDto testWorkoutSet;

	private Profile testProfile;

	@BeforeEach
	void setUp() {
		testProfile = new Profile();
		testProfile.setName("testUser");

		testWorkout = new Workout(testProfile);
		testWorkout.setId(1);
		testWorkout.setDateTracked(LocalDate.now());
		testWorkout.setActivities(new ArrayList<>());
		testWorkout.setTotalWeight(100.0);

		testWorkoutSet = new WorkoutSetDto();
		testWorkoutSet.setExerciseName("Bench Press");
		testWorkoutSet.setReps(10);
		testWorkoutSet.setSets(3);
		testWorkoutSet.setWeight(135);
	}

	@Test
	void createWorkout_ShouldReturnNewWorkout() throws Exception {
		LocalDate testDate = LocalDate.now();
		when(workoutServiceHandler.createWorkout("testUser", testDate)).thenReturn(testWorkout);

		mockMvc.perform(post("/workout/testUser/" + testDate))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(testWorkout.getId()))
				.andExpect(jsonPath("$.dateTracked").value(testDate.toString()));
	}

	@Test
	void addActivity_ShouldReturnUpdatedWorkout() throws Exception {
		LocalDate testDate = LocalDate.now();
		when(workoutServiceHandler.addSet(eq(testDate), eq("testUser"), any(WorkoutSetDto.class)))
				.thenReturn(testWorkout);

		mockMvc.perform(post("/workout/add/" + testDate + "/testUser")
				                .contentType(MediaType.APPLICATION_JSON)
				                .content(objectMapper.writeValueAsString(testWorkoutSet)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(testWorkout.getId()));
	}

	@Test
	void getAllWorkouts_ShouldReturnListOfWorkouts() throws Exception {
		when(workoutServiceHandler.getAllWorkouts()).thenReturn(Arrays.asList(testWorkout));

		mockMvc.perform(get("/workout/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(testWorkout.getId()));
	}

	@Test
	void getAllWorkoutsForUser_ShouldReturnUserWorkouts() throws Exception {
		when(workoutServiceHandler.getWorkoutsByUser("testUser")).thenReturn(Arrays.asList(testWorkout));

		mockMvc.perform(get("/workout/testUser"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(testWorkout.getId()));
	}

	@Test
	void getWorkout_ShouldReturnWorkout() throws Exception {
		when(workoutServiceHandler.getWorkoutById(1)).thenReturn(testWorkout);

		mockMvc.perform(get("/workout/id/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(testWorkout.getId()));
	}

	@Test
	void updateWorkout_ShouldReturnUpdatedWorkout() throws Exception {
		when(workoutServiceHandler.updateWorkout(eq(1), any(Workout.class))).thenReturn(testWorkout);

		mockMvc.perform(put("/workout/id/1")
				                .contentType(MediaType.APPLICATION_JSON)
				                .content(objectMapper.writeValueAsString(testWorkout)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(testWorkout.getId()));
	}

	@Test
	void deleteWorkout_ShouldReturnSuccessMessage() throws Exception {
		when(workoutServiceHandler.removeWorkout(1)).thenReturn("Workout with id 1 has been deleted");

		mockMvc.perform(delete("/workout/id/1"))
				.andExpect(status().isOk())
				.andExpect(content().string("Workout with id 1 has been deleted"));
	}
}