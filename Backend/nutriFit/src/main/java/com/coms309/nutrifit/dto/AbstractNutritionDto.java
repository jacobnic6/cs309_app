package com.coms309.nutrifit.dto;

import com.coms309.nutrifit.entity.nutrition.Trackable;
import lombok.*;

import java.io.Serializable;
import java.util.Map;


public abstract class AbstractNutritionDto implements Trackable  {




    protected  Map<String, Integer> nutrientMap;

    public Map<String, Integer> getNutrientMap() {
        return nutrientMap;
    }

    @Override
    public void addNutrient(String nutrient, int amount) {
        if(nutrientMap.containsKey(nutrient)){
            nutrientMap.put(nutrient, nutrientMap.get(nutrient) + amount);
        }else{
            nutrientMap.put(nutrient, amount);
        }
    }

    @Override
    public void combineNutrients(Map<String, Integer> nutrients) {
        if(nutrients != null){
            for(String n : nutrients.keySet()){
                if(nutrientMap.containsKey(n)){
                    nutrientMap.put(n, nutrientMap.get(n) + nutrients.get(n));
                }else{
                    nutrientMap.put(n, nutrients.get(n));
                }
            }
        }
    }
}
