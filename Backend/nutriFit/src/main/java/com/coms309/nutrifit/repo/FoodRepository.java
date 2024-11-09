package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.nutrition.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Integer> {
}