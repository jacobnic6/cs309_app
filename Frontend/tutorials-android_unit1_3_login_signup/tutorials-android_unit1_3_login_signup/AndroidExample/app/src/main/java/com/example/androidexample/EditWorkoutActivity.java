package com.example.androidexample;

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
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class EditWorkoutActivity extends AppCompatActivity {

    private EditText workoutIdEditText, monthEditText, dayEditText, yearEditText, totalWeightEditText, exercisesPerformedEditText, notesEditText, workoutLengthEditText;
    private Button fetchWorkoutButton, saveWorkoutButton;
    private final String BASE_URL = "https://2765ffc4-ba07-4af7-8713-26293f0065d9.mock.pstmn.io/workouts";  // Replace with your actual server URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);  // Referencing the edit workout layout

        // Initialize UI elements based on XML IDs
        workoutIdEditText = findViewById(R.id.edit_workout_id_edt);
        monthEditText = findViewById(R.id.edit_month_edt);
        dayEditText = findViewById(R.id.edit_day_edt);
        yearEditText = findViewById(R.id.edit_year_edt);
        totalWeightEditText = findViewById(R.id.edit_total_weight_edt);
        exercisesPerformedEditText = findViewById(R.id.edit_exercises_performed_edt);
        notesEditText = findViewById(R.id.edit_notes_edt);
        workoutLengthEditText = findViewById(R.id.edit_workout_length_edt);

        fetchWorkoutButton = findViewById(R.id.fetch_workout_btn);
        saveWorkoutButton = findViewById(R.id.save_workout_btn);

        // Retrieve the workout ID from the intent
        Intent intent = getIntent();
        int workoutId = intent.getIntExtra("WORKOUT_ID", -1);

        if (workoutId != -1) {
            workoutIdEditText.setText(String.valueOf(workoutId));  // Set the workout ID in the edit text
            fetchWorkoutDetails(workoutId);  // Fetch details for the given workout ID
        }

        // Fetch button click listener
        fetchWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int workoutId = getValidatedWorkoutId();
                if (workoutId != -1) {
                    fetchWorkoutDetails(workoutId);
                }
            }
        });

        // Save button click listener
        saveWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWorkoutDetails();
            }
        });
    }

    // Fetch the workout details from the server based on the ID
    private void fetchWorkoutDetails(int workoutId) {
        String url = BASE_URL + "/" + workoutId;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Populate fields with the fetched workout details
                            String startTime = response.getString("startTime");
                            String[] dateParts = startTime.split("-");
                            yearEditText.setText(dateParts[0]);
                            monthEditText.setText(dateParts[1]);
                            dayEditText.setText(dateParts[2]);

                            workoutLengthEditText.setText(response.getString("workoutLength"));
                            totalWeightEditText.setText(response.getString("totalWeight"));
                            exercisesPerformedEditText.setText(response.getJSONObject("exercisesPerformed").toString());
                            notesEditText.setText(response.optString("notes", ""));

                            Toast.makeText(EditWorkoutActivity.this, "Workout details fetched!", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditWorkoutActivity.this, "Failed to parse workout details.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("EditWorkoutActivity", "Failed to fetch workout: " + error.getMessage());
                Toast.makeText(EditWorkoutActivity.this, "Failed to fetch workout: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    // Update the workout details on the server based on the input fields
    private void updateWorkoutDetails() {
        int workoutId = getValidatedWorkoutId();
        if (workoutId == -1) return;

        String url = BASE_URL + "/" + workoutId;

        try {
            JSONObject workoutDetails = new JSONObject();
            workoutDetails.put("userId", 1);  // Replace with the actual user ID
            String startTime = yearEditText.getText().toString() + "-" +
                    monthEditText.getText().toString() + "-" +
                    dayEditText.getText().toString();
            workoutDetails.put("startTime", startTime);
            workoutDetails.put("workoutLength", Integer.parseInt(workoutLengthEditText.getText().toString()));
            workoutDetails.put("totalWeight", Double.parseDouble(totalWeightEditText.getText().toString()));

            // Create a JSON object for the exercises performed and add it to the workout details
            String exercisesJSON = exercisesPerformedEditText.getText().toString();
            if (!exercisesJSON.isEmpty()) {
                workoutDetails.put("exercisesPerformed", new JSONObject(exercisesJSON));
            }

            // Add the notes field if it’s not empty
            String notes = notesEditText.getText().toString().trim();
            if (!notes.isEmpty()) {
                workoutDetails.put("notes", notes);
            }

            // Create a PUT request to update the workout details
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, workoutDetails,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(EditWorkoutActivity.this, "Workout updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("EditWorkoutActivity", "Failed to update workout: " + error.getMessage());
                    Toast.makeText(EditWorkoutActivity.this, "Failed to update workout: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Helper method to validate the Workout ID input
    private int getValidatedWorkoutId() {
        String input = workoutIdEditText.getText().toString().trim();
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
