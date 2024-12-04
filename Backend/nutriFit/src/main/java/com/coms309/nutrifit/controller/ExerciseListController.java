package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.service.ExerciseServiceHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Exercise Management")
@RestController
@RequestMapping("/exercise")
public class ExerciseListController {

	private final ExerciseServiceHandler exerciseServiceHandler;

	@Autowired
	public ExerciseListController(ExerciseServiceHandler exerciseServiceHandler) {
		this.exerciseServiceHandler = exerciseServiceHandler;
	}

	@GetMapping("/name/{exerciseName}")
	public List<String> getByExerciseName(@PathVariable String exerciseName) {
		return exerciseServiceHandler.findExercisesByName(exerciseName);
	}

	//	@GetMapping("/muscle/{muscleName}")
//	public List<Exercise> getByMuscleName(@PathVariable String muscleName) {
//		return exerciseServiceHandler.findExercisesByMuscleName(muscleName);
//	}
	@GetMapping("/muscle/{muscleName}")
	public List<String> getByMuscleName(@PathVariable String muscleName) {
		return exerciseServiceHandler.findExercisesByMuscleName(muscleName);
	}
}
