package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.entity.fitness.UserMuscleProgress;
import com.coms309.nutrifit.entity.fitness.Workout;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Profile.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile {

	@Id
	@Column(name = "user_id")
	private int id;

	@NotNull
	@Column(name = "username", unique = true)
	private String name;

	//    @OneToMany(mappedBy = "profile")
//    private List<UserMuscleProgress> muscleProgress;
	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private Map<String, UserMuscleProgress> muscleProgress;

	@OneToOne(optional = false)
	@MapsId
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	@Column
	private double weight;

	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Workout> workouts;

	@Column
	private int height;

	@OneToOne
	@PrimaryKeyJoinColumn
	private ImageData profileImageData;

	/**
	 * Instantiates a new Profile.
	 *
	 * @param user the user
	 */
	public Profile(User user) {
		if (user != null)
		{
		}
		this.user = user;
		this.name = user.getUsername();
		if (user.getBodyWeights() != null && user.getBodyWeights().size() != 0)
		{
			this.weight = user.getBodyWeights().get(user.getBodyWeights().size() - 1).getWeight();
		}

	}

	/**
	 * Add workout.
	 *
	 * @param workout the workout
	 */
	public void addWorkout(Workout workout) {
		if (this.workouts == null)
		{
			this.workouts = new ArrayList<>();
		}
		workouts.add(workout);
	}

}
