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
    void addNutrient(String nutrient, int amount);

    /**
     * Combine nutrients.
     *
     * @param nutrients the nutrients
     */
    void combineNutrients(Map<String, Integer> nutrients);

}
