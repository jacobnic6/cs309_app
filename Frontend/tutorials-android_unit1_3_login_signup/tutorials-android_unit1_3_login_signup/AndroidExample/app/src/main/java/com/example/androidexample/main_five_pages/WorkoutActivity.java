package com.example.androidexample.main_five_pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.EditWorkoutActivity;
import com.example.androidexample.LogWorkoutActivity;
import com.example.androidexample.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.view.MenuItem;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WorkoutActivity extends AppCompatActivity {

    private EditText editWorkoutIdEditText, deleteWorkoutIdEditText;
    private Button viewWorkoutsButton, editWorkoutButton, deleteWorkoutButton, navigateLogWorkoutButton;

    private final String BASE_URL = "https://2765ffc4-ba07-4af7-8713-26293f0065d9.mock.pstmn.io/workouts";  // Base URL for all requests

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        // Initialize UI elements
        editWorkoutIdEditText = findViewById(R.id.edit_workout_id_edt);
        deleteWorkoutIdEditText = findViewById(R.id.delete_workout_id_edt);
        viewWorkoutsButton = findViewById(R.id.view_workouts_btn);
        editWorkoutButton = findViewById(R.id.edit_workout_btn);
        deleteWorkoutButton = findViewById(R.id.delete_workout_btn);
        navigateLogWorkoutButton = findViewById(R.id.navigate_log_workout_btn);

        // The following is for switching to the other four "main pages of the app" - social, exercise, nutrition, and settings
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.social) {
                    startActivity(new Intent(WorkoutActivity.this, SocialActivity.class));
                    return true;
                } else if (itemId == R.id.workouts) {
                    startActivity(new Intent(WorkoutActivity.this, WorkoutActivity.class));
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
            }
        });

        // Set up click listeners
        navigateLogWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LogWorkoutActivity to log a new workout
                Intent intent = new Intent(WorkoutActivity.this, LogWorkoutActivity.class);
                startActivity(intent);
            }
        });

        viewWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewWorkouts();
            }
        });

        editWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EditWorkoutActivity and pass the workout ID
                int workoutId = getValidatedWorkoutId(editWorkoutIdEditText);
                if (workoutId != -1) {
                    Intent intent = new Intent(WorkoutActivity.this, EditWorkoutActivity.class);
                    intent.putExtra("WORKOUT_ID", workoutId);
                    startActivity(intent);
                }
            }
        });

        deleteWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWorkout();
            }
        });
    }

    // View all workouts
    private void viewWorkouts() {
        String url = BASE_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Convert response to JSON array
                            JSONArray workoutsArray = new JSONArray(response);
                            StringBuilder workoutsDisplay = new StringBuilder();

                            // Parse each workout in the array
                            for (int i = 0; i < workoutsArray.length(); i++) {
                                JSONObject workout = workoutsArray.getJSONObject(i);
                                workoutsDisplay.append("Workout ID: ").append(workout.getInt("workoutId")).append("\n");
                                workoutsDisplay.append("User ID: ").append(workout.getInt("userId")).append("\n");
                                workoutsDisplay.append("Start Time: ").append(workout.getString("startTime")).append("\n");
                                workoutsDisplay.append("Workout Length: ").append(workout.getInt("workoutLength")).append(" minutes\n");
                                workoutsDisplay.append("Total Weight: ").append(workout.getDouble("totalWeight")).append(" lbs\n");
                                workoutsDisplay.append("Exercises Performed: ").append(workout.getJSONObject("exercisesPerformed").toString()).append("\n\n");
                            }

                            // Display the workouts
                            Toast.makeText(WorkoutActivity.this, workoutsDisplay.toString(), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            Log.e("WorkoutActivity", "JSON Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("WorkoutActivity", "Failed to retrieve workouts: " + error.getMessage());
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    // Delete a workout based on its ID (using integer ID)
    private void deleteWorkout() {
        int workoutId = getValidatedWorkoutId(deleteWorkoutIdEditText);
        if (workoutId == -1) return;  // Return if the ID is invalid

        String url = BASE_URL + "/" + workoutId;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(WorkoutActivity.this, "Workout deleted successfully!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("WorkoutActivity", "Failed to delete workout: " + error.getMessage());
                    }
                });

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
