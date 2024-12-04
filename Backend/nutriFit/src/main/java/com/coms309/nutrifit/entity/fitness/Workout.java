package com.coms309.nutrifit.entity.fitness;

import com.coms309.nutrifit.entity.Profile;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Workout.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Workout {

	@JsonProperty
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@JsonProperty("activities")
	@OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<WorkoutSet> activities;

	@JsonProperty("totalWeight")
	private double totalWeight;

	@ManyToOne(optional = false)
	@JoinColumn(name = "profile_id", nullable = false, updatable = false)
	@JsonIgnore
	private Profile profile;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateTracked;

	/**
	 * Instantiates a new Workout.
	 *
	 * @param profile the profile
	 */
	public Workout(Profile profile) {

		this.profile = profile;
		activities = new ArrayList<>();
		this.dateTracked = LocalDate.now();

	}

	/**
	 * Instantiates a new Workout.
	 */

	public double getTotalWeight() {
		double tempTotal = 0;
		if (activities != null)
		{

			for (WorkoutSet set : activities)
			{
				double weight = set.getWeight() * set.getReps() * set.getSets();
				tempTotal += weight;
			}
			//totalWeight = tempTotal;
		}
		return tempTotal;
	}
}
