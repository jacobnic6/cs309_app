package com.coms309.nutrifit.entity.fitness;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Acts as an exercise
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkoutSet {

	@JsonIgnore
	@Id
	private int id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "workout_id", referencedColumnName = "id")
	private Workout workout;

	@JsonProperty("category")
	private String category;

	@JsonProperty("exerciseName")
	private String exerciseName;

	@JsonProperty(value = "weight", defaultValue = "0")
	private int weight;

	@JsonProperty(value = "sets", defaultValue = "0")
	private int sets;

	@JsonProperty(value = "reps", defaultValue = "0")
	private int reps;

}
