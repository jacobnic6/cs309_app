package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
import com.coms309.nutrifit.repo.UserMealsRepository;
import com.coms309.nutrifit.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NutritionService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMealsRepository userMealsRepository;



    public UserMeals addMeal(String username) {

        User user = userRepository.findByUsername(username);
        LocalDate date = LocalDate.now();

        UserMeals meals = userMealsRepository.findByUserAndDate(user, date);
        if(meals == null) {
            meals = new UserMeals();
            meals.setDate(date);
            meals.setUser(user);
            userMealsRepository.save(meals);
        }
        return userMealsRepository.findByUserAndDate(user, date);
    }

    public UserMeals getMealsForDay(LocalDate date, String username) {
        User user = userRepository.findByUsername(username);
        UserMeals meals = userMealsRepository.findByUserAndDate(user, date);
        return userMealsRepository.findByUserAndDate(user, date);
    }

    public List<UserMeals> getAllUserMeals() {
        return userMealsRepository.findAll();
    }
}
