package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface UserMealsRepository extends JpaRepository<UserMeals, Integer> {
  UserMeals findByUserAndDate(User user, LocalDate date);
}