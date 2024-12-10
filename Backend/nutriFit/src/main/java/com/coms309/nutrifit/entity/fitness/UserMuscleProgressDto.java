package com.coms309.nutrifit.entity.fitness;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link UserMuscleProgress}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMuscleProgressDto implements Serializable {
	@JsonProperty(value = "muscle", required = true)
	private String muscle;

	@JsonProperty(value = "percentage", defaultValue = "0")
	private double percentage;

	@JsonProperty(value = "tier", defaultValue = "0")
	private int tier;

	@JsonProperty(value = "total_progress", defaultValue = "0")
	private double totalProgress;

	@JsonProperty(value = "amount_to_next_tier", defaultValue = "100")
	private double amountToNextTier;

	public UserMuscleProgressDto(String muscle) {
		this.muscle = muscle;

	}

	public UserMuscleProgressDto(String muscleName, double primaryProgress) {
		this.muscle = muscleName;
		this.percentage = primaryProgress;
		this.tier = 0;
		this.totalProgress = primaryProgress;
		this.amountToNextTier = 100;
	}

	public double getAmountToNextTier() {
		if (tier == 0)
		{
			amountToNextTier = 100;
		} else
		{
			amountToNextTier = ((tier * 1.2 * 100) + 100);
		}
		return amountToNextTier;
	}

}