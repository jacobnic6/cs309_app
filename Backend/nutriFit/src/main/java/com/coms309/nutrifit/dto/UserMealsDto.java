package com.coms309.nutrifit.dto;

import com.coms309.nutrifit.entity.nutrition.Trackable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link com.coms309.nutrifit.entity.nutrition.UserMeals}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMealsDto extends AbstractNutritionDto {

    @EqualsAndHashCode.Exclude
    private int id;
    private LocalDate date;
    private List<MealDto> meals;
    private Map<String, Integer> nutrientTotals;


    /**
     * Instantiates a new User meals dto.
     *
     * @param meals the meals
     */
    UserMealsDto(List<MealDto> meals){
        date = LocalDate.now();
        this.meals = meals;
        nutrientTotals = new HashMap<>();
        for(MealDto m : meals){
            combineNutrients(m.getMealNutrients());
        }

    }
    @Override
    public void addNutrient(String nutrient, int amount) {
        if(nutrientTotals.containsKey(nutrient)){
            nutrientTotals.put(nutrient, nutrientTotals.get(nutrient) + amount);
        }else{
            nutrientTotals.put(nutrient, amount);
        }
    }

    @Override
    public void combineNutrients(Map<String, Integer> nutrients) {
        if(nutrients != null){
            for(String n : nutrients.keySet()){
                if(nutrientTotals.containsKey(n)){
                    nutrientTotals.put(n, nutrientTotals.get(n) + nutrients.get(n));
                }else{
                    nutrientTotals.put(n, nutrients.get(n));
                }
            }
        }
    }
}