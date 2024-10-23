package com.example.androidexample.main_five_pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.androidexample.AddMealActivity;
import com.example.androidexample.R;
import com.example.androidexample.main_five_pages.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NutritionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

    // The following is for switching to the other four "main pages of the app" - social, exercise, nutrition, and settings
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()

    {
        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item){
        int itemId = item.getItemId();

        if (itemId == R.id.social) {
            startActivity(new Intent(NutritionActivity.this, SocialActivity.class));
            return true;
        } else if (itemId == R.id.workouts) {
            startActivity(new Intent(NutritionActivity.this, WorkoutActivity.class));
            return true;
        } else if (itemId == R.id.profile) {
            startActivity(new Intent(NutritionActivity.this, UserProfileActivity.class));
            return true;
        } else if (itemId == R.id.nutrition) {
            startActivity(new Intent(NutritionActivity.this, NutritionActivity.class));
            return true;
        } else if (itemId == R.id.settings) {
            startActivity(new Intent(NutritionActivity.this, SettingsActivity.class));
            return true;
        }
        return false;
    }
    });
}
}
//package com.example.androidexample.main_five_pages;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.example.androidexample.AddMealActivity;
//import com.example.androidexample.R;
//import com.example.androidexample.main_five_pages.SettingsActivity;
//import com.example.androidexample.main_five_pages.SocialActivity;
//import com.example.androidexample.main_five_pages.UserProfileActivity;
//import com.example.androidexample.main_five_pages.WorkoutActivity;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.navigation.NavigationBarView;
//
//public class NutritionActivity extends AppCompatActivity {
//    // UI Elements
//    private TextView caloriesRemaining;
//    private ProgressBar caloriesProgress, proteinProgress, carbsProgress, fatProgress;
//    private TextView breakfastCalories, lunchCalories, dinnerCalories, snacksCalories;
//    private Button addMealButton, scanBarcodeButton, quickAddButton;
//
//    // Nutrition Goals
//    private final int DAILY_CALORIES_GOAL = 2000;
//    private final int DAILY_PROTEIN_GOAL = 150;
//    private final int DAILY_CARBS_GOAL = 250;
//    private final int DAILY_FAT_GOAL = 65;
//
//    // Current Totals
//    private int currentCalories = 0;
//    private int currentProtein = 0;
//    private int currentCarbs = 0;
//    private int currentFat = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nutrition);
//
//        // Initialize UI elements
//        initializeViews();
//        setupClickListeners();
//        updateNutritionDisplays();
//        setupNavigationBar();
//    }
//
//    private void initializeViews() {
//        // Progress Bars
//        caloriesProgress = findViewById(R.id.calories_progress);
//        proteinProgress = findViewById(R.id.protein_progress);
//        carbsProgress = findViewById(R.id.carbs_progress);
//        fatProgress = findViewById(R.id.fat_progress);
//
//        // TextViews
//        caloriesRemaining = findViewById(R.id.calories_remaining);
//        breakfastCalories = findViewById(R.id.breakfast_calories);
//        lunchCalories = findViewById(R.id.lunch_calories);
//        dinnerCalories = findViewById(R.id.dinner_calories);
//        snacksCalories = findViewById(R.id.snacks_calories);
//
//        // Buttons
//        addMealButton = findViewById(R.id.add_meal_button);
//        scanBarcodeButton = findViewById(R.id.scan_barcode_button);
//        quickAddButton = findViewById(R.id.quick_add_button);
//    }
//
//    private void setupClickListeners() {
//        addMealButton.setOnClickListener(v -> showAddMealDialog());
//
//        scanBarcodeButton.setOnClickListener(v -> {
//            // TODO: Implement barcode scanning functionality
//        });
//
//        quickAddButton.setOnClickListener(v -> showQuickAddDialog());
//    }
//
//    private void showAddMealDialog() {
//        // Create intent for AddMealActivity
//        Intent intent = new Intent(this, AddMealActivity.class);
//        startActivityForResult(intent, ADD_MEAL_REQUEST_CODE);
//    }
//
//    private void showQuickAddDialog() {
//        // TODO: Implement quick add dialog
//    }
//
//    private static final int ADD_MEAL_REQUEST_CODE = 1;
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ADD_MEAL_REQUEST_CODE && resultCode == RESULT_OK) {
//            // Handle the added meal data
//            updateNutritionData(
//                    data.getIntExtra("calories", 0),
//                    data.getIntExtra("protein", 0),
//                    data.getIntExtra("carbs", 0),
//                    data.getIntExtra("fat", 0),
//                    data.getStringExtra("mealType")
//            );
//        }
//    }
//
//    private void updateNutritionData(int calories, int protein, int carbs, int fat, String mealType) {
//        // Update totals
//        currentCalories += calories;
//        currentProtein += protein;
//        currentCarbs += carbs;
//        currentFat += fat;
//
//        // Update meal-specific displays
//        switch (mealType) {
//            case "Breakfast":
//                updateMealDisplay(breakfastCalories, calories);
//                break;
//            case "Lunch":
//                updateMealDisplay(lunchCalories, calories);
//                break;
//            case "Dinner":
//                updateMealDisplay(dinnerCalories, calories);
//                break;
//            case "Snacks":
//                updateMealDisplay(snacksCalories, calories);
//                break;
//        }
//
//        // Update overall displays
//        updateNutritionDisplays();
//    }
//
//    private void updateMealDisplay(TextView mealTextView, int calories) {
//        mealTextView.setText(calories + " cal");
//    }
//
//    private void updateNutritionDisplays() {
//        // Update progress bars
//        caloriesProgress.setProgress((currentCalories * 100) / DAILY_CALORIES_GOAL);
//        proteinProgress.setProgress((currentProtein * 100) / DAILY_PROTEIN_GOAL);
//        carbsProgress.setProgress((currentCarbs * 100) / DAILY_CARBS_GOAL);
//        fatProgress.setProgress((currentFat * 100) / DAILY_FAT_GOAL);
//
//        // Update calories remaining
//        int remainingCalories = DAILY_CALORIES_GOAL - currentCalories;
//        caloriesRemaining.setText(remainingCalories + " calories remaining");
//    }
//
//    private void setupNavigationBar() {
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int itemId = item.getItemId();
//
//                if (itemId == R.id.social) {
//                    startActivity(new Intent(NutritionActivity.this, SocialActivity.class));
//                    return true;
//                } else if (itemId == R.id.workouts) {
//                    startActivity(new Intent(NutritionActivity.this, WorkoutActivity.class));
//                    return true;
//                } else if (itemId == R.id.profile) {
//                    startActivity(new Intent(NutritionActivity.this, UserProfileActivity.class));
//                    return true;
//                } else if (itemId == R.id.nutrition) {
//                    startActivity(new Intent(NutritionActivity.this, NutritionActivity.class));
//                    return true;
//                } else if (itemId == R.id.settings) {
//                    startActivity(new Intent(NutritionActivity.this, SettingsActivity.class));
//                    return true;
//                }
//                return false;
//            }
//        });