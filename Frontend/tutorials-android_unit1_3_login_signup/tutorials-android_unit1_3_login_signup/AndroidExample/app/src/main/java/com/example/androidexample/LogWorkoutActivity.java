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
import com.example.androidexample.main_five_pages.WorkoutActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LogWorkoutActivity extends AppCompatActivity {

    private EditText monthEditText, dayEditText, yearEditText, totalWeightEditText, exercisesPerformedEditText, notesEditText;
    private EditText workoutLengthEditText;
    private Button logWorkoutButton;
    private final String BASE_URL = "https://2765ffc4-ba07-4af7-8713-26293f0065d9.mock.pstmn.io/workouts";  // Mock server URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);  // Referencing the new XML layout

        // Initialize UI elements based on XML IDs
        monthEditText = findViewById(R.id.log_month_edt);
        dayEditText = findViewById(R.id.log_day_edt);
        yearEditText = findViewById(R.id.log_year_edt);
        totalWeightEditText = findViewById(R.id.total_weight_edt);
        exercisesPerformedEditText = findViewById(R.id.exercises_performed_edt);
        notesEditText = findViewById(R.id.notes_edt);  // Reference to new Notes field in XML
        logWorkoutButton = findViewById(R.id.log_workout_btn);

        // Set up the log workout button click listener
        logWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logNewWorkout();
            }
        });
    }

    private void logNewWorkout() {
        String url = BASE_URL;

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

            // Create a POST request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, workoutDetails,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(LogWorkoutActivity.this, "Workout logged successfully!", Toast.LENGTH_SHORT).show();
                            // Redirect back to WorkoutActivity
                            Intent intent = new Intent(LogWorkoutActivity.this, WorkoutActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LogWorkoutActivity", "Failed to log workout: " + error.getMessage());
                    Toast.makeText(LogWorkoutActivity.this, "Failed to log workout: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            // Add the request to the Volley queue
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
