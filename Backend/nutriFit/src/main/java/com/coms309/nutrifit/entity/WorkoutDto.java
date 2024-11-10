package com.coms309.nutrifit.entity;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link Workout}
 */
@Value
public class WorkoutDto implements Serializable {
    List<WorkoutSetDto> activities;
    double totalWeight;
    LocalDate dateTracked;
}