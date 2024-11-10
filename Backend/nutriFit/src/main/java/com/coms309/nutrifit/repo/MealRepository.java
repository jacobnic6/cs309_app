package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.nutrition.Meal;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The interface Meal repository.
 */
public interface MealRepository extends JpaRepository<Meal, Integer> {
    @NonNull
    Optional<Meal> findFirstByUserMeals_User_UsernameAndUserMeals_DateAndMealTypeAllIgnoreCaseOrderByUserMeals_User_UsernameAscUserMeals_DateAscUserMeals_MealList_MealTypeAsc(
            @NonNull String username, @NonNull LocalDate date, @NonNull String mealType);

    List<Meal> findByUserMeals(UserMeals meals);
}