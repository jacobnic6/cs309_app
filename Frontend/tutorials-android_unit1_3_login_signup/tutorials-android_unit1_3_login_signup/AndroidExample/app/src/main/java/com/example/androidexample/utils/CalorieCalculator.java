package com.example.androidexample.utils;

public class CalorieCalculator {
    // Activity level multipliers
    private static final double SEDENTARY = 1.2;
    private static final double LIGHT_ACTIVITY = 1.375;
    private static final double MODERATE_ACTIVITY = 1.55;
    private static final double VERY_ACTIVE = 1.725;

    // Calories per pound of bodyweight for different activity levels
    private static final double BASE_CALORIES_PER_POUND = 12;

    public static int calculateBaseCalories(double weightInPounds, ActivityLevel level) {
        // Calculate base calories using weight
        double baseCalories = weightInPounds * BASE_CALORIES_PER_POUND;

        // Apply activity multiplier
        double activityMultiplier;
        switch (level) {
            case SEDENTARY:
                activityMultiplier = SEDENTARY;
                break;
            case LIGHT_ACTIVITY:
                activityMultiplier = LIGHT_ACTIVITY;
                break;
            case MODERATE_ACTIVITY:
                activityMultiplier = MODERATE_ACTIVITY;
                break;
            case VERY_ACTIVE:
                activityMultiplier = VERY_ACTIVE;
                break;
            default:
                activityMultiplier = MODERATE_ACTIVITY;
        }

        return (int) (baseCalories * activityMultiplier);
    }

    public static NutritionGoals calculateNutritionGoals(double weightInPounds, ActivityLevel level, Goal goal) {
        int baseCalories = calculateBaseCalories(weightInPounds, level);

        // Adjust calories based on goal
        switch (goal) {
            case LOSE_WEIGHT:
                baseCalories -= 500; // 500 calorie deficit for weight loss
                break;
            case GAIN_WEIGHT:
                baseCalories += 500; // 500 calorie surplus for weight gain
                break;
            default: // MAINTAIN
                break;
        }

        return new NutritionGoals(baseCalories);
    }

    public enum ActivityLevel {
        SEDENTARY("Sedentary (little or no exercise)"),
        LIGHT_ACTIVITY("Lightly active (1-3 workouts/week)"),
        MODERATE_ACTIVITY("Moderately active (3-5 workouts/week)"),
        VERY_ACTIVE("Very active (6+ workouts/week)");

        private final String description;

        ActivityLevel(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    public enum Goal {
        LOSE_WEIGHT("Lose weight (500 calorie deficit)"),
        MAINTAIN("Maintain weight"),
        GAIN_WEIGHT("Gain weight (500 calorie surplus)");

        private final String description;

        Goal(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}