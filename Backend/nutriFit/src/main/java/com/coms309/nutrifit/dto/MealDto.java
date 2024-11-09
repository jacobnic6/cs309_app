package com.coms309.nutrifit.dto;

import com.coms309.nutrifit.entity.nutrition.MealType;
import com.coms309.nutrifit.entity.nutrition.Trackable;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link com.coms309.nutrifit.entity.nutrition.Meal}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealDto extends AbstractNutritionDto{
    @EqualsAndHashCode.Exclude
   private int id;
    private  List<FoodDto> foods;
    private Map<String, Integer> mealNutrients;

    private String mealType;

    public MealDto(List<FoodDto> foods){
        this.foods = foods;
        mealNutrients = new HashMap<>();
        for(FoodDto f : foods){
        combineNutrients(f.getFoodNutrients());
        }
    }

    @Override
    public void addNutrient(String nutrient, int amount) {

        if(mealNutrients.containsKey(nutrient)){
            mealNutrients.put(nutrient, mealNutrients.get(nutrient) + amount);
        }else{
            mealNutrients.put(nutrient, amount);
        }
    }

    @Override
    public void combineNutrients(Map<String, Integer> nutrients) {
        if(nutrients != null){
            for(String n : nutrients.keySet()){
                if(mealNutrients.containsKey(n)){
                    mealNutrients.put(n, mealNutrients.get(n) + nutrients.get(n));
                }else{
                    mealNutrients.put(n, nutrients.get(n));
                }
            }
        }
    }
}