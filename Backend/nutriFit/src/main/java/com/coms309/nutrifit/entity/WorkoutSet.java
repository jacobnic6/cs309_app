package com.coms309.nutrifit.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Workout set.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class WorkoutSet {


        @JsonProperty("setNumber")
        private int setNumber;

        @JsonProperty("exerciseName")
        private String exerciseName;

        @JsonProperty("repetitions")
        private int repetitions;

        @JsonProperty("weightLifted")
        private int weightLifted;

        @JsonProperty("restTime")
        private int restTime;


}
