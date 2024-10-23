package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;

@JsonFormat
@Getter
@Setter
@NoArgsConstructor
public class WeightDTO
    {
        @JsonProperty("weight")
        private double weight;
        @JsonProperty("date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate weightDate;

        public WeightDTO(double weight)
            {
                this.weight = weight;
                this.weightDate = LocalDate.now();
            }

        public WeightDTO(double weight, LocalDate weightDate)
            {
                this.weight = weight;
                this.weightDate = weightDate;
            }
        public WeightDTO(LocalDate weightDate){
            this.weightDate = weightDate;
        }
    }
