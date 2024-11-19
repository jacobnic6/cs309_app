/**
 * Represents a response from the USDA Food Database API containing food search results.
 * This class encapsulates the data structure returned when searching for foods,
 * including detailed nutritional information for each food item.
 *
 * The response contains a list of food items, where each food item includes:
 * - Basic food information (ID, description)
 * - Serving size information
 * - Detailed nutritional content
 *
 * Example usage:
 * <pre>
 * FoodSearchResponse response = apiClient.searchFoods("apple");
 * List<Food> foods = response.getFoods();
 * for (Food food : foods) {
 *     System.out.println(food.getDescription());
 *     System.out.println(food.getServingSize() + " " + food.getServingSizeUnit());
 * }
 * </pre>
 *
 * @author Michael Becker
 * @version 1.0
 * @since 2024-03-20
 */
package com.example.androidexample.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FoodSearchResponse {

    /**
     * List of food items returned from the search query.
     * This field is serialized from the "foods" field in the JSON response.
     */
    @SerializedName("foods")
    private List<Food> foods;

    /**
     * Retrieves the list of food items from the search response.
     *
     * @return A List of Food objects containing detailed food information and nutritional data.
     *         Returns an empty list if no foods were found.
     */
    public List<Food> getFoods() {
        return foods;
    }

    /**
     * Represents a single food item with its nutritional information.
     * This class contains detailed information about a specific food item,
     * including its description, serving size, and nutritional content.
     */
    public static class Food {
        /**
         * Unique identifier for the food item in the USDA database.
         * This ID can be used for subsequent detailed queries about the specific food item.
         */
        @SerializedName("fdcId")
        private long fdcId;

        /**
         * Human-readable description of the food item.
         * Contains the common name and sometimes additional identifying information.
         */
        @SerializedName("description")
        private String description;

        /**
         * Numerical value representing the standard serving size.
         * Should be used in conjunction with servingSizeUnit.
         */
        @SerializedName("servingSize")
        private double servingSize;

        /**
         * Unit of measurement for the serving size.
         * Common values include "g" (grams), "oz" (ounces), or "ml" (milliliters).
         */
        @SerializedName("servingSizeUnit")
        private String servingSizeUnit;

        /**
         * List of nutrients contained in this food item.
         * Each nutrient includes its name, value, and unit of measurement.
         */
        @SerializedName("foodNutrients")
        private List<Nutrient> foodNutrients;

        /**
         * Gets the description or name of the food item.
         *
         * @return A String containing the food item's description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets the standard serving size quantity.
         *
         * @return A double value representing the serving size quantity
         */
        public double getServingSize() {
            return servingSize;
        }

        /**
         * Gets the unit of measurement for the serving size.
         *
         * @return A String containing the serving size unit (e.g., "g", "oz", "ml")
         */
        public String getServingSizeUnit() {
            return servingSizeUnit;
        }

        /**
         * Gets the list of nutrients for this food item.
         *
         * @return A List of Nutrient objects containing detailed nutritional information.
         *         Returns an empty list if no nutrient information is available.
         */
        public List<Nutrient> getFoodNutrients() {
            return foodNutrients;
        }
    }

    /**
     * Represents a single nutrient and its value within a food item.
     * This class contains information about a specific nutrient such as
     * protein, carbohydrates, fats, vitamins, or minerals.
     */
    public static class Nutrient {
        /**
         * Unique identifier for the nutrient type in the USDA database.
         * This ID can be used to consistently identify specific nutrients
         * across different food items.
         */
        @SerializedName("nutrientId")
        private int nutrientId;

        /**
         * Human-readable name of the nutrient.
         * Examples include "Protein", "Total lipid (fat)", "Carbohydrate, by difference".
         */
        @SerializedName("nutrientName")
        private String nutrientName;

        /**
         * Numerical value indicating the amount of the nutrient present.
         * This value should be interpreted in conjunction with the unitName.
         */
        @SerializedName("value")
        private double value;

        /**
         * Unit of measurement for the nutrient value.
         * Common units include "g" (grams), "mg" (milligrams), "µg" (micrograms).
         */
        @SerializedName("unitName")
        private String unitName;

        /**
         * Gets the name of the nutrient.
         *
         * @return A String containing the nutrient name
         */
        public String getNutrientName() {
            return nutrientName;
        }

        /**
         * Gets the quantity value of the nutrient.
         *
         * @return A double value representing the nutrient quantity in the specified unit
         */
        public double getValue() {
            return value;
        }
    }
}