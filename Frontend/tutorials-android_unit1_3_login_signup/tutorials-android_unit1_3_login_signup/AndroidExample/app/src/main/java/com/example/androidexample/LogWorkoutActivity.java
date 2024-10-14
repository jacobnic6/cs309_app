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


    private EditText totalWeightEditText, exerciseNameEditText;
    private Button logWorkoutButton;
    private final String BASE_URL = "https://06e76ef4-a66e-49e1-89ff-719066ed57f5.mock.pstmn.io//workouts";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);


        totalWeightEditText = findViewById(R.id.total_weight_edt);
        exerciseNameEditText = findViewById(R.id.exercise_name_edt);
        logWorkoutButton = findViewById(R.id.log_workout_btn);


        logWorkoutButton.setOnClickListener(v -> logNewWorkout());
    }


    private void logNewWorkout() {
        String url = BASE_URL;


        try {
            // Create the JSON object to send in the request
            JSONObject workoutDetails = new JSONObject();
            workoutDetails.put("userId", 1);  // Replace with the actual user ID
            workoutDetails.put("totalWeight", Double.parseDouble(totalWeightEditText.getText().toString()));
            workoutDetails.put("exerciseName", exerciseNameEditText.getText().toString());


            // Create the POST request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, workoutDetails,
                    response -> {
                        try {
                            // Get the workoutId from the response
                            int workoutId = response.getInt("workoutId");
                            Toast.makeText(LogWorkoutActivity.this, "Workout logged successfully! ID: " + workoutId, Toast.LENGTH_SHORT).show();


                            // After successfully logging, redirect back to WorkoutActivity
                            Intent intent = new Intent(LogWorkoutActivity.this, WorkoutActivity.class);
                            startActivity(intent);
                            finish();  // End this activity so it’s removed from the back stack


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LogWorkoutActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Log.e("LogWorkoutActivity", "Failed to log workout: " + error.getMessage());
                        Toast.makeText(LogWorkoutActivity.this, "Failed to log workout", Toast.LENGTH_SHORT).show();
                    }
            );


            // Add the request to the Volley request queue
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create request", Toast.LENGTH_SHORT).show();
        }
    }
}
