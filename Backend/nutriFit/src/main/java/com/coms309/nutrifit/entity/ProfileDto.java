package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.dto.ImageDataDto;
import com.coms309.nutrifit.entity.fitness.UserMuscleProgressDto;
import com.coms309.nutrifit.entity.fitness.WorkoutDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

	@NotNull
	private String name;

	private Map<String, UserMuscleProgressDto> muscleProgress;

	private double weight;

	private List<WorkoutDto> workouts;

	private int height;

	private ImageDataDto profileImageData;
}