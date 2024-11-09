package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.api.FoodSearchResponse;
import com.example.androidexample.api.USDAApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AddMealActivity extends AppCompatActivity {
    private AutoCompleteTextView foodNameInput;
    private Spinner mealTypeSpinner;
    private EditText servingSizeInput;
    private EditText caloriesInput;
    private EditText proteinInput;
    private EditText carbsInput;
    private EditText fatInput;
    private Button saveButton;
    private Button cancelButton;

    private Timer searchTimer;
    private USDAApiClient apiClient;
    private List<FoodSearchResponse.Food> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        apiClient = USDAApiClient.getInstance();
        searchResults = new ArrayList<>();

        initializeViews();
        setupSpinner();
        setupButtons();
        setupFoodSearch();

        // Check if this is edit mode
        boolean isEditMode = getIntent().getBooleanExtra("isEditMode", false);
        if (isEditMode) {
            String mealType = getIntent().getStringExtra("mealType");
            loadMealDataForEditing(mealType);
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
        // Fetch meal data from the API or local storage
        if (mealType.equals("breakfast")) {
            foodNameInput.setText("Oatmeal");
            servingSizeInput.setText("1 cup");
            caloriesInput.setText("150");
            proteinInput.setText("5");
            carbsInput.setText("27");
            fatInput.setText("3");
        }
        // Load data for other meal types similarly
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
            String foodName = foodNameInput.getText().toString();
            String servingSize = servingSizeInput.getText().toString();
            int calories = Integer.parseInt(caloriesInput.getText().toString());
            float protein = proteinInput.getText().toString().isEmpty() ? 0 :
                    Float.parseFloat(proteinInput.getText().toString());
            float carbs = carbsInput.getText().toString().isEmpty() ? 0 :
                    Float.parseFloat(carbsInput.getText().toString());
            float fat = fatInput.getText().toString().isEmpty() ? 0 :
                    Float.parseFloat(fatInput.getText().toString());
            String mealType = mealTypeSpinner.getSelectedItem().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("foodName", foodName);
            resultIntent.putExtra("servingSize", servingSize);
            resultIntent.putExtra("calories", calories);
            resultIntent.putExtra("protein", (int)protein);
            resultIntent.putExtra("carbs", (int)carbs);
            resultIntent.putExtra("fat", (int)fat);
            resultIntent.putExtra("mealType", mealType);

            setResult(RESULT_OK, resultIntent);
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }
}
