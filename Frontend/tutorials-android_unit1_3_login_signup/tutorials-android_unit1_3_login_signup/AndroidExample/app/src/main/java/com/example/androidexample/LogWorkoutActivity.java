package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogWorkoutActivity extends AppCompatActivity {
    private static final String TAG = "LogWorkoutActivity";
    private TextInputEditText workoutNameEditText;
    private AutoCompleteTextView categorySpinner;
    private TextInputEditText exerciseNameEditText;
    private TextInputEditText weightEditText;
    private TextInputEditText setsEditText;
    private TextInputEditText repsEditText;
    private Button addExerciseButton;
    private Button saveWorkoutButton;
    private RecyclerView exerciseListRecyclerView;
    private final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080/workout/add/2024-11-10/billy123";
    private List<Exercise> exercisesList;
    private ExerciseAdapter adapter;
    private WorkoutDatabase workoutDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);

        Log.d(TAG, "onCreate started");

        try {
            workoutDatabase = new WorkoutDatabase(this);
            exercisesList = new ArrayList<>();
            initializeViews();
            setupSpinner();
            setupButtons();
            setupRecyclerView();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            Toast.makeText(this, "Error initializing workout form", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        try {
            workoutNameEditText = findViewById(R.id.workout_name_edt);
            categorySpinner = findViewById(R.id.category_spinner);
            exerciseNameEditText = findViewById(R.id.exercise_name_edt);
            weightEditText = findViewById(R.id.weight_edt);
            setsEditText = findViewById(R.id.sets_edt);
            repsEditText = findViewById(R.id.reps_edt);
            addExerciseButton = findViewById(R.id.add_exercise_btn);
            saveWorkoutButton = findViewById(R.id.save_workout_btn);
            exerciseListRecyclerView = findViewById(R.id.exercise_list_recycler);
            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage());
            throw e;
        }
    }

    private void setupRecyclerView() {
        adapter = new ExerciseAdapter(exercisesList);
        exerciseListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exerciseListRecyclerView.setAdapter(adapter);
    }

    private void setupSpinner() {
        String[] categories = {"Biceps", "Triceps", "Chest", "Back", "Legs", "Shoulders", "Core"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                categories
        );
        categorySpinner.setAdapter(adapter);
    }

    private void setupButtons() {
        addExerciseButton.setOnClickListener(v -> addExerciseToList());
        saveWorkoutButton.setOnClickListener(v -> saveWorkout());
    }

    private void addExerciseToList() {
        try {
            String category = categorySpinner.getText().toString();
            String exerciseName = exerciseNameEditText.getText().toString();

            if (category.isEmpty() || exerciseName.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Exercise exercise = new Exercise(
                    category,
                    exerciseName,
                    Double.parseDouble(weightEditText.getText().toString()),
                    Integer.parseInt(setsEditText.getText().toString()),
                    Integer.parseInt(repsEditText.getText().toString())
            );

            exercisesList.add(exercise);
            adapter.notifyDataSetChanged();
            clearExerciseInputs();

            Toast.makeText(this, "Exercise added to workout", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for weight, sets, and reps", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error adding exercise: " + e.getMessage());
            Toast.makeText(this, "Error adding exercise", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveWorkout() {
        try {
            String workoutName = workoutNameEditText.getText().toString();
            if (workoutName.isEmpty()) {
                Toast.makeText(this, "Please enter a workout name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (exercisesList.isEmpty()) {
                Toast.makeText(this, "Please add at least one exercise", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject workoutData = new JSONObject();
            workoutData.put("workoutName", workoutName);
            workoutData.put("dateTracked", getCurrentDate());

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

            Log.d(TAG, "Sending workout data: " + workoutData.toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    BASE_URL,
                    workoutData,
                    response -> {
                        int workoutId = response.optInt("id");
                        Workout savedWorkout = new Workout(
                                workoutId,
                                workoutName,
                                getCurrentDate(),
                                exercisesList.size()
                        );
                        workoutDatabase.saveWorkout(savedWorkout);
                        Toast.makeText(this, "Workout saved successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    error -> {
                        Log.e(TAG, "Error saving workout: " + error.getMessage());
                        Toast.makeText(this, "Error saving workout", Toast.LENGTH_SHORT).show();
                    }
            );

            Volley.newRequestQueue(this).add(request);
        } catch (Exception e) {
            Log.e(TAG, "Error creating workout: " + e.getMessage());
            Toast.makeText(this, "Error creating workout", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private void clearExerciseInputs() {
        exerciseNameEditText.setText("");
        weightEditText.setText("");
        setsEditText.setText("");
        repsEditText.setText("");
        categorySpinner.setText("");
    }
}