package com.example.androidexample.main_five_pages;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.AddMealActivity;
import com.example.androidexample.R;
import com.example.androidexample.api.MealService;
import com.example.androidexample.utils.CalorieCalculator;
import com.example.androidexample.utils.NutritionGoals;
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
    private Button addMealButton, viewHistoryButton, createEmptyMealListButton;
    private ImageButton prevDateButton, nextDateButton;
    private TextView dateDisplay;
    private ProgressDialog loadingDialog;
    private NutritionGoals nutritionGoals;
    private Button setNutritionGoalsButton;
    private static final String PREFS_NAME = "NutritionPrefs";
    private static final String KEY_ACTIVITY_LEVEL = "activityLevel";
    private static final String KEY_FITNESS_GOAL = "fitnessGoal";
    private static final String KEY_CALORIES_GOAL = "caloriesGoal";

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
        createEmptyMealListButton = findViewById(R.id.create_empty_meal_list_button);
        setNutritionGoalsButton = findViewById(R.id.set_nutrition_goals_button);

        if (caloriesProgress == null || caloriesRemaining == null) {
            Log.e(TAG, "Some views are not properly initialized.");
            Toast.makeText(this, "Error initializing UI elements", Toast.LENGTH_LONG).show();
        }
    }

    private void setupClickListeners() {
        // Meal management buttons
        addMealButton.setOnClickListener(v -> showAddMealDialog());
        viewHistoryButton.setOnClickListener(v -> showMealHistory());
        createEmptyMealListButton.setOnClickListener(v -> createEmptyMealList());
        setNutritionGoalsButton.setOnClickListener(v -> showNutritionGoalsDialog());
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
    private void showNutritionGoalsDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_set_nutrition_goals, null);
        EditText weightInput = dialogView.findViewById(R.id.weight_input);
        Spinner activitySpinner = dialogView.findViewById(R.id.activity_level_spinner);
        Spinner goalSpinner = dialogView.findViewById(R.id.goal_spinner);
        TextView caloriesText = dialogView.findViewById(R.id.calories_goal_text);
        TextView proteinText = dialogView.findViewById(R.id.protein_goal_text);
        TextView carbsText = dialogView.findViewById(R.id.carbs_goal_text);
        TextView fatText = dialogView.findViewById(R.id.fat_goal_text);

        // Setup spinners
        setupActivitySpinner(activitySpinner);
        setupGoalSpinner(goalSpinner);

        // Load current values
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String currentActivity = prefs.getString(KEY_ACTIVITY_LEVEL,
                CalorieCalculator.ActivityLevel.MODERATE_ACTIVITY.name());
        String currentGoal = prefs.getString(KEY_FITNESS_GOAL,
                CalorieCalculator.Goal.MAINTAIN.name());

        // Set current values
        activitySpinner.setSelection(getActivityLevelPosition(currentActivity));
        goalSpinner.setSelection(getGoalPosition(currentGoal));

        // Setup weight change listener
        weightInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateNutritionPreview(s.toString(),
                        (CalorieCalculator.ActivityLevel) activitySpinner.getSelectedItem(),
                        (CalorieCalculator.Goal) goalSpinner.getSelectedItem(),
                        caloriesText, proteinText, carbsText, fatText);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Setup spinner change listeners
        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateNutritionPreview(weightInput.getText().toString(),
                        (CalorieCalculator.ActivityLevel) activitySpinner.getSelectedItem(),
                        (CalorieCalculator.Goal) goalSpinner.getSelectedItem(),
                        caloriesText, proteinText, carbsText, fatText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        goalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateNutritionPreview(weightInput.getText().toString(),
                        (CalorieCalculator.ActivityLevel) activitySpinner.getSelectedItem(),
                        (CalorieCalculator.Goal) goalSpinner.getSelectedItem(),
                        caloriesText, proteinText, carbsText, fatText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Create and show dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Set Nutrition Goals")
                .setView(dialogView)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    if (weightInput.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please enter your weight", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    saveNutritionGoals(
                            Double.parseDouble(weightInput.getText().toString()),
                            (CalorieCalculator.ActivityLevel) activitySpinner.getSelectedItem(),
                            (CalorieCalculator.Goal) goalSpinner.getSelectedItem()
                    );
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }

    private void setupActivitySpinner(Spinner spinner) {
        ArrayAdapter<CalorieCalculator.ActivityLevel> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                        CalorieCalculator.ActivityLevel.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupGoalSpinner(Spinner spinner) {
        ArrayAdapter<CalorieCalculator.Goal> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                        CalorieCalculator.Goal.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void updateNutritionPreview(String weightStr,
                                        CalorieCalculator.ActivityLevel activity,
                                        CalorieCalculator.Goal goal,
                                        TextView caloriesText,
                                        TextView proteinText,
                                        TextView carbsText,
                                        TextView fatText) {
        try {
            if (!weightStr.isEmpty()) {
                double weight = Double.parseDouble(weightStr);
                NutritionGoals preview = CalorieCalculator.calculateNutritionGoals(weight, activity, goal);

                caloriesText.setText(String.format("Daily Calories: %d",
                        preview.getDailyCaloriesGoal()));
                proteinText.setText(String.format("Protein: %dg",
                        preview.getProteinGoal()));
                carbsText.setText(String.format("Carbs: %dg",
                        preview.getCarbsGoal()));
                fatText.setText(String.format("Fat: %dg",
                        preview.getFatGoal()));
            }
        } catch (NumberFormatException e) {
            // Ignore invalid input
        }
    }

    private void saveNutritionGoals(double weight,
                                    CalorieCalculator.ActivityLevel activity,
                                    CalorieCalculator.Goal goal) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_ACTIVITY_LEVEL, activity.name());
        editor.putString(KEY_FITNESS_GOAL, goal.name());

        nutritionGoals = CalorieCalculator.calculateNutritionGoals(weight, activity, goal);
        editor.putInt(KEY_CALORIES_GOAL, nutritionGoals.getDailyCaloriesGoal());
        editor.apply();

        Toast.makeText(this, "Nutrition goals updated!", Toast.LENGTH_SHORT).show();
        updateNutritionDisplays();
    }

    private int getActivityLevelPosition(String activityLevel) {
        CalorieCalculator.ActivityLevel[] levels = CalorieCalculator.ActivityLevel.values();
        for (int i = 0; i < levels.length; i++) {
            if (levels[i].name().equals(activityLevel)) {
                return i;
            }
        }
        return 0;
    }

    private int getGoalPosition(String goal) {
        CalorieCalculator.Goal[] goals = CalorieCalculator.Goal.values();
        for (int i = 0; i < goals.length; i++) {
            if (goals[i].name().equals(goal)) {
                return i;
            }
        }
        return 0;
    }
    private void createEmptyMealList() {
        loadingDialog.show();
        String userId = getUserId();
        String date = getCurrentDate();

        mealService.createEmptyMealList(userId, date, new MealService.MealServiceCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                loadingDialog.dismiss();
                Toast.makeText(NutritionActivity.this, "Empty meal list created successfully!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Empty meal list response: " + response.toString());
            }

            @Override
            public void onError(String error) {
                loadingDialog.dismiss();
                Toast.makeText(NutritionActivity.this, "Error creating empty meal list: " + error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error creating empty meal list: " + error);
            }
        });
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