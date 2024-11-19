package com.coms309.nutrifit.entity.nutrition;

import com.coms309.nutrifit.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type User meals.
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMeals {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@MapKey
	private LocalDate date;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@JsonIgnore
	private User user;

	@OneToMany(mappedBy = "userMeals", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Meal> mealList = new ArrayList<>();

	@ElementCollection
	private Map<String, Integer> nutrientTotals = new HashMap<>();

	/**
	 * Add nutrient.
	 *
	 * @param nutrient the nutrient
	 * @param amount   the amount
	 */
	public void addNutrient(String nutrient, int amount) {
		if (nutrientTotals.containsKey(nutrient))
		{
			nutrientTotals.put(nutrient, nutrientTotals.get(nutrient) + amount);
		} else
		{
			nutrientTotals.put(nutrient, amount);
		}
	}

}
