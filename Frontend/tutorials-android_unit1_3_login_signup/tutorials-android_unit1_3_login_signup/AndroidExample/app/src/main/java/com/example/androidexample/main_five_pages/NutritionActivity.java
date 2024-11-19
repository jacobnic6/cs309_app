
package com.example.androidexample.main_five_pages;


import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.AddMealActivity;
import com.example.androidexample.R;
import com.example.androidexample.api.MealService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/**
 * Activity for managing and displaying daily nutrition information.
 * Provides meal tracking functionality including logging, editing, and deleting meals
 * while monitoring nutritional goals and progress.
 *
 * Core Features:
 * <ul>
 *   <li>Daily nutrition goal tracking</li>
 *   <li>Meal management (breakfast, lunch, dinner, snacks)</li>
 *   <li>Progress visualization</li>
 *   <li>Real-time nutritional calculations</li>
 * </ul>
 *
 * @author Michael Becker
 * @version 1.0
 * @since 2024-03-20
 *
 */
public class NutritionActivity extends AppCompatActivity {
    /** Tag for logging purposes */
    private static final String TAG = "NutritionActivity";

    /** Request code for adding a new meal */
    private static final int ADD_MEAL_REQUEST_CODE = 1;

    /** Request code for editing an existing meal */
    private static final int EDIT_MEAL_REQUEST_CODE = 2;

    // UI Elements
    /** TextView displaying remaining calories for the day */
    private TextView caloriesRemaining;

    /** Progress bars for different nutritional metrics */
    private ProgressBar caloriesProgress, proteinProgress, carbsProgress, fatProgress;

    /** TextViews displaying calories for each meal type */
    private TextView breakfastCalories, lunchCalories, dinnerCalories, snacksCalories;

    /** Buttons for adding meals */
    private Button addMealButton;

    /** Dialog for showing loading states */
    private ProgressDialog loadingDialog;

    // Nutrition Goals
    /** Daily calorie intake goal */
    private final int DAILY_CALORIES_GOAL = 2000;

    /** Daily protein intake goal in grams */
    private final int DAILY_PROTEIN_GOAL = 150;

    /** Daily carbohydrate intake goal in grams */
    private final int DAILY_CARBS_GOAL = 250;

    /** Daily fat intake goal in grams */
    private final int DAILY_FAT_GOAL = 65;

    // Current Totals
    /** Current total calories consumed */
    private int currentCalories = 0;

    /** Current total protein consumed in grams */
    private int currentProtein = 0;

    /** Current total carbs consumed in grams */
    private int currentCarbs = 0;

    /** Current total fat consumed in grams */
    private int currentFat = 0;

    /** Service for handling meal-related API calls */
    private MealService mealService;

    /** Map to store meals organized by meal type */
    private Map<String, List<JSONObject>> mealsByType = new HashMap<>();

    /**
     * Initializes the activity, sets up UI components, and loads initial meal data.
     *
     * @param savedInstanceState Bundle containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        mealService = new MealService(Volley.newRequestQueue(this));

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Loading...");
        loadingDialog.setCancelable(false);

        initializeViews();
        setupClickListeners();
        setupNavigationBar();
        fetchDailyMeals();
    }

    /**
     * Initializes all UI components and binds them to their respective layout elements.
     */
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

        if (caloriesProgress == null || caloriesRemaining == null) {
            Log.e(TAG, "Some views are not properly initialized.");
            Toast.makeText(this, "Error initializing UI elements", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sets up click listeners for all interactive UI elements.
     */
    private void setupClickListeners() {
        addMealButton.setOnClickListener(v -> showAddMealDialog());

        findViewById(R.id.edit_breakfast_button).setOnClickListener(v -> editMeal("breakfast"));
        findViewById(R.id.delete_breakfast_button).setOnClickListener(v -> confirmDeleteMeal("breakfast"));

        findViewById(R.id.edit_lunch_button).setOnClickListener(v -> editMeal("lunch"));
        findViewById(R.id.delete_lunch_button).setOnClickListener(v -> confirmDeleteMeal("lunch"));

        findViewById(R.id.edit_dinner_button).setOnClickListener(v -> editMeal("dinner"));
        findViewById(R.id.delete_dinner_button).setOnClickListener(v -> confirmDeleteMeal("dinner"));

        findViewById(R.id.edit_snacks_button).setOnClickListener(v -> editMeal("snacks"));
        findViewById(R.id.delete_snacks_button).setOnClickListener(v -> confirmDeleteMeal("snacks"));
    }

    /**
     * Launches the AddMealActivity to add a new meal.
     */
    private void showAddMealDialog() {
        Intent intent = new Intent(this, AddMealActivity.class);
        startActivityForResult(intent, ADD_MEAL_REQUEST_CODE);
    }

    /**
     * Launches the AddMealActivity for a specific meal type.
     *
     * @param mealType The type of meal to edit ("breakfast", "lunch", "dinner", "snacks")
     */
    private void editMeal(String mealType) {
        Intent intent = new Intent(this, AddMealActivity.class);
        intent.putExtra("mealType", mealType);
        intent.putExtra("isEditMode", true);
        startActivityForResult(intent, EDIT_MEAL_REQUEST_CODE);
    }

    /**
     * Asks if you want to confirm deleting a meal before deleting a meal.
     *
     * @param mealType The type of meal to delete
     */
    private void confirmDeleteMeal(String mealType) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Meal")
                .setMessage("Are you sure you want to delete this meal?")
                .setPositiveButton("Yes", (dialog, which) -> deleteMeal(mealType))
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Handles the result returned from AddMealActivity for both adding and editing meals.
     *
     * @param requestCode The original request code
     * @param resultCode Result of the activity
     * @param data Intent containing the meal data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            try {
                if (requestCode == ADD_MEAL_REQUEST_CODE) {
                    String newMealJson = data.getStringExtra("newMeal");
                    String mealType = data.getStringExtra("mealType");
                    if (newMealJson != null) {
                        JSONObject newMeal = new JSONObject(newMealJson);
                        updateTotalsWithNewMeal(newMeal);
                        updateMealTypeDisplay(mealType, newMeal);
                    }
                } else if (requestCode == EDIT_MEAL_REQUEST_CODE) {
                    String updatedMealJson = data.getStringExtra("updatedMeal");
                    if (updatedMealJson != null) {
                        fetchDailyMeals();
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error processing meal data", e);
                fetchDailyMeals();
            }
        }
    }

    /**
     * Updates the total nutrition values with data from a new meal.
     *
     * @param newMeal JSONObject containing the new meal's nutritional information
     * @throws JSONException if there's an error accessing the meal's nutritional values
     */
    private void updateTotalsWithNewMeal(JSONObject newMeal) throws JSONException {
        currentCalories += newMeal.getInt("calories");
        currentProtein += newMeal.getInt("protein");
        currentCarbs += newMeal.getInt("carbs");
        currentFat += newMeal.getInt("fat");

        updateNutritionDisplays();
    }

    /**
     * Updates the calorie display for a specific meal type.
     *
     * @param mealType The type of meal to update
     * @param meal JSONObject containing the meal's calorie information
     * @throws JSONException if there's an error accessing the meal's calorie value
     */
    private void updateMealTypeDisplay(String mealType, JSONObject meal) throws JSONException {
        TextView targetTextView;
        switch (mealType.toLowerCase()) {
            case "breakfast":
                targetTextView = breakfastCalories;
                break;
            case "lunch":
                targetTextView = lunchCalories;
                break;
            case "dinner":
                targetTextView = dinnerCalories;
                break;
            case "snacks":
                targetTextView = snacksCalories;
                break;
            default:
                return;
        }

        String currentText = targetTextView.getText().toString();
        int currentCalories = 0;
        try {
            currentCalories = Integer.parseInt(currentText.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing current calories", e);
        }

        int newTotalCalories = currentCalories + meal.getInt("calories");
        targetTextView.setText(String.format("%d cal", newTotalCalories));
    }

    /**
     * Updates all nutrition-related displays including progress bars and remaining calories.
     */
    private void updateNutritionDisplays() {
        caloriesProgress.setProgress((currentCalories * 100) / DAILY_CALORIES_GOAL);
        proteinProgress.setProgress((currentProtein * 100) / DAILY_PROTEIN_GOAL);
        carbsProgress.setProgress((currentCarbs * 100) / DAILY_CARBS_GOAL);
        fatProgress.setProgress((currentFat * 100) / DAILY_FAT_GOAL);

        int remainingCalories = DAILY_CALORIES_GOAL - currentCalories;
        caloriesRemaining.setText(String.format("%d calories remaining", remainingCalories));

        Log.d(TAG, String.format("Updated totals - Cal: %d, Pro: %d, Carbs: %d, Fat: %d",
                currentCalories, currentProtein, currentCarbs, currentFat));
    }

    /**
     * Convenience method to update macronutrient information for a specific meal type.
     *
     * @param mealType The type of meal to update
     * @param mealData JSONObject containing the meal's nutritional information
     */
    private void updateMealTypeMacros(String mealType, JSONObject mealData) {
        try {
            TextView targetTextView = null;
            switch (mealType.toLowerCase()) {
                case "breakfast":
                    targetTextView = breakfastCalories;
                    break;
                case "lunch":
                    targetTextView = lunchCalories;
                    break;
                case "dinner":
                    targetTextView = dinnerCalories;
                    break;
                case "snacks":
                    targetTextView = snacksCalories;
                    break;
            }

            if (targetTextView != null) {
                int calories = mealData.getInt("calories");
                targetTextView.setText(String.format("%d cal", calories));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error updating meal macros", e);
        }
    }

    /**
     * Sets up the bottom navigation bar with navigation handlers for each destination.
     */
    private void setupNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
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
                return true;
            } else if (itemId == R.id.settings) {
                startActivity(new Intent(NutritionActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    /**
     * Fetches all meal data for the current day.
     * First retrieves total nutritional values, then fetches individual meal details.
     */
    private void fetchDailyMeals() {
        String userId = getUserId();
        String currentDate = getCurrentDate();

        loadingDialog.show();

        mealService.getMealTotals(currentDate, userId, new MealService.MealServiceCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    currentCalories = response.getInt("totalCalories");
                    currentProtein = response.getInt("totalProtein");
                    currentCarbs = response.getInt("totalCarbs");
                    currentFat = response.getInt("totalFat");

                    fetchIndividualMeals();
                    updateNutritionDisplays();
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing meal totals", e);
                    loadingDialog.dismiss();
                    Toast.makeText(NutritionActivity.this, "Error parsing meal totals", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error fetching meal totals: " + error);
                loadingDialog.dismiss();
                Toast.makeText(NutritionActivity.this, "Error fetching meal totals", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fetches and processes individual meals for the current day.
     * Categorizes meals by type and updates the UI in the specific meal category
     */
    private void fetchIndividualMeals() {
        mealService.getMealsByDate(getCurrentDate(), getUserId(), new MealService.MealServiceCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                loadingDialog.dismiss();
                try {
                    int breakfastCals = 0;
                    int lunchCals = 0;
                    int dinnerCals = 0;
                    int snacksCals = 0;

                    JSONArray mealList = response.getJSONArray("mealList");

                    for (int i = 0; i < mealList.length(); i++) {
                        JSONObject meal = mealList.getJSONObject(i);
                        String mealType = meal.getString("mealType").toLowerCase();
                        int calories = meal.getInt("calories");

                        switch (mealType) {
                            case "breakfast":
                                breakfastCals += calories;
                                break;
                            case "lunch":
                                lunchCals += calories;
                                break;
                            case "dinner":
                                dinnerCals += calories;
                                break;
                            case "snacks":
                                snacksCals += calories;
                                break;
                        }
                    }

                    breakfastCalories.setText(String.format("%d cal", breakfastCals));
                    lunchCalories.setText(String.format("%d cal", lunchCals));
                    dinnerCalories.setText(String.format("%d cal", dinnerCals));
                    snacksCalories.setText(String.format("%d cal", snacksCals));

                    storeMealsByType(mealList);

                } catch (Exception e) {
                    Log.e(TAG, "Error updating meal displays", e);
                    Toast.makeText(NutritionActivity.this, "Error updating meal displays", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                loadingDialog.dismiss();
                Log.e(TAG, "Error fetching individual meals: " + error);
                Toast.makeText(NutritionActivity.this, "Error fetching meals", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Stores meals in the mealsByType map, organized by meal type.
     *
     * @param mealList JSONArray containing the meals to be stored
     * @throws JSONException if there's an error parsing the meal data
     */
    private void storeMealsByType(JSONArray mealList) throws JSONException {
        mealsByType.clear();
        mealsByType.put("breakfast", new ArrayList<>());
        mealsByType.put("lunch", new ArrayList<>());
        mealsByType.put("dinner", new ArrayList<>());
        mealsByType.put("snacks", new ArrayList<>());

        for (int i = 0; i < mealList.length(); i++) {
            JSONObject meal = mealList.getJSONObject(i);
            String mealType = meal.getString("mealType").toLowerCase();
            if (mealsByType.containsKey(mealType)) {
                mealsByType.get(mealType).add(meal);
            }
        }
    }

    /**
     * Retrieves meals of a specific type from the stored meals map.
     *
     * @param mealType The type of meals to retrieve
     * @return List of JSONObjects containing the meals of said type
     */
    private List<JSONObject> getMealsByType(String mealType) {
        return mealsByType.getOrDefault(mealType.toLowerCase(), new ArrayList<>());
    }

    /**
     * Deletes all meals of a type and updates UI.
     *
     * @param mealType The type of meals to delete
     */
    private void deleteMeal(String mealType) {
        loadingDialog.show();

        List<JSONObject> mealsOfType = getMealsByType(mealType);

        switch(mealType.toLowerCase()) {
            case "breakfast":
                breakfastCalories.setText("0 cal");
                break;
            case "lunch":
                lunchCalories.setText("0 cal");
                break;
            case "dinner":
                dinnerCalories.setText("0 cal");
                break;
            case "snacks":
                snacksCalories.setText("0 cal");
                break;
        }

        mealsByType.get(mealType.toLowerCase()).clear();

        try {
            int deletedCalories = 0;
            int deletedProtein = 0;
            int deletedCarbs = 0;
            int deletedFat = 0;

            for (JSONObject meal : mealsOfType) {
                deletedCalories += meal.getInt("calories");
                deletedProtein += meal.getInt("protein");
                deletedCarbs += meal.getInt("carbs");
                deletedFat += meal.getInt("fat");
            }

            currentCalories -= deletedCalories;
            currentProtein -= deletedProtein;
            currentCarbs -= deletedCarbs;
            currentFat -= deletedFat;

            updateNutritionDisplays();
            loadingDialog.dismiss();
            Toast.makeText(NutritionActivity.this, "Meal deleted successfully", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            Log.e(TAG, "Error calculating deleted meal totals", e);
            loadingDialog.dismiss();
            Toast.makeText(NutritionActivity.this, "Error updating meal totals", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Updates the calorie display for a specific meal TextView.
     *
     * @param mealTextView Updates
     * @param mealData JSONObject containing the meal data, or null to reset to 0
     */
    private void updateMealDisplay(TextView mealTextView, JSONObject mealData) {
        if (mealData != null) {
            int calories = mealData.optInt("calories", 0);
            mealTextView.setText(String.format("%d cal", calories));
        } else {
            mealTextView.setText("0 cal");
        }
    }

    /**
     * Retrieves the current user's ID.
     *
     * @return String containing the user ID
     */
    private String getUserId() {
        return "Bauer6445";
    }

    /**
     * Gets the current date formatted as yyyy-MM-dd.
     *
     * @return String containing the formatted current date
     */
    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}