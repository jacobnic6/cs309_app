package com.coms309.nutrifit.entity;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link WorkoutSet}
 */
@Value
public class WorkoutSetDto implements Serializable {
    String category;
    String exerciseName;
    int weight;
    int reps;
    int sets;
    int setTotal;
}