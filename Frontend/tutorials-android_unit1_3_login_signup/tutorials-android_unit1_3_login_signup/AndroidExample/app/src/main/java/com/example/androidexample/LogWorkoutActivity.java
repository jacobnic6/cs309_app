package com.example.androidexample;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class LogWorkoutActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private EditText exerciseNameEditText;
    private EditText weightEditText;
    private EditText setsEditText;
    private EditText repsEditText;
    private Button logWorkoutButton;
    private final String BASE_URL = "https://06e76ef4-a66e-49e1-89ff-719066ed57f5.mock.pstmn.io//workouts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);

        initializeViews();
        setupSpinner();
        setupLogButton();
    }

    private void initializeViews() {
        categorySpinner = findViewById(R.id.category_spinner);
        exerciseNameEditText = findViewById(R.id.exercise_name_edt);
        weightEditText = findViewById(R.id.weight_edt);
        setsEditText = findViewById(R.id.sets_edt);
        repsEditText = findViewById(R.id.reps_edt);
        logWorkoutButton = findViewById(R.id.log_workout_btn);
    }

    private void setupSpinner() {
        String[] categories = {"Biceps", "Triceps", "Chest", "Back", "Legs", "Shoulders", "Core"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void setupLogButton() {
        logWorkoutButton.setOnClickListener(v -> logWorkout());
    }

    private void logWorkout() {
        try {
            // Get values from fields
            String exerciseName = exerciseNameEditText.getText().toString();
            double weight = Double.parseDouble(weightEditText.getText().toString());
            int sets = Integer.parseInt(setsEditText.getText().toString());
            int reps = Integer.parseInt(repsEditText.getText().toString());

            // Create JSON object
            JSONObject workoutData = new JSONObject();
            workoutData.put("category", categorySpinner.getSelectedItem().toString());
            workoutData.put("exerciseName", exerciseName);
            workoutData.put("weight", weight);
            workoutData.put("sets", sets);
            workoutData.put("reps", reps);

            // Make API request
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    BASE_URL,
                    workoutData,
                    response -> {
                        Toast.makeText(this, "Workout saved successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    error -> Toast.makeText(this, "Error saving workout", Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(this).add(request);

        } catch (Exception e) {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
        }
    }
}