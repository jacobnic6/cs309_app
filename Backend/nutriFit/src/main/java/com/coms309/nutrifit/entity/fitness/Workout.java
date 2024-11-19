package com.coms309.nutrifit.entity.fitness;

import com.coms309.nutrifit.entity.Profile;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Workout.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Workout {

    @JsonProperty
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonProperty("activities")
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<WorkoutSet> activities;


    @JsonProperty("totalWeight")
    private double totalWeight;


    @ManyToOne
    @JoinColumn(name = "profile_id")
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


    /**
     * Add activity.
     *
     * @param set the set
     */
    public void addActivity(WorkoutSet set) {
        if (activities == null) {
            activities = new ArrayList<>();
        }
        if (!activities.contains(set)) {
            activities.add(set);
        }

    }

    /**
     * Update total weight.
     */
    public void updateTotalWeight() {
        if (activities != null ) {
            double tempTotal = 0;
            for (WorkoutSet set : activities) {
                double weight = set.getWeight() * set.getReps() * set.getSets();
                tempTotal += weight;
            }
            totalWeight = tempTotal;
        }

    }


}
