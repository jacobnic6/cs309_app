package com.coms309.nutrifit.service;

import com.coms309.nutrifit.repo.MealRepository;
import org.springframework.stereotype.Service;

/**
 * The type Meal service.
 */
@Service
public class MealService {

    private final MealRepository mealRepository;

    /**
     * Instantiates a new Meal service.
     *
     * @param mealRepository the meal repository
     */
    public MealService(MealRepository mealRepository){
       this.mealRepository = mealRepository;
   }


}
