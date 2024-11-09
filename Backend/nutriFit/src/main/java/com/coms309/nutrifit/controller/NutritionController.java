package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.dto.MealDto;
import com.coms309.nutrifit.dto.UserMealsDto;
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
    public ResponseEntity<?> createMealList(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable("username") String username) {

       UserMeals meals = nutritionService.createMealList(date, username);


      return ResponseEntity.status(HttpStatus.OK).body(meals);


    }

    @PostMapping("/add/{date}/{username}")
    public ResponseEntity<?> addMeal(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                     @PathVariable("username") String username, @RequestBody MealDto meal) {
       return  ResponseEntity.status(HttpStatus.OK).body(nutritionService.addMeal(date, username, meal ));
    }

    //Read
    @GetMapping("/{date}/{username}")
    public UserMeals getMealsByDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable("username") String username) {


       return nutritionService.getMealsByDate(date, username);

    }

    //Update
    @PutMapping("/{Id}")
    public UserMeals updateMealListById(@PathVariable("Id")int id,  @RequestBody UserMealsDto mealsDto) {
        return nutritionService.updateMealListByDate(id, mealsDto);

    }

    //Delete

    @DeleteMapping("/{id}")
    public String deleteMealListById(@PathVariable int id) {
        return   nutritionService.deleteMealListById(id);
    }

    @DeleteMapping("/meal/{id}")
    public String deleteMealById(@PathVariable int id) {
      return   nutritionService.deleteMealById(id);
    }




    //List
    @GetMapping
    public List<UserMeals> getAllMeals() {
        return nutritionService.getAllUserMeals();
    }
}
