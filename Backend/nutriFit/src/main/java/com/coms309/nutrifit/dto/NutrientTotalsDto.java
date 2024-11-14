package com.coms309.nutrifit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NutrientTotalsDto {

    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonProperty(value = "totalCalories", defaultValue = "0")
    private int totalCalories;

    @JsonProperty(value = "totalProtein", defaultValue = "0")
    private int totalProtein;

    @JsonProperty(value = "totalCarbs", defaultValue = "0")
    private int totalCarbs;

    @JsonProperty(value = "totalFat", defaultValue = "0")
    private int totalFat;

    @JsonProperty(value = "breakfastCalories", defaultValue = "0")
    private int breakfastCalories;

    @JsonProperty(value = "lunchCalories", defaultValue = "0")
    private int lunchCalories;

    @JsonProperty(value = "dinnerCalories", defaultValue = "0")
    private int dinnerCalories;

    @JsonProperty(value = "snackCalories", defaultValue = "0")
    private int snacksCalories;


    public void addCalories(int calories) {
        this.totalCalories += calories;
    }

    public void addProtein(int protein) {
        this.totalProtein += protein;
    }

    public void addCarbs(int carbs) {
        this.totalCarbs += carbs;
    }

    public void addFats(int fats) {
        this.totalFat += fats;
    }

    public void addBreakfastCalories(int calories) {
        this.breakfastCalories += calories;
    }

    public void addLunchCalories(int calories) {
        this.lunchCalories += calories;
    }

    public void addDinnerCalories(int calories) {
        this.dinnerCalories += calories;
    }

    public void addSnackCalories(int calories) {
        this.snacksCalories += calories;
    }

}
