package com.coms309.nutrifit.exercises;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Exercises.
 */
@Getter
@Setter
@AllArgsConstructor
public class Exercises {

	/**
	 * The Categories.
	 */
	private List<Category> categories;

	/**
	 * The Equipment.
	 */
	private List<Equipment> equipment;

	/**
	 * The Muscles.
	 */
	private List<Muscle> muscles;

	/**
	 * The Muscle groups.
	 */
	private List<MuscleGroup> muscleGroups;

	/**
	 * The Exercises.
	 */
	private List<Exercise> exercises;

	private int id;

	/**
	 * Instantiates a new Exercises.
	 */
	public Exercises() {
		categories = new ArrayList<>();
		equipment = new ArrayList<>();
		muscles = new ArrayList<>();
		muscleGroups = new ArrayList<>();
		exercises = new ArrayList<>();
	}

	public void addCategory(Category category) {
		categories.add(category);
	}

	public void addEquipment(Equipment equipment) {
		this.equipment.add(equipment);
	}

	public void addMuscle(Muscle muscle) {
		muscles.add(muscle);
	}

	public void addMuscleGroup(MuscleGroup muscleGroup) {
		muscleGroups.add(muscleGroup);
	}

	public void addMuscleToExistingGroup(String groupName, Muscle muscle) {
		for (MuscleGroup group : muscleGroups)
		{
			if (group.getGroupName().equals(groupName))
			{
				muscle.setMuscleGroup(group);
				group.addMuscle(muscle);
				return;
			}
		}

	}

	public void addExercise(Exercise exercise) {

		exercises.add(exercise);
	}

}
