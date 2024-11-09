package com.example.androidexample.main_five_pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.AddMealActivity;
import com.example.androidexample.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NutritionActivity extends AppCompatActivity {
    // UI Elements
    private TextView caloriesRemaining;
    private ProgressBar caloriesProgress, proteinProgress, carbsProgress, fatProgress;
    private TextView breakfastCalories, lunchCalories, dinnerCalories, snacksCalories;
    private Button addMealButton, scanBarcodeButton, quickAddButton;

    // Nutrition Goals
    private final int DAILY_CALORIES_GOAL = 2000;
    private final int DAILY_PROTEIN_GOAL = 150;
    private final int DAILY_CARBS_GOAL = 250;
    private final int DAILY_FAT_GOAL = 65;

    // Current Totals
    private int currentCalories = 0;
    private int currentProtein = 0;
    private int currentCarbs = 0;
    private int currentFat = 0;

    // API Related
    private RequestQueue requestQueue;
    private static final String BASE_URL = "YOUR_API_URL"; // Replace with your API URL
    private static final int ADD_MEAL_REQUEST_CODE = 1;
    private static final int EDIT_MEAL_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        // Initialize RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Initialize UI elements
        initializeViews();
        setupClickListeners();
        setupNavigationBar();

        // Fetch initial data
        fetchDailyMeals();
    }

    private void initializeViews() {
        // Initialize Progress Bars
        caloriesProgress = findViewById(R.id.calories_progress);
        proteinProgress = findViewById(R.id.protein_progress);
        carbsProgress = findViewById(R.id.carbs_progress);
        fatProgress = findViewById(R.id.fat_progress);

        // Initialize TextViews
        caloriesRemaining = findViewById(R.id.calories_remaining);
        breakfastCalories = findViewById(R.id.breakfast_calories);
        lunchCalories = findViewById(R.id.lunch_calories);
        dinnerCalories = findViewById(R.id.dinner_calories);
        snacksCalories = findViewById(R.id.snacks_calories);

        // Initialize Buttons
        addMealButton = findViewById(R.id.add_meal_button);
        quickAddButton = findViewById(R.id.quick_add_button);

        // Log to ensure views are initialized correctly
        if (caloriesProgress == null || caloriesRemaining == null) {
            Log.e("NutritionActivity", "Some views are not properly initialized.");
            Toast.makeText(this, "Error initializing UI elements", Toast.LENGTH_LONG).show();
        }
    }

    private void setupClickListeners() {
        addMealButton.setOnClickListener(v -> showAddMealDialog());

        // Listeners for Edit and Delete actions
        findViewById(R.id.edit_breakfast_button).setOnClickListener(v -> editMeal("breakfast"));
        findViewById(R.id.delete_breakfast_button).setOnClickListener(v -> confirmDeleteMeal("breakfastId")); // Replace with actual meal ID

        // Repeat for lunch, dinner, and snacks
        findViewById(R.id.edit_lunch_button).setOnClickListener(v -> editMeal("lunch"));
        findViewById(R.id.delete_lunch_button).setOnClickListener(v -> confirmDeleteMeal("lunchId"));

        findViewById(R.id.edit_dinner_button).setOnClickListener(v -> editMeal("dinner"));
        findViewById(R.id.delete_dinner_button).setOnClickListener(v -> confirmDeleteMeal("dinnerId"));

        findViewById(R.id.edit_snacks_button).setOnClickListener(v -> editMeal("snacks"));
        findViewById(R.id.delete_snacks_button).setOnClickListener(v -> confirmDeleteMeal("snacksId"));
    }

    private void showAddMealDialog() {
        Intent intent = new Intent(this, AddMealActivity.class);
        startActivityForResult(intent, ADD_MEAL_REQUEST_CODE);
    }

    private void editMeal(String mealType) {
        Intent intent = new Intent(this, AddMealActivity.class);
        intent.putExtra("mealType", mealType);
        intent.putExtra("isEditMode", true);
        startActivityForResult(intent, EDIT_MEAL_REQUEST_CODE);
    }

    private void confirmDeleteMeal(String mealId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Meal")
                .setMessage("Are you sure you want to delete this meal?")
                .setPositiveButton("Yes", (dialog, which) -> deleteMeal(mealId))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteMeal(String mealId) {
        String url = BASE_URL + "/meals/" + mealId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    Toast.makeText(this, "Meal deleted successfully", Toast.LENGTH_SHORT).show();
                    fetchDailyMeals(); // Refresh the data
                },
                error -> {
                    Log.e("NutritionActivity", "Error deleting meal: " + error.getMessage());
                    Toast.makeText(this, "Error deleting meal", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_MEAL_REQUEST_CODE || requestCode == EDIT_MEAL_REQUEST_CODE) {
                fetchDailyMeals(); // Refresh the data
            }
        }
    }

    private void setupNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

    private void fetchDailyMeals() {
        String url = BASE_URL + "/meals/" + getCurrentDate();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        currentCalories = response.getInt("totalCalories");
                        currentProtein = response.getInt("totalProtein");
                        currentCarbs = response.getInt("totalCarbs");
                        currentFat = response.getInt("totalFat");

                        updateMealDisplay(breakfastCalories, response.getInt("breakfastCalories"));
                        updateMealDisplay(lunchCalories, response.getInt("lunchCalories"));
                        updateMealDisplay(dinnerCalories, response.getInt("dinnerCalories"));
                        updateMealDisplay(snacksCalories, response.getInt("snacksCalories"));

                        updateNutritionDisplays();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing meal data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("NutritionActivity", "Error fetching meals: " + error.getMessage());
                    Toast.makeText(this, "Error fetching meals", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(request);
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void updateMealDisplay(TextView mealTextView, int calories) {
        mealTextView.setText(calories + " cal");
    }

    private void updateNutritionDisplays() {
        caloriesProgress.setProgress((currentCalories * 100) / DAILY_CALORIES_GOAL);
        proteinProgress.setProgress((currentProtein * 100) / DAILY_PROTEIN_GOAL);
        carbsProgress.setProgress((currentCarbs * 100) / DAILY_CARBS_GOAL);
        fatProgress.setProgress((currentFat * 100) / DAILY_FAT_GOAL);

        int remainingCalories = DAILY_CALORIES_GOAL - currentCalories;
        caloriesRemaining.setText(remainingCalories + " calories remaining");
    }
}
