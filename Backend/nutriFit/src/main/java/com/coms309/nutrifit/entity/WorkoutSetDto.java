package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link WorkoutSet}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class WorkoutSetDto implements Serializable {

 @JsonIgnore
 @JsonProperty("id")
 private int id;

    @JsonProperty(value = "category", defaultValue = "strength")
    private String category;
    @JsonProperty(value = "exerciseName", required = true)
    private  String exerciseName;
    @JsonProperty(value = "weight", defaultValue = "0")
    private int weight;
    @JsonProperty(value ="reps", defaultValue = "0")
    private  int reps;
    @JsonProperty(value ="sets", defaultValue = "0")
    private  int sets;
    @JsonProperty(value ="setTotal", defaultValue = "0")
    private int setTotal;


}