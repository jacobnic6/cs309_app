package com.coms309.nutrifit.entity.fitness;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embedded;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Workout}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDto implements Serializable {

        @Embedded
        private  List<WorkoutSetDto> activities;
        @JsonProperty("totalWeight")
        private double totalWeight;

     @JsonProperty(value = "dateTracked")
     @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateTracked;
}