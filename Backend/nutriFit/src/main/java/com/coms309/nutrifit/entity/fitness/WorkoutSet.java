package com.coms309.nutrifit.entity.fitness;

import com.coms309.nutrifit.exercises.Exercise;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Acts as an exercise
 */
@Builder
@Data
//@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkoutSet {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "workout_id", referencedColumnName = "id")
	private Workout workout;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "exercise", referencedColumnName = "id")
	private Exercise exercise;

	@JsonProperty("category")
	private String category;

	@JsonProperty(value = "name", required = true)
	private String exerciseName;

	@JsonProperty(value = "weight", defaultValue = "0")
	private int weight;

	@JsonProperty(value = "sets", defaultValue = "0")
	private int sets;

	@JsonProperty(value = "reps", defaultValue = "0")
	private int reps;

	@JsonProperty(value = "primaryProgress", defaultValue = "0")
	private int primaryProgress;

	@JsonProperty(value = "secondaryProgress", defaultValue = "0")
	private int secondaryProgress;

	public int getPrimaryProgress() {
		int primaryDiv = 100;

		return (weight * reps * sets) / primaryDiv;

	}

	public int getSecondaryProgress() {
		int secondaryDiv = 200;

		return (weight * reps * sets) / secondaryDiv;

	}

	public void setExercise(Exercise exercise) {
		this.category = exercise.getCategory().getName();
		this.exerciseName = exercise.getName();
		this.exercise = exercise;

	}

}
