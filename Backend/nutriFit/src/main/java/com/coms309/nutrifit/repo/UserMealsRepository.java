package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * The interface User meals repository.
 */
@Repository
public interface UserMealsRepository extends JpaRepository<UserMeals, Integer> {
	/**
	 * Find by user and date user meals.
	 *
	 * @param user the user
	 * @param date the date
	 *
	 * @return the user meals
	 */
	UserMeals findByUserAndDate(User user, LocalDate date);

	/**
	 * Find user meals by user username and date optional.
	 *
	 * @param username the username
	 * @param date     the date
	 *
	 * @return the optional
	 */
	@NonNull
	Optional<UserMeals> findUserMealsByUser_UsernameAndDate(@NonNull String username, @NonNull LocalDate date);

//    Optional<UserMeals> findFirstByUser_UsernameAndDateAndMealList_MealTypeOrderByUser_UsernameAscDateAscMealList_MealTypeAsc(
//            @NonNull String username,
//            @NonNull LocalDate date,
//            @NonNull MealType mealType);

}