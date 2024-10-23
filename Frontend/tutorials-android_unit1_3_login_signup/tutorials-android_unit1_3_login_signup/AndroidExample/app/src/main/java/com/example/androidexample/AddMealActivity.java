package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.R;

public class AddMealActivity extends AppCompatActivity {
    private Spinner mealTypeSpinner;
    private EditText foodNameInput;
    private EditText servingSizeInput;
    private EditText caloriesInput;
    private EditText proteinInput;
    private EditText carbsInput;
    private EditText fatInput;
    private Button saveButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        initializeViews();
        setupSpinner();
        setupButtons();
    }

    private void initializeViews() {
        mealTypeSpinner = findViewById(R.id.meal_type_spinner);
        foodNameInput = findViewById(R.id.food_name);
        servingSizeInput = findViewById(R.id.serving_size);
        caloriesInput = findViewById(R.id.calories);
        proteinInput = findViewById(R.id.protein);
        carbsInput = findViewById(R.id.carbs);
        fatInput = findViewById(R.id.fat);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
    }

    private void setupSpinner() {
        // Create an ArrayAdapter for the meal types
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

    private void validateAndSaveMeal() {
        // Validate required fields
        if (foodNameInput.getText().toString().trim().isEmpty()) {
            foodNameInput.setError("Food name is required");
            return;
        }

        if (caloriesInput.getText().toString().trim().isEmpty()) {
            caloriesInput.setError("Calories are required");
            return;
        }

        try {
            // Get values from inputs
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

            // Create intent to send back the data
            Intent resultIntent = new Intent();
            resultIntent.putExtra("foodName", foodName);
            resultIntent.putExtra("servingSize", servingSize);
            resultIntent.putExtra("calories", calories);
            resultIntent.putExtra("protein", (int)protein);
            resultIntent.putExtra("carbs", (int)carbs);
            resultIntent.putExtra("fat", (int)fat);
            resultIntent.putExtra("mealType", mealType);

            // Set the result and finish
            setResult(RESULT_OK, resultIntent);
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }
}