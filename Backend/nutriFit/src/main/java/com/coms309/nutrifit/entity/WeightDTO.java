package com.coms309.nutrifit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeightDTO
    {
        private double weight;
        private LocalDate weightDate;

        public WeightDTO(double weight){
            this.weight = weight;
            this.weightDate = LocalDate.now();
        }
    }
