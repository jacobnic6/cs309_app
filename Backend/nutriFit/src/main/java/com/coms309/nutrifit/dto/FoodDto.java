package com.coms309.nutrifit.dto;

import com.coms309.nutrifit.entity.nutrition.Trackable;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO for {@link com.coms309.nutrifit.entity.nutrition.Food}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodDto extends AbstractNutritionDto {
    @EqualsAndHashCode.Exclude
    private int id;
    private String foodName;
    private String foodType;
    private int amount;
    private Map<String, Integer> foodNutrients;


    @Override
    public void addNutrient(String nutrient, int amount) {
        if(foodNutrients.containsKey(nutrient)){
            foodNutrients.put(nutrient, foodNutrients.get(nutrient) + amount);
        }else{
            foodNutrients.put(nutrient, amount);
        }
    }

    @Override
    public void combineNutrients(Map<String, Integer> nutrients) {
        if(nutrients != null){
            for(String n : nutrients.keySet()){
                if(foodNutrients.containsKey(n)){
                    foodNutrients.put(n, foodNutrients.get(n) + nutrients.get(n));
                }else{
                    foodNutrients.put(n, nutrients.get(n));
                }
            }
        }
    }
}