package com.coms309.nutrifit.service;

import com.coms309.nutrifit.dto.FoodDto;
import com.coms309.nutrifit.dto.MealDto;
import com.coms309.nutrifit.dto.UserMealsDto;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.nutrition.Food;
import com.coms309.nutrifit.entity.nutrition.Meal;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
import com.coms309.nutrifit.repo.FoodRepository;
import com.coms309.nutrifit.repo.MealRepository;
import com.coms309.nutrifit.repo.UserMealsRepository;
import com.coms309.nutrifit.repo.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class NutritionService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMealsRepository userMealsRepository;

    @Autowired
    MealRepository mealRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    ObjectMapper objectMapper;


    public UserMeals createMealList(LocalDate date, String username ) {

        User user = userRepository.findByUsername(username);

        if(user == null) {
            return null;
        }

        UserMeals meals = userMealsRepository.findByUserAndDate(user, date);
        if(meals == null) {
            meals = new UserMeals();
            meals.setDate(date);
            meals.setUser(user);


        }
        return userMealsRepository.save(meals);
    }

    public UserMeals getMealsByDate(LocalDate date, String username) {
        User user = userRepository.findByUsername(username);
        UserMeals meals = userMealsRepository.findByUserAndDate(user, date);
        return userMealsRepository.findByUserAndDate(user, date);
    }

    public List<UserMeals> getAllUserMeals() {
        return userMealsRepository.findAll();
    }

    public UserMeals addMeal(LocalDate date, String username, MealDto mealDto) {
        UserMeals meals = getMealsByDate(date, username);
        if(meals == null) {
            meals = createMealList(date, username);

        }


      Meal meal = objectMapper.convertValue(mealDto, Meal.class);

        List<FoodDto> foodDtoList = mealDto.getFoods();
        List<Food> food = new ArrayList<>();
        for(FoodDto foodDto : foodDtoList){
            mealDto.combineNutrients(foodDto.getFoodNutrients());
            Food f = objectMapper.convertValue(foodDto, Food.class);
            f.setMeal(meal);
            food.add(f);
        }

     meal.setFoods(food);
    mealDto.setMealNutrients(mealDto.getMealNutrients());
    Map<String, Integer>map = meals.getNutrientTotals();

    UserMealsDto d = new UserMealsDto();
    d.setNutrientTotals(map);
    d.combineNutrients(mealDto.getMealNutrients());
    meals.setNutrientTotals(d.getNutrientTotals());
        meal.setUserMeals(meals);
        meals.getMealList().add(meal);
        mealRepository.save(meal);

        return userMealsRepository.save(meals);
    }


    public String deleteMealById(int id) {

        if(!mealRepository.existsById(id)) {
           return "meal with id " + id + " does not exist";
        }
        mealRepository.deleteById(id);
        return "meal with id " + id + " deleted";
    }


    public String deleteMealListById(int id) {
        if(!userMealsRepository.existsById(id)) {
            return "meal list with id " + id + " does not exist";
        }

        userMealsRepository.deleteById(id);
        return "meal list with id " + id + " deleted";
    }



    public UserMeals updateMealListByDate(int id, UserMealsDto mealsDto) {
        if(!userMealsRepository.existsById(id)) {
            return null;
        }
        UserMeals meals = userMealsRepository.findById(id).orElse(null);
        if(meals != null) {
           int i = meals.getId();
           UserMeals.UserMealsBuilder builder = UserMeals.builder();
           for(Field field : mealsDto.getClass().getDeclaredFields()) {

           }
           builder.id(i);

           builder.date(mealsDto.getDate());
           builder.user(meals.getUser());
           builder.mealList(meals.getMealList());
           builder.nutrientTotals(mealsDto.getNutrientTotals());
           UserMeals mealsB = builder.build();
          return userMealsRepository.save(mealsB);

        }
        return null;

    }
}
