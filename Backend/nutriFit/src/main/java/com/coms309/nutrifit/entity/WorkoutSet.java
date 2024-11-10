package com.coms309.nutrifit.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

/**
 * Acts as an exercise
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkoutSet {

    @Id
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "workout_id", referencedColumnName = "id")
    private Workout workout;

    @JsonProperty("category")
    private String category;

    @JsonProperty("exerciseName")
    private String exerciseName;

    @JsonProperty("weight")
    private int weight;

    @JsonProperty("sets")
    private int sets;

    @JsonProperty("reps")
    private int reps;





}
