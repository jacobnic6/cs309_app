package com.coms309.nutrifit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateDto {

	@JsonProperty("age")
	private int age;

	@JsonProperty("height")
	private int height;

	@JsonProperty("fitness_goal")
	private String fitnessGoal;

	@JsonProperty("bio")
	private String bio;

}