package com.example.androidexample.utils;

public class NutritionGoals {
    // Macro ratios
    private static final double PROTEIN_RATIO = 0.35; // 35%
    private static final double CARBS_RATIO = 0.45;   // 45%
    private static final double FAT_RATIO = 0.20;     // 20%

    // Calories per gram of each macro
    private static final int CALORIES_PER_PROTEIN = 4;
    private static final int CALORIES_PER_CARB = 4;
    private static final int CALORIES_PER_FAT = 9;

    private int dailyCaloriesGoal;
    private int proteinGoal;
    private int carbsGoal;
    private int fatGoal;

    public NutritionGoals(int dailyCaloriesGoal) {
        this.dailyCaloriesGoal = dailyCaloriesGoal;
        calculateMacroGoals();
    }

    public void setDailyCaloriesGoal(int dailyCaloriesGoal) {
        this.dailyCaloriesGoal = dailyCaloriesGoal;
        calculateMacroGoals();
    }

    private void calculateMacroGoals() {
        // Calculate protein grams (35% of calories)
        proteinGoal = (int) ((dailyCaloriesGoal * PROTEIN_RATIO) / CALORIES_PER_PROTEIN);

        // Calculate carbs grams (45% of calories)
        carbsGoal = (int) ((dailyCaloriesGoal * CARBS_RATIO) / CALORIES_PER_CARB);

        // Calculate fat grams (20% of calories)
        fatGoal = (int) ((dailyCaloriesGoal * FAT_RATIO) / CALORIES_PER_FAT);
    }

    public int getDailyCaloriesGoal() { return dailyCaloriesGoal; }
    public int getProteinGoal() { return proteinGoal; }
    public int getCarbsGoal() { return carbsGoal; }
    public int getFatGoal() { return fatGoal; }

    public double getProteinRatio() { return PROTEIN_RATIO * 100; }
    public double getCarbsRatio() { return CARBS_RATIO * 100; }
    public double getFatRatio() { return FAT_RATIO * 100; }
}