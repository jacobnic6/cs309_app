package com.example.androidexample.main_five_pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.EditWorkoutActivity;
import com.example.androidexample.LogWorkoutActivity;
import com.example.androidexample.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.example.androidexample.WorkoutDatabase;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WorkoutActivity extends AppCompatActivity {

    private EditText workoutIdEditText, deleteWorkoutIdEditText;
    private Button editWorkoutButton, deleteWorkoutButton, addWorkoutButton;
    private final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";
    private WorkoutDatabase workoutDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        workoutDatabase = new WorkoutDatabase(this);

        initializeViews();
        setupButtons();
        setupBottomNavigation();
    }

    private void initializeViews() {
        workoutIdEditText = findViewById(R.id.edit_workout_id_edt);
        deleteWorkoutIdEditText = findViewById(R.id.delete_workout_id_edt);
        editWorkoutButton = findViewById(R.id.edit_workout_btn);
        deleteWorkoutButton = findViewById(R.id.delete_workout_btn);
        addWorkoutButton = findViewById(R.id.add_workout_btn);
    }
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }
    private void setupButtons() {
        // Add new workout

        // Add new workout
        addWorkoutButton.setOnClickListener(v -> {
            try {
                String emptyBody = "{}";

                StringRequest postRequest = new StringRequest(
                        Request.Method.POST,
                        BASE_URL+"/workout/billy123/2024-11-10",
                        response -> {
                            // Handle the response from the server if needed
                            Log.d("WorkoutActivity", "Response: " + response);
                            Intent intent = new Intent(WorkoutActivity.this, LogWorkoutActivity.class);
                            startActivity(intent);
                        },
                        error -> {
                            // Handle any errors that occur during the request
                            Log.e("WorkoutActivity", "Error: " + error.getMessage());
                            Toast.makeText(WorkoutActivity.this, "Error creating workout", Toast.LENGTH_SHORT).show();
                        }
                ) {
                    @Override
                    public byte[] getBody() {
                        return emptyBody.getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };

                Volley.newRequestQueue(this).add(postRequest);
            } catch (Exception e) {
                Log.e("WorkoutActivity", "Error creating workout: " + e.getMessage());
                Toast.makeText(WorkoutActivity.this, "Error creating workout", Toast.LENGTH_SHORT).show();
            }
        });


        // Edit workout
        editWorkoutButton.setOnClickListener(v -> {
            int workoutId = getValidatedWorkoutId(workoutIdEditText);
            if (workoutId != -1) {
                Intent intent = new Intent(WorkoutActivity.this, EditWorkoutActivity.class);
                intent.putExtra("WORKOUT_ID", workoutId);
                startActivity(intent);
            }
        });

        // Delete workout
        deleteWorkoutButton.setOnClickListener(v -> deleteWorkout());
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.social) {
                startActivity(new Intent(WorkoutActivity.this, SocialActivity.class));
                return true;
            } else if (itemId == R.id.workouts) {
                return true;
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(WorkoutActivity.this, UserProfileActivity.class));
                return true;
            } else if (itemId == R.id.nutrition) {
                startActivity(new Intent(WorkoutActivity.this, NutritionActivity.class));
                return true;
            } else if (itemId == R.id.settings) {
                startActivity(new Intent(WorkoutActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    // Delete a workout
    private void deleteWorkout() {
        int workoutId = getValidatedWorkoutId(deleteWorkoutIdEditText);
        if (workoutId == -1) return;

        workoutDatabase.deleteWorkout(workoutId);

        String url = BASE_URL + "/workout/id/" + workoutId;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                response -> Toast.makeText(WorkoutActivity.this, "Workout deleted successfully!", Toast.LENGTH_SHORT).show(),
                error -> Log.e("WorkoutActivity", "Failed to delete workout: " + error.getMessage())
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    // Helper method to validate workout ID
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