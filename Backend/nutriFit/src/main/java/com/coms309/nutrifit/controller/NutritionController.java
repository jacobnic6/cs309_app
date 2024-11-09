package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.nutrition.Meal;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
import com.coms309.nutrifit.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/meals")
public class NutritionController {

    @Autowired
    NutritionService nutritionService;

    //Create
    @PostMapping("/{date}/{username}")
    public ResponseEntity<?> addMeal(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable("username") String username) {


      return ResponseEntity.status(HttpStatus.OK).body(nutritionService.addMeal(username));


    }

    //Read
    @GetMapping("/{date}/{username}")
    public UserMeals getMealsForDay(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable("username") String username) {


       return nutritionService.getMealsForDay(date, username);

    }

    //Update

    //Delete

    //List
    @GetMapping
    public List<UserMeals> getAllMeals() {
        return nutritionService.getAllUserMeals();
    }
}
