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

	@MapKey
	@JsonProperty("muscle")
	private String muscle;

	//xp to next level
	@JsonProperty(value = "percentage", defaultValue = "0")
	private double percentage;

	//lvl
	@JsonProperty(value = "tier")
	private int tier;

	@JsonProperty(value = "total_progress")
	private double totalProgress;

	@ManyToOne
	@JoinColumn(name = "profile_id", referencedColumnName = "user_id")
	@JsonIgnore
	private Profile profile;

}
