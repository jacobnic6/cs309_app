package com.example.androidexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;
import com.example.androidexample.api.FoodSearchResponse;
import com.example.androidexample.api.MealService;
import com.example.androidexample.api.USDAApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AddMealActivity extends AppCompatActivity {
    private static final String TAG = "AddMealActivity";


    private AutoCompleteTextView foodNameInput;
    private Spinner mealTypeSpinner;
    private EditText servingSizeInput;
    private EditText caloriesInput;
    private EditText proteinInput;
    private EditText carbsInput;
    private EditText fatInput;
    private Button saveButton;
    private Button cancelButton;
    private ProgressDialog loadingDialog;

    // API and Data
    private Timer searchTimer;
    private USDAApiClient apiClient;
    private MealService mealService;
    private List<FoodSearchResponse.Food> searchResults;
    private boolean isEditMode;
    private String mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        // Initialize APIs
        apiClient = USDAApiClient.getInstance();
        mealService = new MealService(Volley.newRequestQueue(this));
        searchResults = new ArrayList<>();

        // Initialize loading dialog
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Saving meal...");
        loadingDialog.setCancelable(false);

        initializeViews();
        setupSpinner();
        setupButtons();
        setupFoodSearch();

        // Check if this is edit mode
        isEditMode = getIntent().getBooleanExtra("isEditMode", false);
        if (isEditMode) {
            mealType = getIntent().getStringExtra("mealType");
            setTitle("Edit " + mealType.substring(0, 1).toUpperCase() + mealType.substring(1));
            loadMealDataForEditing(mealType);
        } else {
            setTitle("Add New Meal");
        }
    }

    private void initializeViews() {
        foodNameInput = findViewById(R.id.food_name);
        mealTypeSpinner = findViewById(R.id.meal_type_spinner);
        servingSizeInput = findViewById(R.id.serving_size);
        caloriesInput = findViewById(R.id.calories);
        proteinInput = findViewById(R.id.protein);
        carbsInput = findViewById(R.id.carbs);
        fatInput = findViewById(R.id.fat);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Breakfast", "Lunch", "Dinner", "Snacks"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(adapter);

        if (isEditMode && mealType != null) {
            int position = adapter.getPosition(mealType.substring(0, 1).toUpperCase() + mealType.substring(1));
            mealTypeSpinner.setSelection(position);
            mealTypeSpinner.setEnabled(false); // Disable changing meal type in edit mode
        }
    }

    private void setupButtons() {
        saveButton.setOnClickListener(v -> validateAndSaveMeal());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void setupFoodSearch() {
        foodNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchTimer != null) {
                    searchTimer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 3) {
                    searchTimer = new Timer();
                    searchTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            searchFood(s.toString());
                        }
                    }, 600);
                }
            }
        });

        foodNameInput.setOnItemClickListener((parent, view, position, id) -> {
            FoodSearchResponse.Food selectedFood = searchResults.get(position);
            populateNutritionData(selectedFood);
        });
    }

    private void searchFood(String query) {
        apiClient.searchFoods(query, new USDAApiClient.USDAApiCallback() {
            @Override
            public void onSuccess(FoodSearchResponse response) {
                runOnUiThread(() -> {
                    searchResults = response.getFoods();
                    List<String> foodNames = new ArrayList<>();
                    for (FoodSearchResponse.Food food : searchResults) {
                        foodNames.add(food.getDescription());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            AddMealActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            foodNames
                    );
                    foodNameInput.setAdapter(adapter);
                    if (foodNames.size() > 0) {
                        foodNameInput.showDropDown();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(AddMealActivity.this, error, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void populateNutritionData(FoodSearchResponse.Food food) {
        servingSizeInput.setText(food.getServingSize() + " " + food.getServingSizeUnit());

        for (FoodSearchResponse.Nutrient nutrient : food.getFoodNutrients()) {
            switch (nutrient.getNutrientName().toLowerCase()) {
                case "energy":
                    caloriesInput.setText(String.valueOf((int) nutrient.getValue()));
                    break;
                case "protein":
                    proteinInput.setText(String.valueOf((int) nutrient.getValue()));
                    break;
                case "carbohydrate, by difference":
                    carbsInput.setText(String.valueOf((int) nutrient.getValue()));
                    break;
                case "total lipid (fat)":
                    fatInput.setText(String.valueOf((int) nutrient.getValue()));
                    break;
            }
        }
    }

    private void loadMealDataForEditing(String mealType) {
        String userId = getUserId();
        String currentDate = getCurrentDate();

        loadingDialog.show();
        mealService.getMealsByDate(currentDate, userId, new MealService.MealServiceCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                loadingDialog.dismiss();
                try {
                    Log.d(TAG, "Got response for editing: " + response.toString());
                    JSONArray mealList = response.getJSONArray("mealList");

                    // Find the meal matching the specified mealType
                    for (int i = 0; i < mealList.length(); i++) {
                        JSONObject meal = mealList.getJSONObject(i);
                        if (meal.getString("mealType").equalsIgnoreCase(mealType)) {
                            populateFieldsForEditing(meal); // Populate fields with the selected meal
                            return;
                        }
                    }

                    showError("No meal found for " + mealType);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing meal data for editing: " + e.getMessage());
                    showError("Error loading meal data");
                }
            }

            @Override
            public void onError(String error) {
                loadingDialog.dismiss();
                showError("Error loading meal data: " + error);
            }
        });
    }

    private void populateFieldsForEditing(JSONObject meal) {
        try {
            foodNameInput.setText(meal.optString("foodName", ""));
            servingSizeInput.setText(meal.optString("servingSize", ""));
            caloriesInput.setText(String.valueOf(meal.optInt("calories", 0)));
            proteinInput.setText(String.valueOf(meal.optInt("protein", 0)));
            carbsInput.setText(String.valueOf(meal.optInt("carbs", 0)));
            fatInput.setText(String.valueOf(meal.optInt("fat", 0)));

            // Set the meal type in the spinner
            String mealType = meal.optString("mealType", "").toLowerCase();
            int spinnerPosition = ((ArrayAdapter) mealTypeSpinner.getAdapter()).getPosition(mealType.substring(0, 1).toUpperCase() + mealType.substring(1));
            mealTypeSpinner.setSelection(spinnerPosition);
        } catch (Exception e) {
            Log.e(TAG, "Error populating fields for editing: " + e.getMessage());
            showError("Error loading meal data");
        }
    }


    private void validateAndSaveMeal() {
        if (foodNameInput.getText().toString().trim().isEmpty()) {
            foodNameInput.setError("Food name is required");
            return;
        }

        if (caloriesInput.getText().toString().trim().isEmpty()) {
            caloriesInput.setError("Calories are required");
            return;
        }

        try {
            JSONObject mealData = new JSONObject();
            mealData.put("foodName", foodNameInput.getText().toString().trim());
            mealData.put("servingSize", servingSizeInput.getText().toString().trim());
            mealData.put("calories", Integer.parseInt(caloriesInput.getText().toString().trim()));
            mealData.put("protein", proteinInput.getText().toString().isEmpty() ? 0 :
                    Integer.parseInt(proteinInput.getText().toString().trim()));
            mealData.put("carbs", carbsInput.getText().toString().isEmpty() ? 0 :
                    Integer.parseInt(carbsInput.getText().toString().trim()));
            mealData.put("fat", fatInput.getText().toString().isEmpty() ? 0 :
                    Integer.parseInt(fatInput.getText().toString().trim()));

            String selectedMealType = mealTypeSpinner.getSelectedItem().toString().toLowerCase();
            mealData.put("mealType", selectedMealType);

            Log.d(TAG, "Saving meal data: " + mealData.toString());

            String userId = getUserId();
            String currentDate = getCurrentDate();

            loadingDialog.show();

            if (isEditMode) {
                mealService.updateMeal(currentDate, userId, mealType, mealData, new MealService.MealServiceCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedMeal", mealData.toString());
                        resultIntent.putExtra("mealType", mealType);
                        Log.d(TAG, "Setting result for edit with type: " + mealType);
                        setResult(RESULT_OK, resultIntent);
                        handleSaveSuccess("Meal updated successfully");
                    }

                    @Override
                    public void onError(String error) {
                        handleSaveError("Error updating meal: " + error);
                    }
                });
            } else {
                mealService.addMeal(currentDate, userId, mealData, new MealService.MealServiceCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("newMeal", mealData.toString());
                        resultIntent.putExtra("mealType", selectedMealType);
                        Log.d(TAG, "Setting result for new meal with type: " + selectedMealType);
                        setResult(RESULT_OK, resultIntent);
                        handleSaveSuccess("Meal added successfully");
                    }

                    @Override
                    public void onError(String error) {
                        handleSaveError("Error adding meal: " + error);
                    }
                });
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error creating meal JSON: " + e.getMessage());
            showError("Error saving meal data");
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing numbers: " + e.getMessage());
            showError("Please enter valid numbers");
        }
    }

    private void handleSaveSuccess(String message) {
        runOnUiThread(() -> {
            loadingDialog.dismiss();
            Toast.makeText(AddMealActivity.this, message, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void handleSaveError(String error) {
        runOnUiThread(() -> {
            loadingDialog.dismiss();
            showError(error);
        });
    }

    private void showError(String message) {
        runOnUiThread(() ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        );
    }

    private String getUserId() {
        // TODO: Implement actual user ID retrieval from your authentication system
        return "msbecker"; // Updated to match your API
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchTimer != null) {
            searchTimer.cancel();
        }
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}