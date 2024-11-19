package com.example.androidexample.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Represents a response from the USDA Food Database API containing food search results.
 * This class encapsulates the data structure returned when searching for foods,
 * providing detailed nutritional information for each food item.
 *
 * The response contains a list of food items. Each food item includes:
 * - Basic identification details, such as a unique food ID and description.
 * - Standard serving size information, including the amount and unit of measure.
 * - A list of nutrients, each with its name, value, and unit.
 *
 * Example usage:
 * <pre>
 * FoodSearchResponse response = apiClient.searchFoods("apple");
 * List&lt;Food&gt; foods = response.getFoods();
 * for (Food food : foods) {
 *     System.out.println(food.getDescription());
 *     System.out.println(food.getServingSize() + " " + food.getServingSizeUnit());
 *     for (FoodSearchResponse.Nutrient nutrient : food.getFoodNutrients()) {
 *         System.out.println(nutrient.getNutrientName() + ": " + nutrient.getValue() + " " + nutrient.getUnitName());
 *     }
 * }
 * </pre>
 *
 * This class helps to parse and organize food data from the API, making it easier to handle
 * and display detailed nutritional information in a structured format.
 *
 * @author Michael Becker
 * @version 1.1
 * @since 2024-03-20
 */
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
     * @return A List of Food objects containing nutritional data.
     *         Returns an empty list if no foods were found in the response.
     */
    public List<Food> getFoods() {
        return foods;
    }

    /**
     * Represents a single food item with detailed nutritional information.
     * This class contains the unique ID, description, serving size, and a list of nutrients for the food item.
     */
    public static class Food {
        /**
         * Unique identifier for the food item in the USDA database.
         * This ID can be used for making subsequent queries to retrieve more specific or detailed data about the food item.
         */
        @SerializedName("fdcId")
        private long fdcId;

        /**
         * description of the food item.
         * The description provides the common name and may include additional details
         * such as brand names,etc.
         */
        @SerializedName("description")
        private String description;

        /**
         * The standard serving size of the food item.
         * This value represents the recommended quantity for a single serving.
         */
        @SerializedName("servingSize")
        private double servingSize;

        /**
         * The unit of measurement for the serving size.
         * Common units include "g" (grams), "oz" (ounces), or "ml" (milliliters)
         */
        @SerializedName("servingSizeUnit")
        private String servingSizeUnit;

        /**
         * List of nutrients contained in this food item.
         * Each nutrient includes its name, quantity, and unit of measurement.
         * This list provides detailed information about the nutritional content of the food.
         */
        @SerializedName("foodNutrients")
        private List<Nutrient> foodNutrients;

        /**
         * Gets the unique identifier for the food item.
         *
         * @return A long value representing the food item's ID in the USDA database.
         */
        public long getFdcId() {
            return fdcId;
        }

        /**
         * Gets the description or name of the food item.
         *
         * @return A String containing the food item's description.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets the standard serving size quantity of the food item.
         *
         * @return A double value representing the serving size quantity.
         */
        public double getServingSize() {
            return servingSize;
        }

        /**
         * Gets the unit of measurement for the serving size.
         *
         * @return A String containing the unit of the serving size, such as "g", "oz", or "ml".
         */
        public String getServingSizeUnit() {
            return servingSizeUnit;
        }

        /**
         * Gets the list of nutrients for this food item.
         * Nutrients provide information about components such as proteins, fats, carbohydrates.
         *
         * @return A List of Nutrient objects containing detailed nutritional information.
         *         Returns an empty list if no nutrient information is available for the food item.
         */
        public List<Nutrient> getFoodNutrients() {
            return foodNutrients;
        }
    }

    /**
     * Represents a single nutrient and its value within a food item.
     * Nutrients are components of the food, such as protein, carbohydrates, fats, vitamins, or minerals,
     * each with a specific quantity and unit of measure.
     */
    public static class Nutrient {
        /**
         * Unique identifier for the nutrient type in the USDA database.
         * This ID is useful for referencing the specific nutrient across different food items.
         */
        @SerializedName("nutrientId")
        private int nutrientId;

        /**
         * Examples include "Protein", "Fat", and "Carbohydrate, by difference".
         * The name provides a description of the nutrient.
         */
        @SerializedName("nutrientName")
        private String nutrientName;

        /**
         * Numerical value indicating the amount of the nutrient present in the food item.
         */
        @SerializedName("value")
        private double value;

        /**
         * Unit of measurement for value
         * Common units include "g" (grams), "mg" (milligrams), and "µg" (micrograms),
         * providing context to the nutrient value.
         */
        @SerializedName("unitName")
        private String unitName;

        /**
         * Gets the unique identifier for the nutrient.
         *
         * @return An int value representing the nutrient's ID in the USDA database.
         */
        public int getNutrientId() {
            return nutrientId;
        }

        /**
         * Gets the name of the nutrient.
         *
         * @return A String containing the nutrient name, such as "Protein" or "Calcium".
         */
        public String getNutrientName() {
            return nutrientName;
        }

        /**
         * Gets the quantity value of the nutrient present in the food item.
         *
         * @return A double value representing the amount of the nutrient in the specified unit.
         */
        public double getValue() {
            return value;
        }

        /**
         * Gets the unit of measurement for the nutrient value.
         *
         * @return A String containing the unit name, such as "g", "mg", or "µg".
         */
        public String getUnitName() {
            return unitName;
        }
    }
}
