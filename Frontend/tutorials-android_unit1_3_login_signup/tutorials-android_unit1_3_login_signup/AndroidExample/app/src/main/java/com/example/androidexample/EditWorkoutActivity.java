package com.example.androidexample;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;


public class EditWorkoutActivity extends AppCompatActivity {


    private EditText workoutIdEditText, totalWeightEditText, exerciseNameEditText;
    private Button fetchWorkoutButton, saveWorkoutButton;
    private final String BASE_URL = "https://06e76ef4-a66e-49e1-89ff-719066ed57f5.mock.pstmn.io//workouts";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);


        // Initialize UI elements
        workoutIdEditText = findViewById(R.id.edit_workout_id_edt);
        totalWeightEditText = findViewById(R.id.edit_total_weight_edt);
        exerciseNameEditText = findViewById(R.id.edit_exercise_name_edt);
        fetchWorkoutButton = findViewById(R.id.fetch_workout_btn);
        saveWorkoutButton = findViewById(R.id.save_workout_btn);


        // Fetch button click listener
        fetchWorkoutButton.setOnClickListener(v -> {
            int workoutId = getValidatedWorkoutId();
            if (workoutId != -1) {
                fetchWorkoutDetails(workoutId);
            }
        });


        // Save button click listener
        saveWorkoutButton.setOnClickListener(v -> updateWorkoutDetails());
    }


    // Fetch the workout details from the server based on the ID
    private void fetchWorkoutDetails(int workoutId) {
        String url = BASE_URL + "/" + workoutId;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Populate the form fields with the fetched workout data
                        totalWeightEditText.setText(String.valueOf(response.getDouble("totalWeight")));
                        exerciseNameEditText.setText(response.getString("exerciseName"));


                        Toast.makeText(EditWorkoutActivity.this, "Workout details fetched and ready to edit!", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(EditWorkoutActivity.this, "Failed to parse workout details.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Log.e("EditWorkoutActivity", "Failed to fetch workout: " + error.getMessage())
        );


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }


    // Update the workout details on the server based on the input fields
    private void updateWorkoutDetails() {
        int workoutId = getValidatedWorkoutId();
        if (workoutId == -1) return;


        String url = BASE_URL + "/" + workoutId;


        try {
            JSONObject workoutDetails = new JSONObject();
            workoutDetails.put("totalWeight", Double.parseDouble(totalWeightEditText.getText().toString()));
            workoutDetails.put("exerciseName", exerciseNameEditText.getText().toString());


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, workoutDetails,
                    response -> Toast.makeText(EditWorkoutActivity.this, "Workout updated successfully!", Toast.LENGTH_SHORT).show(),
                    error -> Log.e("EditWorkoutActivity", "Failed to update workout: " + error.getMessage())
            );


            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create JSON request.", Toast.LENGTH_SHORT).show();
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
