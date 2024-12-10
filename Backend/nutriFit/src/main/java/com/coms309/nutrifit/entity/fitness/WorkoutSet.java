package com.coms309.nutrifit.entity.fitness;

import com.coms309.nutrifit.exercises.Exercise;
import com.fasterxml.jackson.annotation.JsonAlias;
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

	@JsonAlias({"exerciseName"})
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

	public WorkoutSet(Workout workout, Exercise exercise, String category, String name, int weight, int sets, int reps) {
		this.workout = workout;
		this.exercise = exercise;
		this.category = category;
		this.exerciseName = name;
		this.weight = weight;
		this.sets = sets;
		this.reps = reps;
		this.primaryProgress = 0;
		this.secondaryProgress = 0;
	}

	public void setExercise(Exercise exercise) {
		this.category = exercise.getCategory().getName();
		this.exerciseName = exercise.getName();
		this.exercise = exercise;

	}

	public void calculateProgress() {

	}

}
