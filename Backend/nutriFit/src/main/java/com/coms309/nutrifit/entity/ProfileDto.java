package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.dto.ImageDataDto;
import com.coms309.nutrifit.entity.fitness.UserMuscleProgressDto;
import com.coms309.nutrifit.entity.fitness.WorkoutDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link Profile}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileDto implements Serializable {

	@JsonProperty("id")
	private int id;

	@JsonProperty("name")
	@NotNull
	private String name;

	@JsonProperty("muscleProgress")
	private Map<String, UserMuscleProgressDto> muscleProgress;

	@JsonProperty("bio")
	private String bio;

	@JsonProperty("weight")
	private double weight;

	@JsonProperty("workouts")
	private List<WorkoutDto> workouts;

	@JsonProperty("age")
	private int age;

	@JsonProperty("height")
	private int height;

	@JsonProperty("profileImage")
	private ImageDataDto profileImageData;

}