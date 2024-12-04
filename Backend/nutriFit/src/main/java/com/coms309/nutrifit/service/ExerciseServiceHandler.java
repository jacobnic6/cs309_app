package com.coms309.nutrifit.service;

import com.coms309.nutrifit.exercises.Category;
import com.coms309.nutrifit.exercises.Equipment;
import com.coms309.nutrifit.exercises.Exercise;
import com.coms309.nutrifit.exercises.Muscle;
import com.coms309.nutrifit.repo.CategoryRepository;
import com.coms309.nutrifit.repo.EquipmentRepository;
import com.coms309.nutrifit.repo.ExerciseRepository;
import com.coms309.nutrifit.repo.MuscleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Exercise service handler.
 */
@Service
public class ExerciseServiceHandler {

	/**
	 * The Category repository.
	 */

	private final CategoryRepository categoryRepository;

	private final ExerciseRepository exerciseRepository;

	private final EquipmentRepository equipmentRepository;

	private final ObjectMapper objectMapper;

	private final MuscleRepository muscleRepository;

	@Autowired
	public ExerciseServiceHandler(CategoryRepository categoryRepository, ExerciseRepository exerciseRepository, EquipmentRepository equipmentRepository, ObjectMapper objectMapper, MuscleRepository muscleRepository) {
		this.categoryRepository = categoryRepository;
		this.exerciseRepository = exerciseRepository;
		this.equipmentRepository = equipmentRepository;
		this.objectMapper = objectMapper;
		this.muscleRepository = muscleRepository;
	}

	/**
	 * Add exercise.
	 *
	 * @param exercise the exercise
	 */
	public void addExercise(Exercise exercise) {
		ensureCategoryIsSet(exercise);
		exercise.setEquipment(getEquipmentFromRepository(exercise.getEquipment()));
		exercise.setPrimaryMuscles(getMusclesFromRepository(exercise.getPrimaryMuscles()));
		exercise.setSecondaryMuscles(getMusclesFromRepository(exercise.getSecondaryMuscles()));
	}

	private void ensureCategoryIsSet(Exercise exercise) {
		if (exercise.getCategory() == null)
		{
			exercise.setCategory(new Category("strength"));
		}
		exercise.setCategory(getCategoryFromRepository(exercise.getCategory().getName()));
	}

	private List<Equipment> getEquipmentFromRepository(List<Equipment> equipment) {
		return equipment.stream()
				       .map(equip -> equipmentRepository.getEquipmentByName(equip.getName()))
				       .collect(Collectors.toList());
	}

	private List<Muscle> getMusclesFromRepository(List<Muscle> muscles) {
		return muscles.stream()
				       .map(muscle -> muscleRepository.getByName(muscle.getName()))
				       .collect(Collectors.toList());
	}

	private Category getCategoryFromRepository(String categoryName) {
		return categoryRepository.getByName(categoryName);
	}

	//	public List<Exercise> findExercisesByName(String exerciseName) {
//		return exerciseRepository.findByNameContainsIgnoreCase(exerciseName);
//	}
	public List<String> findExercisesByName(String exerciseName) {
		List<String> exerciseNameList = new ArrayList<>();
		exerciseRepository.findByNameContainsIgnoreCase(exerciseName).forEach(exercise -> exerciseNameList.add(exercise.getName()));
		return exerciseNameList;
	}

//	public List<Exercise> findExercisesByMuscleName(String muscleName) {
//		return exerciseRepository.findByPrimaryMuscles_NameIgnoreCase(muscleName);
//	}

	public List<String> findExercisesByMuscleName(String muscleName) {
		List<String> exerciseNameList = new ArrayList<>();
		exerciseRepository.findByPrimaryMuscles_NameIgnoreCase(muscleName).forEach(exercise -> exerciseNameList.add(exercise.getName()));
		return exerciseNameList;
	}
}
