package com.coms309.nutrifit.entity;


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
@Embeddable
public class WorkoutSet {




        @JsonProperty("category")
        private String category;

        @JsonProperty("exerciseName")
        private String exerciseName;

        @JsonProperty("weight")
        private int weight;

        @JsonProperty("reps")
        private int reps;


        @JsonProperty("sets")
        private int sets;


}
