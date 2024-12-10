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
    private WorkoutDatabase workoutDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);

        workoutDatabase = new WorkoutDatabase(this);
        exercisesList = new ArrayList<>();
        initializeViews();
        setupRecyclerView();

        // Get workout ID from intent
        currentWorkoutId = getIntent().getIntExtra("WORKOUT_ID", -1);
        if (currentWorkoutId != -1) {
            fetchWorkoutDetailsFromLocalDatabase(currentWorkoutId);
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
        ExerciseAdapter.ExerciseClickListener listener = new ExerciseAdapter.ExerciseClickListener() {
            @Override
            public void onExerciseRemoved(Exercise exercise) {
                // Remove the exercise from the list
                exercisesList.remove(exercise);
                adapter.notifyDataSetChanged();
                Toast.makeText(EditWorkoutActivity.this, "Exercise removed: " + exercise.getName(), Toast.LENGTH_SHORT).show();
            }
        };

        adapter = new ExerciseAdapter(exercisesList, listener);
        exerciseListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exerciseListRecyclerView.setAdapter(adapter);
    }

    private void fetchWorkoutDetailsFromLocalDatabase(int workoutId) {
        Workout workout = workoutDatabase.getWorkoutById(workoutId);
        if (workout != null) {
            workoutNameEditText.setText(workout.getName());
            // Fetch exercises from local database based on workout ID
            exercisesList.clear();
            exercisesList.addAll(workoutDatabase.getExercisesByWorkoutId(workoutId));
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Workout loaded from local database", Toast.LENGTH_SHORT).show();
        } else {
            fetchWorkoutDetailsFromBackend(workoutId);
        }
    }

    private void fetchWorkoutDetailsFromBackend(int workoutId) {
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
                        Toast.makeText(this, "Workout loaded from backend", Toast.LENGTH_SHORT).show();

                        // Save workout details to local database
                        saveWorkoutToLocalDatabase(workoutId, response.getString("workoutName"));
                        saveExercisesToLocalDatabase(workoutId, exercisesList);

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

    private void saveWorkoutToLocalDatabase(int workoutId, String workoutName) {
        Workout workout = new Workout(workoutId, workoutName, "", exercisesList.size());
        workoutDatabase.saveWorkout(workout, false); // Pass 'false' if it's not synced yet
    }


    private void saveExercisesToLocalDatabase(int workoutId, List<Exercise> exercises) {
        workoutDatabase.saveExercises(workoutId, exercises);
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
                        // Update workout details in local database
                        saveWorkoutToLocalDatabase(currentWorkoutId, workoutName);
                        saveExercisesToLocalDatabase(currentWorkoutId, exercisesList);
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