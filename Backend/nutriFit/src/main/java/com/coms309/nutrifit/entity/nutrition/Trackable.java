package com.coms309.nutrifit.entity.nutrition;

import java.util.Map;

/**
 * The interface Trackable.
 */
public interface Trackable {

    /**
     * Add nutrient.
     *
     * @param nutrient the nutrient
     * @param amount   the amount
     */
    public void addNutrient(String nutrient, int amount);

    /**
     * Combine nutrients.
     *
     * @param nutrients the nutrients
     */
    public void combineNutrients(Map<String, Integer> nutrients );

}
