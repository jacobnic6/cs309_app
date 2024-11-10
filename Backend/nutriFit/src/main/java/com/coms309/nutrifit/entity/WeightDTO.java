package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * The type Weight dto.
 */
@JsonFormat
@Getter
@Setter
@NoArgsConstructor
public class WeightDTO {
    @JsonProperty("weight")
    private double weight;
    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate weightDate;

    /**
     * Instantiates a new Weight dto.
     *
     * @param weight the weight
     */
    public WeightDTO(double weight) {
        this.weight = weight;
        this.weightDate = LocalDate.now();
    }

    /**
     * Instantiates a new Weight dto.
     *
     * @param weight     the weight
     * @param weightDate the weight date
     */
    public WeightDTO(double weight, LocalDate weightDate) {
        this.weight = weight;
        this.weightDate = weightDate;
    }

    /**
     * Instantiates a new Weight dto.
     *
     * @param weightDate the weight date
     */
    public WeightDTO(LocalDate weightDate) {
        this.weightDate = weightDate;
    }
}
