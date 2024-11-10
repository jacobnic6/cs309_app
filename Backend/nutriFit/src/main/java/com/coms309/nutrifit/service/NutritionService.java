package com.coms309.nutrifit.service;

import com.coms309.nutrifit.dto.MealDto;
import com.coms309.nutrifit.dto.NutrientTotalsDto;
import com.coms309.nutrifit.dto.UserMealsDto;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.nutrition.Meal;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
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

/**
 * The type Nutrition service.
 */
@Service
public class NutritionService {

    /**
     * The User repository.
     */
    @Autowired
    UserRepository userRepository;

    /**
     * The User meals repository.
     */
    @Autowired
    UserMealsRepository userMealsRepository;

    /**
     * The Meal repository.
     */
    @Autowired
    MealRepository mealRepository;



    /**
     * The Object mapper.
     */
    @Autowired
    ObjectMapper objectMapper;


    /**
     * Create meal list user meals.
     *
     * @param date     the date
     * @param username the username
     * @return the user meals
     */
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

    /**
     * Gets meals by date.
     *
     * @param date     the date
     * @param username the username
     * @return the meals by date
     */
    public UserMeals getMealsByDate(LocalDate date, String username) {
        User user = userRepository.findByUsername(username);
        UserMeals meals = userMealsRepository.findByUserAndDate(user, date);
        return userMealsRepository.findByUserAndDate(user, date);
    }

    /**
     * Gets all user meals.
     *
     * @return the all user meals
     */
    public List<UserMeals> getAllUserMeals() {
        return userMealsRepository.findAll();
    }

    /**
     * Add meal user meals.
     *
     * @param date     the date
     * @param username the username
     * @param mealDto  the meal dto
     * @return the user meals
     */
    public UserMeals addMeal(LocalDate date, String username, MealDto mealDto) {

        if(!userRepository.existsByUsername(username)) {
            throw new RuntimeException("User with username " + username + " does not exist");
        }

        UserMeals meals = getUserMealList(username, date);

        Meal meal = objectMapper.convertValue(mealDto, Meal.class);
        List<Meal> mealList = meals.getMealList();
        if(mealList == null) {
            mealList = new ArrayList<>();

        }
        mealList.add(meal);
        meals.setMealList(mealList);
        meal.setUserMeals(meals);

        Map<String, Integer> nutrients = mealDto.getNutrients();
        Map<String, Integer> totals = meals.getNutrientTotals();
        if(totals == null ||totals.isEmpty()) {
            meals.setNutrientTotals(nutrients);
        }else {
            for(String key : nutrients.keySet()) {
                if(totals.containsKey(key)) {
                    totals.put(key, totals.get(key) + nutrients.get(key));
                }else{
                    totals.put(key, nutrients.get(key));
                }
            }
        }
        meals.setNutrientTotals(totals);


        return userMealsRepository.save(meals);
    }


    /**
     * Delete meal by id string.
     *
     * @param id the id
     * @return the string
     */
    public String deleteMealById(int id) {

        if(!mealRepository.existsById(id)) {
           return "meal with id " + id + " does not exist";
        }
        mealRepository.deleteById(id);
        return "meal with id " + id + " deleted";
    }


    /**
     * Delete meal list by id string.
     *
     * @param id the id
     * @return the string
     */
    public String deleteMealListById(int id) {
        if(!userMealsRepository.existsById(id)) {
            return "meal list with id " + id + " does not exist";
        }

        userMealsRepository.deleteById(id);
        return "meal list with id " + id + " deleted";
    }


    /**
     * Update meal list by date user meals.
     *
     * @param id       the id
     * @param mealsDto the meals dto
     * @return the user meals
     */
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

    private UserMeals getUserMealList(String username, LocalDate date) {
        return userMealsRepository
                .findUserMealsByUser_UsernameAndDate(username, date)
                .orElse( new UserMeals());
    }

    private Meal getMeal(LocalDate date, String username, String mealType) {
       return mealRepository
                .findFirstByUserMeals_User_UsernameAndUserMeals_DateAndMealTypeAllIgnoreCaseOrderByUserMeals_User_UsernameAscUserMeals_DateAscUserMeals_MealList_MealTypeAsc(
                        username,date, mealType).orElse(new Meal());
    }

    public NutrientTotalsDto getDailyTotals(LocalDate date, String username) {
        if(!userRepository.existsByUsername(username)) {
            throw new RuntimeException("User with username " + username + " does not exist");
        }
        User user = userRepository.findByUsername(username);

        UserMeals meals = userMealsRepository.findByUserAndDate(user, date);
        if(meals == null) {
            throw new RuntimeException("User with username " + username + " does not exist");
        }
        NutrientTotalsDto totals = new NutrientTotalsDto();
        totals.setDate(date);
        mealRepository.findByUserMeals(meals).stream().forEach(meal -> {

            totals.addCalories(meal.getCalories());
            totals.addCarbs(meal.getCarbs());
            totals.addProtein(meal.getProtein());
            totals.addFats(meal.getFat());
            String mealType = meal.getMealType().toLowerCase();
            switch (mealType) {
                case "breakfast":
                    totals.addBreakfastCalories(meal.getCalories());
                    break;
                case "lunch":
                    totals.addLunchCalories(meal.getCalories());
                    break;
                case "dinner":
                    totals.addDinnerCalories(meal.getCalories());
                    break;
                   default:
                       totals.addSnackCalories(meal.getCalories());
                       break;
            }

        }) ;
        return totals;

    }




}
