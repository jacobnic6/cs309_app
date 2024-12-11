package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.nutrition.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Optional;

/**
 * The interface Meal repository.
 */
public interface MealRepository extends JpaRepository<Meal, Integer> {
	/**
	 * Find first by user meals user username and user meals date and meal type all ignore case order by user meals user username asc user meals date asc user meals meal list meal type asc optional.
	 *
	 * @param username the username
	 * @param date     the date
	 * @param mealType the meal type
	 *
	 * @return the optional
	 */
	@NonNull
	Optional<Meal> findFirstByUserMeals_User_UsernameAndUserMeals_DateAndMealTypeAllIgnoreCaseOrderByUserMeals_User_UsernameAscUserMeals_DateAscUserMeals_MealList_MealTypeAsc(
			@NonNull String username, @NonNull LocalDate date, @NonNull String mealType);

}