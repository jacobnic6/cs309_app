package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.dto.UserMealsDto;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.nutrition.Meal;
import com.coms309.nutrifit.entity.nutrition.MealType;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
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
     * @return the user meals
     */
    UserMeals findByUserAndDate(User user, LocalDate date);



    @NonNull
    Optional<UserMeals> findUserMealsByUser_UsernameAndDate(@NonNull String username, @NonNull LocalDate date);

//    Optional<UserMeals> findFirstByUser_UsernameAndDateAndMealList_MealTypeOrderByUser_UsernameAscDateAscMealList_MealTypeAsc(
//            @NonNull String username,
//            @NonNull LocalDate date,
//            @NonNull MealType mealType);


}