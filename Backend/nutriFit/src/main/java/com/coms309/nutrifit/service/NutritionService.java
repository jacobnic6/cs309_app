package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.nutrition.*;
import com.coms309.nutrifit.repo.MealRepository;
import com.coms309.nutrifit.repo.UserMealsRepository;
import com.coms309.nutrifit.repo.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Nutrition service.
 */
@Service
public class NutritionService {

	/**
	 * The User repository.
	 */
	private final UserRepository userRepository;

	/**
	 * The User meals repository.
	 */
	private final UserMealsRepository userMealsRepository;

	/**
	 * The Meal repository.
	 */
	private final MealRepository mealRepository;

	/**
	 * The Object mapper.
	 */

	private final ObjectMapper objectMapper;

	/**
	 * Instantiates a new Nutrition service.
	 *
	 * @param userRepository      the user repository
	 * @param userMealsRepository the user meals repository
	 * @param mealRepository      the meal repository
	 * @param objectMapper        the object mapper
	 */
	@Autowired
	public NutritionService(UserRepository userRepository, UserMealsRepository userMealsRepository, MealRepository mealRepository, ObjectMapper objectMapper) {
		this.userRepository = userRepository;
		this.userMealsRepository = userMealsRepository;
		this.mealRepository = mealRepository;
		this.objectMapper = objectMapper;
	}

	/**
	 * Create meal list user meals.
	 *
	 * @param date     the date
	 * @param username the username
	 *
	 * @return the user meals
	 */
	public UserMeals createMealList(LocalDate date, String username) {

		User user = userRepository.findByUsername(username);

		if (user == null)
		{
			return null;
		}

		UserMeals meals = userMealsRepository.findByUserAndDate(user, date);
		if (meals == null)
		{
			meals = new UserMeals();
			meals.setDate(date);
			meals.setUser(user);

		}
		user.getMeals().put(date, meals);
		return userMealsRepository.save(meals);
	}

	/**
	 * Gets meals by date.
	 *
	 * @param date     the date
	 * @param username the username
	 *
	 * @return the meals by date
	 */
	public UserMeals getMealsByDate(LocalDate date, String username) {
		User user = userRepository.findByUsername(username);
		UserMeals meals = userMealsRepository.findByUserAndDate(user, date);
		return userMealsRepository.findByUserAndDate(user, date);
	}

	/**
	 * Gets all user meals.
	 *
	 * @return the all user meals
	 */
	public List<UserMeals> getAllUserMeals() {
		return userMealsRepository.findAll();
	}

	/**
	 * Add meal user meals.
	 *
	 * @param date     the date
	 * @param username the username
	 * @param mealDto  the meal dto
	 *
	 * @return the user meals
	 */
	public UserMeals addMeal(LocalDate date, String username, MealDto mealDto) {

		if (!userRepository.existsByUsername(username))
		{
			throw new RuntimeException("User with username " + username + " does not exist");
		}
		if (mealDto == null)
		{
			throw new IllegalArgumentException("Meal cannot be null");
		}

		Map<String, Integer> nutrients = mealDto.getNutrients();
		Meal meal = objectMapper.convertValue(mealDto, Meal.class);

		UserMeals userMeals = getUserMealList(username, date);
		List<Meal> mealList = userMeals.getMealList();
		if (mealList == null)
		{
			mealList = new ArrayList<>();
		}
		mealList.add(meal);
		userMeals.setMealList(mealList);
		meal.setUserMeals(userMeals);
		Map<String, Integer> totals = userMeals.getNutrientTotals();

		userMeals.setNutrientTotals(totals);
		userMealsRepository.saveAndFlush(userMeals);
		getDailyTotals(date, username);
		return userMealsRepository.findByUserAndDate(userRepository.findByUsername(username), date);
	}

	private UserMeals getUserMealList(String username, LocalDate date) {
		return userMealsRepository
				       .findUserMealsByUser_UsernameAndDate(username, date)
				       .orElse(new UserMeals());
	}

	/**
	 * Gets daily totals.
	 *
	 * @param date     the date
	 * @param username the username
	 *
	 * @return the daily totals
	 */
	public NutrientTotalsDto getDailyTotals(LocalDate date, String username) {
		if (!userRepository.existsByUsername(username))
		{
			throw new RuntimeException("User with username " + username + " does not exist");
		}
		User user = userRepository.findByUsername(username);

		UserMeals meals = userMealsRepository.findByUserAndDate(user, date);
		if (meals == null)
		{
			throw new RuntimeException("User with username " + username + " does not exist");
		}
		Map<String, Integer> nTotals = new HashMap<>();

		nTotals.put("totalCalories", 0);
		nTotals.put("totalCarbs", 0);
		nTotals.put("totalProtein", 0);
		nTotals.put("totalFat", 0);
		nTotals.put("breakfastCalories", 0);
		nTotals.put("lunchCalories", 0);
		nTotals.put("dinnerCalories", 0);
		nTotals.put("snackCalories", 0);
		NutrientTotalsDto totals = new NutrientTotalsDto();
		totals.setDate(date);
		for (Meal meal : meals.getMealList())
		{

			String mealType = meal.getMealType().toLowerCase() + "Calories";
			nTotals.put("totalCalories", meal.getCalories() + nTotals.get("totalCalories"));
			totals.addCalories(meal.getCalories());
			nTotals.put("totalCarbs", meal.getCarbs() + nTotals.get("totalCarbs"));
			totals.addCarbs(meal.getCarbs());
			nTotals.put("totalProtein", meal.getProtein() + nTotals.get("totalProtein"));
			totals.addProtein(meal.getProtein());
			nTotals.put("totalFat", meal.getProtein() + nTotals.get("totalFat"));
			totals.addFats(meal.getFat());
			nTotals.put(mealType, meal.getCalories() + nTotals.get(mealType));
			if (mealType.equals("breakfastCalories"))
			{
				totals.addBreakfastCalories(meal.getCalories());
			} else if (mealType.equals("lunchCalories"))
			{
				totals.addLunchCalories(meal.getCalories());
			} else if (mealType.equals("dinnerCalories"))
			{
				totals.addDinnerCalories(meal.getCalories());
			} else
			{
				totals.addSnackCalories(meal.getCalories());
			}
		}
		meals.setNutrientTotals(nTotals);
		userMealsRepository.save(meals);
		return totals;

	}

	/**
	 * Delete meal by id string.
	 *
	 * @param id     the id
	 * @param mealId the meal id
	 *
	 * @return the string
	 */
	public String deleteMealById(int id, int mealId) {

		UserMeals userMeals = userMealsRepository.findById(id).orElse(null);
		if (userMeals == null)
		{
			return "meal list with id " + id + " does not exist";
		}
		if (!mealRepository.existsById(mealId))
		{
			return "meal with id " + mealId + " does not exist";
		}
		for (Meal meal : userMeals.getMealList())
		{
			if (meal.getId() == mealId)
			{
				userMeals.getMealList().remove(meal);
				userMealsRepository.save(userMeals);

				return "meal with id " + mealId + " deleted";
			}
		}

		return "meal with id " + mealId + " does not exist";

	}

	/**
	 * Delete meal list by id string.
	 *
	 * @param id the id
	 *
	 * @return the string
	 */
	public String deleteMealListById(int id) {
		if (!userMealsRepository.existsById(id))
		{
			return "meal list with id " + id + " does not exist";
		}

		userMealsRepository.deleteById(id);
		return "meal list with id " + id + " deleted";
	}

	/**
	 * Update meal list by date user meals.
	 *
	 * @param id       the id
	 * @param mealsDto the meals dto
	 *
	 * @return the user meals
	 */
	public UserMeals updateMealListByDate(int id, UserMealsDto mealsDto) {
		if (!userMealsRepository.existsById(id))
		{
			return null;
		}
		UserMeals meals = userMealsRepository.findById(id).orElse(null);
		if (meals != null)
		{
			int i = meals.getId();
			UserMeals.UserMealsBuilder builder = UserMeals.builder();
			for (Field field : mealsDto.getClass().getDeclaredFields())
			{

			}
			builder.id(i);

			builder.date(mealsDto.getDate());
			builder.user(meals.getUser());
			builder.mealList(meals.getMealList());
			builder.nutrientTotals(mealsDto.getNutrientTotals());
			UserMeals mealsB = builder.build();
			return userMealsRepository.save(mealsB);

		}
		return null;

	}

}
