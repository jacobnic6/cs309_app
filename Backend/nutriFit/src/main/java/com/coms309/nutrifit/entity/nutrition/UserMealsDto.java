package com.coms309.nutrifit.entity.nutrition;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ElementCollection;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link com.coms309.nutrifit.entity.nutrition.UserMeals}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMealsDto implements Serializable {

	@EqualsAndHashCode.Exclude
	private int id;

	@JsonProperty("date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@ElementCollection
	private List<MealDto> meals;

	@JsonProperty(value = "nutrientTotals", required = true, defaultValue = "{}")
	private Map<String, Integer> nutrientTotals;

}