package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.dto.UserMealsDto;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.nutrition.Meal;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

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

}