package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link WorkoutSet}
 */
@Value
public class WorkoutSetDto implements Serializable {
    @JsonProperty("category")
    String category;
    @JsonProperty("exerciseName")
    String exerciseName;
    @JsonProperty("weight")
   int weight;
    @JsonProperty("reps")
     int reps;
    @JsonProperty("sets")
    int sets;
    @JsonProperty("setTotal")
     int setTotal;
}