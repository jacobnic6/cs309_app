package com.coms309.nutrifit.entity.nutrition;

import java.util.Map;

public interface Trackable {

    public void addNutrient(String nutrient, int amount);
    public void combineNutrients(Map<String, Integer> nutrients );

}
