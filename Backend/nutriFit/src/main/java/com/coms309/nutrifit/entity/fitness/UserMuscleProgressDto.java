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
	@JsonProperty("muscle")
	private String muscle;

	@JsonProperty(value = "percentage", defaultValue = "0")
	private double percentage;

	@JsonProperty(value = "tier", defaultValue = "0")
	private int tier;

	@JsonProperty(value = "total_progress")
	private double totalProgress;

	@JsonProperty(value = "amount_to_next_tier", defaultValue = "100")
	private double amountToNextTier;

	public void setPercentage(double percentage) {
		if (tier == 0)
		{
			this.totalProgress = percentage;
			this.amountToNextTier = 100 - percentage;
		}
		this.percentage = percentage;
	}

}