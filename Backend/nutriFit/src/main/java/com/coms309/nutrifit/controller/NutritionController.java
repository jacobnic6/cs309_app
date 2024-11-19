package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.nutrition.MealDto;
import com.coms309.nutrifit.entity.nutrition.NutrientTotalsDto;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
import com.coms309.nutrifit.entity.nutrition.UserMealsDto;
import com.coms309.nutrifit.service.NutritionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Nutrition controller.
 */
@Tag(name = "Nutrition Management")
@RestController
@RequestMapping("/meals")
public class NutritionController {

	/**
	 * The Nutrition service.
	 */

	private final NutritionService nutritionService;

	/**
	 * Instantiates a new Nutrition controller.
	 *
	 * @param nutritionService the nutrition service
	 */
	@Autowired
	public NutritionController(NutritionService nutritionService) {
		this.nutritionService = nutritionService;
	}

	/**
	 * Create meal list response entity.
	 *
	 * @param username the username
	 * @param date     the date
	 *
	 * @return the response entity
	 */
//Create
	@Operation(summary = "Create meal list", description = "Creates an empty meal list for the specified user on the specified date.")
	@PostMapping("/{username}/{date}")
	public ResponseEntity<?> createMealList(@PathVariable("username") String username, @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

		UserMeals meals = nutritionService.createMealList(date, username);

		return ResponseEntity.status(HttpStatus.OK).body(meals);

	}

	/**
	 * Add meal response entity.
	 *
	 * @param username the username
	 * @param date     the date
	 * @param mealDto  the mealDto
	 *
	 * @return the response entity
	 */
	@Operation(summary = "Add meal to meal list", description = "Adds a meal to that user's meal list for the specified date.")
	@PostMapping("/food/{username}/{date}")
	public UserMeals addMeal(@PathVariable("username") String username, @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
			, @RequestBody MealDto mealDto)
	{
		if (username == null || mealDto == null)
		{
			throw new RuntimeException("Invalid input ");
		}
		if (date == null)
		{
			date = LocalDate.now();
		}
		return nutritionService.addMeal(date, username, mealDto);
	}

	/**
	 * Gets meals by date.
	 *
	 * @param username the username
	 * @param date     the date
	 *
	 * @return the meals by date
	 */
//Read
	@Operation(summary = "Get user meal list for specific date", description = "Returns the meal list for the specified user and date.")
	@GetMapping("/{username}/{date}")
	public UserMeals getMealsByDate(@PathVariable("username") String username, @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

		return nutritionService.getMealsByDate(date, username);

	}

	/**
	 * Update meal list by id user meals.
	 *
	 * @param id       the id
	 * @param mealsDto the meals dto
	 *
	 * @return the user meals
	 */
//Update
	@Operation(summary = "Update Meal list by list id")
	@PutMapping("/{Id}")
	public UserMeals updateMealListById(@PathVariable("Id") int id, @RequestBody UserMealsDto mealsDto) {
		return nutritionService.updateMealListByDate(id, mealsDto);

	}

	//Delete

	/**
	 * Delete meal list by id string.
	 *
	 * @param id the id
	 *
	 * @return the string
	 */
	@Operation(summary = "Delete meal list by id.", description = "Deletes the entire meal list specified by id.")
	@DeleteMapping("/{id}")
	public String deleteMealListById(@PathVariable int id) {
		return nutritionService.deleteMealListById(id);
	}

	/**
	 * Delete meal by id string.
	 *
	 * @param listId the list id
	 * @param mealId the meal id
	 *
	 * @return the string
	 */
	@Operation(summary = "Delete meal by id.", description = "Deletes specific ")
	@DeleteMapping("/{listId}/meal/{mealId}")
	public String deleteMealById(@PathVariable int listId, @PathVariable int mealId) {
		return nutritionService.deleteMealById(listId, mealId);
	}

	/**
	 * Gets all meals.
	 *
	 * @return the all meals
	 */
//List
	@Operation(summary = "Get all meal lists", description = "Returns all meal lists for all users")
	@GetMapping()
	public List<UserMeals> getAllMealLists() {
		return nutritionService.getAllUserMeals();
	}

	/**
	 * Gets nutrient totals.
	 *
	 * @param date     the date
	 * @param username the username
	 *
	 * @return the nutrient totals
	 */
	@Operation(summary = "Get nutrient totals ", description = "Returns nutrient totals for a user meal list")
	@GetMapping("/totals/{date}/")
	public NutrientTotalsDto getNutrientTotals(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd")
	                                           LocalDate date, @RequestParam String username)
	{

		if (username == null)
		{
			throw new RuntimeException("Invalid input ");
		}
		if (date == null)
		{
			date = LocalDate.now();
		}
		return nutritionService.getDailyTotals(date, username);

	}

}
