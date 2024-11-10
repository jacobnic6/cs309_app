package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @Column(name = "name")
    private String name;


    @ElementCollection
    private Map<String, Integer> muscleProgress;


    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


    private double weight;

    @OneToMany(mappedBy = "profile")
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
        this.user = user;
        this.name = user.getFirstName() + " " + user.getLastName();
        if (user.getBodyWeights().size() != 0) {
            this.weight = user.getBodyWeights().get(0).getWeight();
        }


    }

    /**
     * Add workout.
     *
     * @param workout the workout
     */
    public void AddWorkout(Workout workout) {
        if (this.workouts == null) {
            this.workouts = new ArrayList<>();
        }
        workouts.add(workout);
    }


}
