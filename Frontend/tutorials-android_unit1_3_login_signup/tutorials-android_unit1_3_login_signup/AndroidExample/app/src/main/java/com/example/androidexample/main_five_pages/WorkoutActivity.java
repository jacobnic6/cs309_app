package com.example.androidexample.main_five_pages;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.EditWorkoutActivity;
import com.example.androidexample.LogWorkoutActivity;
import com.example.androidexample.R;

import org.json.JSONException;
import org.json.JSONObject;


public class WorkoutActivity extends AppCompatActivity {


    private EditText workoutIdEditText, deleteWorkoutIdEditText;
    private Button editWorkoutButton, deleteWorkoutButton, navigateLogWorkoutButton;


    private final String BASE_URL = "https://06e76ef4-a66e-49e1-89ff-719066ed57f5.mock.pstmn.io//workouts";  // Base URL for all requests


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);


        // Initialize UI elements
        workoutIdEditText = findViewById(R.id.edit_workout_id_edt);
        deleteWorkoutIdEditText = findViewById(R.id.delete_workout_id_edt);
        editWorkoutButton = findViewById(R.id.edit_workout_btn);
        deleteWorkoutButton = findViewById(R.id.delete_workout_btn);
        navigateLogWorkoutButton = findViewById(R.id.navigate_log_workout_btn);


        // Set up click listeners
        navigateLogWorkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutActivity.this, LogWorkoutActivity.class);
            startActivity(intent);
        });


        editWorkoutButton.setOnClickListener(v -> {
            int workoutId = getValidatedWorkoutId(workoutIdEditText);
            if (workoutId != -1) {
                Intent intent = new Intent(WorkoutActivity.this, EditWorkoutActivity.class);
                intent.putExtra("WORKOUT_ID", workoutId);
                startActivity(intent);
            }
        });


        deleteWorkoutButton.setOnClickListener(v -> deleteWorkout());
    }


    // Delete a workout based on its ID
    private void deleteWorkout() {
        int workoutId = getValidatedWorkoutId(deleteWorkoutIdEditText);
        if (workoutId == -1) return;


        String url = BASE_URL + "/" + workoutId;


        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                response -> Toast.makeText(WorkoutActivity.this, "Workout deleted successfully!", Toast.LENGTH_SHORT).show(),
                error -> Log.e("WorkoutActivity", "Failed to delete workout: " + error.getMessage())
        );


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }


    // Helper method to validate the ID input and convert to integer
    private int getValidatedWorkoutId(EditText editText) {
        String input = editText.getText().toString().trim();
        if (input.isEmpty()) {
            Toast.makeText(this, "Workout ID cannot be empty", Toast.LENGTH_SHORT).show();
            return -1;
        }


        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Workout ID. Please enter a numeric value.", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }
}
