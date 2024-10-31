package com.example.androidexample.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FoodSearchResponse {
    @SerializedName("foods")
    private List<Food> foods;

    public List<Food> getFoods() {
        return foods;
    }

    public static class Food {
        @SerializedName("fdcId")
        private long fdcId;

        @SerializedName("description")
        private String description;

        @SerializedName("servingSize")
        private double servingSize;

        @SerializedName("servingSizeUnit")
        private String servingSizeUnit;

        @SerializedName("foodNutrients")
        private List<Nutrient> foodNutrients;

        public String getDescription() {
            return description;
        }

        public double getServingSize() {
            return servingSize;
        }

        public String getServingSizeUnit() {
            return servingSizeUnit;
        }

        public List<Nutrient> getFoodNutrients() {
            return foodNutrients;
        }
    }

    public static class Nutrient {
        @SerializedName("nutrientId")
        private int nutrientId;

        @SerializedName("nutrientName")
        private String nutrientName;

        @SerializedName("value")
        private double value;

        @SerializedName("unitName")
        private String unitName;

        public String getNutrientName() {
            return nutrientName;
        }

        public double getValue() {
            return value;
        }
    }
}
