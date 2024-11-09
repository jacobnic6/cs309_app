package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.nutrition.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Integer> {
}