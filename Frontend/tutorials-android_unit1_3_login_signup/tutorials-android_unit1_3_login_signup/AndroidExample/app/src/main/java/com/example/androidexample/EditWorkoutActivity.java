package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class EditWorkoutActivity extends AppCompatActivity {
    private static final String TAG = "EditWorkoutActivity";
    private TextInputEditText workoutNameEditText;
    private RecyclerView exerciseListRecyclerView;
    private ExerciseAdapter adapter;
    private List<Exercise> exercisesList;
    private final String BASE_URL = "https://06e76ef4-a66e-49e1-89ff-719066ed57f5.mock.pstmn.io//workouts";
    private int currentWorkoutId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);

        exercisesList = new ArrayList<>();
        initializeViews();
        setupRecyclerView();

        // Get workout ID from intent
        currentWorkoutId = getIntent().getIntExtra("WORKOUT_ID", -1);
        if (currentWorkoutId != -1) {
            fetchWorkoutDetails(currentWorkoutId);
        } else {
            Toast.makeText(this, "No workout ID provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        workoutNameEditText = findViewById(R.id.workout_name_edt);
        exerciseListRecyclerView = findViewById(R.id.exercise_list_recycler);
    }

    private void setupRecyclerView() {
        adapter = new ExerciseAdapter(exercisesList);
        exerciseListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exerciseListRecyclerView.setAdapter(adapter);
    }

    private void fetchWorkoutDetails(int workoutId) {
        String url = BASE_URL + "/" + workoutId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        // Set workout name
                        workoutNameEditText.setText(response.getString("workoutName"));

                        // Parse exercises
                        JSONArray exercisesArray = response.getJSONArray("exercises");
                        exercisesList.clear();

                        for (int i = 0; i < exercisesArray.length(); i++) {
                            JSONObject exerciseJson = exercisesArray.getJSONObject(i);
                            Exercise exercise = new Exercise(
                                    exerciseJson.getString("category"),
                                    exerciseJson.getString("exerciseName"),
                                    exerciseJson.getDouble("weight"),
                                    exerciseJson.getInt("sets"),
                                    exerciseJson.getInt("reps")
                            );
                            exercisesList.add(exercise);
                        }

                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Workout loaded successfully", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing workout: " + e.getMessage());
                        Toast.makeText(this, "Error loading workout details", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching workout: " + error.getMessage());
                    Toast.makeText(this, "Error fetching workout", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void saveWorkoutChanges() {
        try {
            String workoutName = workoutNameEditText.getText().toString();
            if (workoutName.isEmpty()) {
                Toast.makeText(this, "Please enter a workout name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (exercisesList.isEmpty()) {
                Toast.makeText(this, "Cannot save workout without exercises", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject workoutData = new JSONObject();
            workoutData.put("workoutName", workoutName);

            JSONArray exercisesArray = new JSONArray();
            for (Exercise exercise : exercisesList) {
                JSONObject exerciseJson = new JSONObject();
                exerciseJson.put("category", exercise.getCategory());
                exerciseJson.put("exerciseName", exercise.getName());
                exerciseJson.put("weight", exercise.getWeight());
                exerciseJson.put("sets", exercise.getSets());
                exerciseJson.put("reps", exercise.getReps());
                exercisesArray.put(exerciseJson);
            }
            workoutData.put("exercises", exercisesArray);

            String url = BASE_URL + "/" + currentWorkoutId;

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    workoutData,
                    response -> {
                        Toast.makeText(this, "Workout updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    error -> {
                        Log.e(TAG, "Error updating workout: " + error.getMessage());
                        Toast.makeText(this, "Error updating workout", Toast.LENGTH_SHORT).show();
                    }
            );

            Volley.newRequestQueue(this).add(request);
        } catch (Exception e) {
            Log.e(TAG, "Error saving workout: " + e.getMessage());
            Toast.makeText(this, "Error saving changes", Toast.LENGTH_SHORT).show();
        }
    }
}