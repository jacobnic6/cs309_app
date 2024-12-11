package com.coms309.nutrifit.entity.fitness;

import com.coms309.nutrifit.entity.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type User muscle progress.
 */
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMuscleProgress {

	@JsonProperty(value = "amount_to_next_tier", defaultValue = "100")
	private double amountToNextTier;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "muscle", nullable = false)
	@JsonProperty("muscle")
	private String muscle;

	//xp to next level
	@JsonProperty(value = "percentage", defaultValue = "0")
	private double percentage;

	//lvl
	@JsonProperty(value = "tier", defaultValue = "0")
	private int tier;

	@JsonProperty(value = "total_progress", defaultValue = "0")
	private double totalProgress;

	@ManyToOne
	@JoinColumn(name = "profile_id", referencedColumnName = "user_id")
	@JsonIgnore
	private Profile profile;

	public UserMuscleProgress(Profile profile, String muscle) {
		this.profile = profile;
		this.muscle = muscle;
		this.percentage = 0;
		this.tier = 0;
		this.totalProgress = 0;
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
