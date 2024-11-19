package com.coms309.nutrifit.entity.fitness;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link UserMuscleProgress}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMuscleProgressDto implements Serializable {
    @JsonProperty("muscle")
   private String muscle;
    @JsonProperty(value = "percentage", defaultValue = "0")
   private int percentage;
    @JsonProperty(value = "tier", defaultValue = "0")
   private int tier;
}