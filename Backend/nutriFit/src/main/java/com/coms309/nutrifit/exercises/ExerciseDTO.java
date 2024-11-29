package com.coms309.nutrifit.exercises;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * The type Exercise dto.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ExerciseDTO {
	@JsonProperty("license")
	private Map<String, String> license;

	@JsonProperty(value = "name", required = true)
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty(value = "category", defaultValue = "strength")
	private String category;

	@JsonProperty(value = "equipment", required = true)
	private List<String> equipment;

	@JsonProperty(value = "instructions", required = true)
	private List<String> instructions;

	@JsonProperty(value = "primary_muscles", required = true)
	private List<String> primaryMuscles;

	@JsonProperty(value = "secondary_muscles", required = true)
	private List<String> secondaryMuscles;

	@JsonProperty("video")
	private String video;

	@JsonProperty("variation_on")
	private List<String> variationOn;

	@JsonProperty("aliases")
	private List<String> aliases;

	@JsonProperty("tips")
	private List<String> tips;

	@JsonProperty("tempo")
	private String tempo;

	@JsonProperty("images")
	private List<String> images;

	@JsonProperty("license_author")
	private String licenseAuthor;

	// Getters and setters
}
