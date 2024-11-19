package com.coms309.nutrifit.entity.nutrition;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * The type Nutrient totals dto.
 */
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

	/**
	 * Add calories.
	 *
	 * @param calories the calories
	 */
	public void addCalories(int calories) {
		this.totalCalories += calories;
	}

	/**
	 * Add protein.
	 *
	 * @param protein the protein
	 */
	public void addProtein(int protein) {
		this.totalProtein += protein;
	}

	/**
	 * Add carbs.
	 *
	 * @param carbs the carbs
	 */
	public void addCarbs(int carbs) {
		this.totalCarbs += carbs;
	}

	/**
	 * Add fats.
	 *
	 * @param fats the fats
	 */
	public void addFats(int fats) {
		this.totalFat += fats;
	}

	/**
	 * Add breakfast calories.
	 *
	 * @param calories the calories
	 */
	public void addBreakfastCalories(int calories) {
		this.breakfastCalories += calories;
	}

	/**
	 * Add lunch calories.
	 *
	 * @param calories the calories
	 */
	public void addLunchCalories(int calories) {
		this.lunchCalories += calories;
	}

	/**
	 * Add dinner calories.
	 *
	 * @param calories the calories
	 */
	public void addDinnerCalories(int calories) {
		this.dinnerCalories += calories;
	}

	/**
	 * Add snack calories.
	 *
	 * @param calories the calories
	 */
	public void addSnackCalories(int calories) {
		this.snacksCalories += calories;
	}

}
