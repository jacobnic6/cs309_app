package com.example.androidexample;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WorkoutActivity extends AppCompatActivity {

    private EditText startTimeEditText, workoutLengthEditText, totalWeightEditText, exercisesPerformedEditText;
    private EditText editWorkoutIdEditText, deleteWorkoutIdEditText;
    private Button logWorkoutButton, viewWorkoutsButton, editWorkoutButton, deleteWorkoutButton;

    private final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080/";  // Base URL for all requests

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        // Initialize UI elements
        startTimeEditText = findViewById(R.id.workout_start_time_edt);
        workoutLengthEditText = findViewById(R.id.workout_length_edt);
        totalWeightEditText = findViewById(R.id.total_weight_edt);
        exercisesPerformedEditText = findViewById(R.id.exercises_performed_edt);
        editWorkoutIdEditText = findViewById(R.id.edit_workout_id_edt);
        deleteWorkoutIdEditText = findViewById(R.id.delete_workout_id_edt);
        logWorkoutButton = findViewById(R.id.log_workout_btn);
        viewWorkoutsButton = findViewById(R.id.view_workouts_btn);
        editWorkoutButton = findViewById(R.id.edit_workout_btn);
        deleteWorkoutButton = findViewById(R.id.delete_workout_btn);

        // Set up click listeners
        logWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logNewWorkout();
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
                editWorkout();
            }
        });

        deleteWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWorkout();
            }
        });
    }

    private void logNewWorkout() {
        String url = BASE_URL;

        try {
            JSONObject workoutDetails = new JSONObject();
            workoutDetails.put("userId", 1);  // Replace with the actual user ID
            workoutDetails.put("startTime", startTimeEditText.getText().toString());
            workoutDetails.put("workoutLength", Integer.parseInt(workoutLengthEditText.getText().toString()));
            workoutDetails.put("totalWeight", Double.parseDouble(totalWeightEditText.getText().toString()));
            workoutDetails.put("exercisesPerformed", new JSONObject(exercisesPerformedEditText.getText().toString()));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, workoutDetails,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(WorkoutActivity.this, "Workout logged successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("WorkoutActivity", "Failed to log workout: " + error.getMessage());
                    Toast.makeText(WorkoutActivity.this, "Failed to log workout: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    private void editWorkout() {
        String workoutId = editWorkoutIdEditText.getText().toString().trim();
        String url = BASE_URL + "/" + workoutId;

        try {
            JSONObject updatedWorkoutDetails = new JSONObject();
            updatedWorkoutDetails.put("startTime", startTimeEditText.getText().toString());
            updatedWorkoutDetails.put("workoutLength", Integer.parseInt(workoutLengthEditText.getText().toString()));
            updatedWorkoutDetails.put("totalWeight", Double.parseDouble(totalWeightEditText.getText().toString()));
            updatedWorkoutDetails.put("exercisesPerformed", new JSONObject(exercisesPerformedEditText.getText().toString()));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, updatedWorkoutDetails,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(WorkoutActivity.this, "Workout updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("WorkoutActivity", "Failed to update workout: " + error.getMessage());
                }
            });

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void deleteWorkout() {
        String workoutId = deleteWorkoutIdEditText.getText().toString().trim();
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
}
