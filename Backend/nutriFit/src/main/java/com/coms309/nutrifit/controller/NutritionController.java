package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.dto.MealDto;
import com.coms309.nutrifit.dto.NutrientTotalsDto;
import com.coms309.nutrifit.dto.UserMealsDto;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
import com.coms309.nutrifit.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Nutrition controller.
 */
@RestController
@RequestMapping("/meals")
public class NutritionController {

    /**
     * The Nutrition service.
     */
    @Autowired
    NutritionService nutritionService;

    /**
     * Create meal list response entity.
     *
     * @param date     the date
     * @param username the username
     * @return the response entity
     */
//Create
    @PostMapping("/{date}/{username}")
    public ResponseEntity<?> createMealList(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable("username") String username) {

       UserMeals meals = nutritionService.createMealList(date, username);


      return ResponseEntity.status(HttpStatus.OK).body(meals);


    }



    /**
     * Add meal response entity.
     *
     * @param date     the date
     * @param username the username
     * @param meal     the meal
     * @return the response entity
     */
    @PostMapping("/food/{date}/{username}")
    public UserMeals addMeal(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                     @PathVariable("username" ) String username, @RequestBody MealDto mealDto) {
        if(username == null || mealDto == null){
            throw new RuntimeException("Invalid input ")  ;
        }
        if(date == null){
            date = LocalDate.now();
        }
       return  nutritionService.addMeal(date, username, mealDto );
    }

    /**
     * Gets meals by date.
     *
     * @param date     the date
     * @param username the username
     * @return the meals by date
     */
//Read
    @GetMapping("/{date}/")
    public UserMeals getMealsByDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam String username) {


       return nutritionService.getMealsByDate(date, username);

    }

    /**
     * Update meal list by id user meals.
     *
     * @param id       the id
     * @param mealsDto the meals dto
     * @return the user meals
     */
//Update
    @PutMapping("/{Id}")
    public UserMeals updateMealListById(@PathVariable("Id")int id,  @RequestBody UserMealsDto mealsDto) {
        return nutritionService.updateMealListByDate(id, mealsDto);

    }



    //Delete

    /**
     * Delete meal list by id string.
     *
     * @param id the id
     * @return the string
     */
    @DeleteMapping("/{id}")
    public String deleteMealListById(@PathVariable int id) {
        return   nutritionService.deleteMealListById(id);
    }

    /**
     * Delete meal by id string.
     *
     * @param id the id
     * @return the string
     */
    @DeleteMapping("/meal/{id}")
    public String deleteMealById(@PathVariable int id) {
      return   nutritionService.deleteMealById(id);
    }


    /**
     * Gets all meals.
     *
     * @return the all meals
     */
    //List
    @GetMapping()
    public List<UserMeals> getAllMealLists() {
        return nutritionService.getAllUserMeals();
    }

    @GetMapping("/totals/{date}/")
    public NutrientTotalsDto getNutrientTotals(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                   LocalDate date, @RequestParam String username) {

        if(username == null ){
           throw new RuntimeException("Invalid input ")  ;
        }
        if(date == null){
            date = LocalDate.now();
        }
        return nutritionService.getDailyTotals(date, username);

    }



}
