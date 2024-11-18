package com.coms309.nutrifit.util;

import com.coms309.nutrifit.exercises.Muscle;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.ToString;

/**
 * The enum User muscles.
 */

public enum UserMuscles {
    /**
     * Quads user muscles.
     */

    @JsonProperty("quads")
  QUADS,
    /**
     * Abs user muscles.
     */
    @JsonProperty("abs")
    ABS,
    /**
     * Biceps user muscles.
     */
    @JsonProperty("biceps")
    BICEPS,
    /**
     * Chest user muscles.
     */
    @JsonProperty("chest")
    CHEST,
    /**
     * Triceps user muscles.
     */
    @JsonProperty("triceps")
    TRICEPS,
    /**
     * Back user muscles.
     */
    @JsonProperty("back")
    BACK,
    /**
     * Hamstrings user muscles.
     */
    @JsonProperty("hamstrings")
    HAMSTRINGS,
    /**
     * Calves user muscles.
     */
    @JsonProperty("calves")
    CALVES;


}
