package com.coms309.nutrifit.service;

import com.coms309.nutrifit.repo.MealRepository;
import org.springframework.stereotype.Service;

@Service
public class MealService {

    private final MealRepository mealRepository;

   public MealService(MealRepository mealRepository){
       this.mealRepository = mealRepository;
   }


}
