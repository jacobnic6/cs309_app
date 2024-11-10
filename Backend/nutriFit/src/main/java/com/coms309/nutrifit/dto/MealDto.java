package com.coms309.nutrifit.dto;

import com.coms309.nutrifit.entity.nutrition.MealType;
import com.coms309.nutrifit.entity.nutrition.Trackable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
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
public class MealDto implements Serializable{


    @JsonProperty("foodName")
    private String foodName;

    @JsonProperty("servingSize")
    private String servingSize;


    @JsonProperty(value = "calories", defaultValue = "0", required = true)
    private int calories;

    @JsonProperty(value = "protein", defaultValue = "0", required = true)
    private int protein;

    @JsonKey
    @JsonProperty(value = "carbs", defaultValue = "0", required = true)
    private int carbs;

    @JsonProperty(value = "fat", defaultValue = "0", required = true)
    private int fat;

    @JsonProperty(value = "mealType",defaultValue = "SNACK", required = true)
    private String mealType;


    public Map<String, Integer> getNutrients(){
        Map<String, Integer> nutrients = new HashMap<>();
        nutrients.put("calories", calories);
        nutrients.put("protein", protein);
        nutrients.put("carbs", carbs);
        nutrients.put("fat", fat);
        return nutrients;
    }


}