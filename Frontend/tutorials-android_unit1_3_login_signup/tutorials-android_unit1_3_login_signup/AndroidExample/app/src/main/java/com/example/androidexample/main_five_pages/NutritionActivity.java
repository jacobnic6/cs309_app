package com.example.androidexample.main_five_pages;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.example.androidexample.nutrition.NutritionCalendarActivity;
import com.example.androidexample.SessionManager;
import com.example.androidexample.nutrition.NutritionCalendarActivity;

public class NutritionActivity extends AppCompatActivity {
    private static final String TAG = "NutritionActivity";
    private static final int ADD_MEAL_REQUEST_CODE = 1;
    private static final int EDIT_MEAL_REQUEST_CODE = 2;

    // UI Elements
    private TextView caloriesRemaining;
    private ProgressBar caloriesProgress, proteinProgress, carbsProgress, fatProgress;
    private TextView breakfastCalories, lunchCalories, dinnerCalories, snacksCalories;
    private Button addMealButton, viewHistoryButton;
    private ImageButton prevDateButton, nextDateButton;
    private TextView dateDisplay;
    private ProgressDialog loadingDialog;

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

    // Services and Data
    private MealService mealService;
    private Map<String, List<JSONObject>> mealsByType = new HashMap<>();
    private Date currentDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        mealService = new MealService(Volley.newRequestQueue(this));
        initializeLoadingDialog();
        initializeViews();
        setupClickListeners();
        setupNavigationBar();
        updateDateDisplay();
        fetchDailyMeals();
    }

    private void initializeLoadingDialog() {
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Loading...");
        loadingDialog.setCancelable(false);
    }

    private void initializeViews() {
        // Progress Bars
        caloriesProgress = findViewById(R.id.calories_progress);
        proteinProgress = findViewById(R.id.protein_progress);
        carbsProgress = findViewById(R.id.carbs_progress);
        fatProgress = findViewById(R.id.fat_progress);

        // TextViews
        caloriesRemaining = findViewById(R.id.calories_remaining);
        breakfastCalories = findViewById(R.id.breakfast_calories);
        lunchCalories = findViewById(R.id.lunch_calories);
        dinnerCalories = findViewById(R.id.dinner_calories);
        snacksCalories = findViewById(R.id.snacks_calories);

        // Buttons
        addMealButton = findViewById(R.id.add_meal_button);
        viewHistoryButton = findViewById(R.id.view_history_button);
        prevDateButton = findViewById(R.id.prev_date_button);
        nextDateButton = findViewById(R.id.next_date_button);
        dateDisplay = findViewById(R.id.date_display);

        if (caloriesProgress == null || caloriesRemaining == null) {
            Log.e(TAG, "Some views are not properly initialized.");
            Toast.makeText(this, "Error initializing UI elements", Toast.LENGTH_LONG).show();
        }
    }

    private void setupClickListeners() {
        // Meal management buttons
        addMealButton.setOnClickListener(v -> showAddMealDialog());
        viewHistoryButton.setOnClickListener(v -> showMealHistory());

        // Date navigation
        prevDateButton.setOnClickListener(v -> navigateDate(-1));
        nextDateButton.setOnClickListener(v -> navigateDate(1));

        // Meal editing buttons
        setupMealTypeButtons("breakfast");
        setupMealTypeButtons("lunch");
        setupMealTypeButtons("dinner");
        setupMealTypeButtons("snacks");
    }

    private void setupMealTypeButtons(String mealType) {
        int editId = getResources().getIdentifier("edit_" + mealType + "_button", "id", getPackageName());
        int deleteId = getResources().getIdentifier("delete_" + mealType + "_button", "id", getPackageName());

        findViewById(editId).setOnClickListener(v -> editMeal(mealType));
        findViewById(deleteId).setOnClickListener(v -> confirmDeleteMeal(mealType));
    }

    private void navigateDate(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DAY_OF_MONTH, offset);
        currentDate = cal.getTime();

        updateDateDisplay();
        fetchDailyMeals();
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        dateDisplay.setText(sdf.format(currentDate));
    }

    private void showAddMealDialog() {
        Intent intent = new Intent(this, AddMealActivity.class);
        startActivityForResult(intent, ADD_MEAL_REQUEST_CODE);
    }

    private void showMealHistory() {
        Intent intent = new Intent(this, NutritionCalendarActivity.class);
        startActivity(intent);
    }

    private void editMeal(String mealType) {
        Intent intent = new Intent(this, AddMealActivity.class);
        intent.putExtra("mealType", mealType);
        intent.putExtra("isEditMode", true);
        intent.putExtra("date", getCurrentDate());
        startActivityForResult(intent, EDIT_MEAL_REQUEST_CODE);
    }

    private void confirmDeleteMeal(String mealType) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Meal")
                .setMessage("Are you sure you want to delete this meal?")
                .setPositiveButton("Yes", (dialog, which) -> deleteMeal(mealType))
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            fetchDailyMeals(); // Refresh data after any meal changes
        }
    }

    private void updateNutritionDisplays() {
        // Update progress bars
        caloriesProgress.setProgress((currentCalories * 100) / DAILY_CALORIES_GOAL);
        proteinProgress.setProgress((currentProtein * 100) / DAILY_PROTEIN_GOAL);
        carbsProgress.setProgress((currentCarbs * 100) / DAILY_CARBS_GOAL);
        fatProgress.setProgress((currentFat * 100) / DAILY_FAT_GOAL);

        // Update remaining calories
        int remainingCalories = DAILY_CALORIES_GOAL - currentCalories;
        caloriesRemaining.setText(String.format("%d calories remaining", remainingCalories));
    }

    private void fetchDailyMeals() {
        loadingDialog.show();
        String userId = getUserId();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate);

        mealService.getMealTotals(date, userId, new MealService.MealServiceCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    updateNutritionTotals(response);
                    fetchIndividualMeals();
                } catch (JSONException e) {
                    handleError("Error parsing meal totals", e);
                }
            }

            @Override
            public void onError(String error) {
                handleError("Error fetching meal totals: " + error, null);
            }
        });
    }

    private void updateNutritionTotals(JSONObject response) throws JSONException {
        currentCalories = response.getInt("totalCalories");
        currentProtein = response.getInt("totalProtein");
        currentCarbs = response.getInt("totalCarbs");
        currentFat = response.getInt("totalFat");
        updateNutritionDisplays();
    }

    private void fetchIndividualMeals() {
        mealService.getMealsByDate(getCurrentDate(), getUserId(), new MealService.MealServiceCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                loadingDialog.dismiss();
                try {
                    processIndividualMeals(response);
                } catch (Exception e) {
                    handleError("Error updating meal displays", e);
                }
            }

            @Override
            public void onError(String error) {
                handleError("Error fetching meals: " + error, null);
            }
        });
    }

    private void processIndividualMeals(JSONObject response) throws JSONException {
        JSONArray mealList = response.getJSONArray("mealList");
        Map<String, Integer> mealCalories = new HashMap<>();
        mealCalories.put("breakfast", 0);
        mealCalories.put("lunch", 0);
        mealCalories.put("dinner", 0);
        mealCalories.put("snacks", 0);

        for (int i = 0; i < mealList.length(); i++) {
            JSONObject meal = mealList.getJSONObject(i);
            String type = meal.getString("mealType").toLowerCase();
            mealCalories.put(type, mealCalories.get(type) + meal.getInt("calories"));
        }

        updateMealDisplays(mealCalories);
        storeMealsByType(mealList);
    }

    private void updateMealDisplays(Map<String, Integer> mealCalories) {
        breakfastCalories.setText(String.format("%d cal", mealCalories.get("breakfast")));
        lunchCalories.setText(String.format("%d cal", mealCalories.get("lunch")));
        dinnerCalories.setText(String.format("%d cal", mealCalories.get("dinner")));
        snacksCalories.setText(String.format("%d cal", mealCalories.get("snacks")));
    }

    private void handleError(String message, Exception e) {
        if (e != null) {
            Log.e(TAG, message, e);
        } else {
            Log.e(TAG, message);
        }
        loadingDialog.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

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

    private void deleteMeal(String mealType) {
        List<JSONObject> mealsToDelete = getMealsByType(mealType);
        if (mealsToDelete.isEmpty()) {
            return;
        }

        loadingDialog.show();
        try {
            for (JSONObject meal : mealsToDelete) {
                // Call your delete API here
                // For now, just updating UI
                updateUIAfterDelete(mealType, meal);
            }
        } catch (JSONException e) {
            handleError("Error deleting meals", e);
        }
    }

    private void updateUIAfterDelete(String mealType, JSONObject meal) throws JSONException {
        currentCalories -= meal.getInt("calories");
        currentProtein -= meal.getInt("protein");
        currentCarbs -= meal.getInt("carbs");
        currentFat -= meal.getInt("fat");

        updateNutritionDisplays();
        TextView targetTextView = getMealTypeTextView(mealType);
        if (targetTextView != null) {
            targetTextView.setText("0 cal");
        }
    }

    private TextView getMealTypeTextView(String mealType) {
        switch (mealType.toLowerCase()) {
            case "breakfast": return breakfastCalories;
            case "lunch": return lunchCalories;
            case "dinner": return dinnerCalories;
            case "snacks": return snacksCalories;
            default: return null;
        }
    }

    private void setupNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;

            if (itemId == R.id.social) {
                intent = new Intent(this, SocialActivity.class);
            } else if (itemId == R.id.workouts) {
                intent = new Intent(this, WorkoutActivity.class);
            } else if (itemId == R.id.profile) {
                intent = new Intent(this, UserProfileActivity.class);
            } else if (itemId == R.id.nutrition) {
                return true; // Already here
            } else if (itemId == R.id.settings) {
                intent = new Intent(this, SettingsActivity.class);
            }

            if (intent != null) {
                intent.putExtra("Username", getUserId());
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private List<JSONObject> getMealsByType(String mealType) {
        return mealsByType.getOrDefault(mealType.toLowerCase(), new ArrayList<>());
    }

    private String getUserId() {
        return "msbecker"; // Replace with actual user ID retrieval
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate);
    }
}