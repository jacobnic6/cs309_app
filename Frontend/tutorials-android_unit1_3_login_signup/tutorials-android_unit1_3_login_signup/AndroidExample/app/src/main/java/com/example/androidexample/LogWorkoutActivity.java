package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.services.NotificationService;
import com.google.android.material.button.MaterialButton;
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

    // UI Components
    private TextInputEditText workoutNameEditText;
    private AutoCompleteTextView categorySpinner;
    private TextInputEditText exerciseNameEditText;
    private TextInputEditText weightEditText;
    private TextInputEditText setsEditText;
    private TextInputEditText repsEditText;
    private MaterialButton addExerciseButton;
    private MaterialButton saveWorkoutButton;
    private RecyclerView exerciseListRecyclerView;

    // Data
    private List<Exercise> exercisesList;
    private ExerciseAdapter exerciseAdapter;
    private int workoutId = -1;
    private boolean isEditMode = false;

    // Services
    private WorkoutDatabase workoutDatabase;
    private NotificationService notificationService;
    private RequestQueue requestQueue;
    private final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";

    private static final String[] EXERCISE_CATEGORIES = {
            "Chest", "Back", "Legs", "Shoulders", "Arms", "Core", "Cardio"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);

        initializeServices();
        initializeViews();
        setupCategorySpinner();
        setupButtons();
        setupRecyclerView();
        handleIntent();
    }

    private void initializeServices() {
        workoutDatabase = new WorkoutDatabase(this);
        notificationService = new NotificationService(this);
        requestQueue = Volley.newRequestQueue(this);
        exercisesList = new ArrayList<>();
    }

    private void initializeViews() {
        workoutNameEditText = findViewById(R.id.workout_name_edt);
        categorySpinner = findViewById(R.id.category_spinner);
        exerciseNameEditText = findViewById(R.id.exercise_name_edt);
        weightEditText = findViewById(R.id.weight_edt);
        setsEditText = findViewById(R.id.sets_edt);
        repsEditText = findViewById(R.id.reps_edt);
        addExerciseButton = findViewById(R.id.add_exercise_btn);
        saveWorkoutButton = findViewById(R.id.save_workout_btn);
        exerciseListRecyclerView = findViewById(R.id.exercise_list_recycler);
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,
                EXERCISE_CATEGORIES
        );
        categorySpinner.setAdapter(adapter);
    }

    private void setupButtons() {
        addExerciseButton.setOnClickListener(v -> addExercise());
        saveWorkoutButton.setOnClickListener(v -> saveWorkout());
    }

    private void setupRecyclerView() {
        exerciseAdapter = new ExerciseAdapter(exercisesList, exercise -> {
            exercisesList.remove(exercise);
            exerciseAdapter.notifyDataSetChanged();
            checkExerciseListEmpty();
        });
        exerciseListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exerciseListRecyclerView.setAdapter(exerciseAdapter);
    }

    private void handleIntent() {
        workoutId = getIntent().getIntExtra("WORKOUT_ID", -1);
        isEditMode = workoutId != -1;

        if (isEditMode) {
            setTitle("Edit Workout");
            loadWorkout();
        } else {
            setTitle("New Workout");
        }
    }

    private void loadWorkout() {
        String url = BASE_URL + "/workout/" + workoutId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        workoutNameEditText.setText(response.getString("workoutName"));
                        JSONArray exercisesArray = response.getJSONArray("exercises");

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
                        exerciseAdapter.notifyDataSetChanged();
                        checkExerciseListEmpty();
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing workout: " + e.getMessage());
                        loadWorkoutFromLocal();
                    }
                },
                error -> {
                    Log.e(TAG, "Error loading workout: " + error.getMessage());
                    loadWorkoutFromLocal();
                }
        );

        requestQueue.add(request);
    }

    private void loadWorkoutFromLocal() {
        Workout workout = workoutDatabase.getWorkoutById(workoutId);
        if (workout != null) {
            workoutNameEditText.setText(workout.getName());
            exercisesList.addAll(workoutDatabase.getExercisesByWorkoutId(workoutId));
            exerciseAdapter.notifyDataSetChanged();
            checkExerciseListEmpty();
        }
    }

    private void addExercise() {
        if (!validateExerciseInputs()) {
            return;
        }

        Exercise exercise = new Exercise(
                categorySpinner.getText().toString(),
                exerciseNameEditText.getText().toString(),
                Double.parseDouble(weightEditText.getText().toString()),
                Integer.parseInt(setsEditText.getText().toString()),
                Integer.parseInt(repsEditText.getText().toString())
        );

        exercisesList.add(exercise);
        exerciseAdapter.notifyItemInserted(exercisesList.size() - 1);
        clearExerciseInputs();
        checkExerciseListEmpty();
    }

    private boolean validateExerciseInputs() {
        if (categorySpinner.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (exerciseNameEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter exercise name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (weightEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter weight", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (setsEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter number of sets", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (repsEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter number of reps", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearExerciseInputs() {
        categorySpinner.setText("");
        exerciseNameEditText.setText("");
        weightEditText.setText("");
        setsEditText.setText("");
        repsEditText.setText("");
    }

    private void saveWorkout() {
        String workoutName = workoutNameEditText.getText().toString().trim();
        if (workoutName.isEmpty()) {
            Toast.makeText(this, "Please enter workout name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (exercisesList.isEmpty()) {
            Toast.makeText(this, "Please add at least one exercise", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject workoutData = createWorkoutJson(workoutName);
            String url = isEditMode ?
                    BASE_URL + "/workout/" + workoutId :
                    BASE_URL + "/workout/add/" + getCurrentDate() + "/billy123";

            JsonObjectRequest request = new JsonObjectRequest(
                    isEditMode ? Request.Method.PUT : Request.Method.POST,
                    url,
                    workoutData,
                    response -> handleWorkoutSaveSuccess(response, workoutName),
                    error -> handleWorkoutSaveError(error)
            );

            requestQueue.add(request);
        } catch (Exception e) {
            Log.e(TAG, "Error saving workout: " + e.getMessage());
            Toast.makeText(this, "Error saving workout", Toast.LENGTH_SHORT).show();
        }
    }

    private JSONObject createWorkoutJson(String workoutName) throws Exception {
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
        return workoutData;
    }

    private void handleWorkoutSaveSuccess(JSONObject response, String workoutName) {
        int savedWorkoutId = isEditMode ? workoutId : response.optInt("id", -1);

        Workout savedWorkout = new Workout(
                savedWorkoutId,
                workoutName,
                getCurrentDate(),
                exercisesList.size()
        );

        workoutDatabase.saveWorkout(savedWorkout,false);
        workoutDatabase.saveExercises(savedWorkoutId, exercisesList);

        notifyWorkoutAchievements();

        Toast.makeText(this, "Workout saved successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void handleWorkoutSaveError(Exception error) {
        Log.e(TAG, "Error saving workout: " + error.getMessage());
        Toast.makeText(this, "Error saving workout", Toast.LENGTH_SHORT).show();
    }

    private void notifyWorkoutAchievements() {
        double totalVolume = calculateTotalVolume();

        // Compare with previous workout
        if (!isEditMode) {
            Workout lastWorkout = workoutDatabase.getLastWorkout();
            if (lastWorkout != null) {
                List<Exercise> lastExercises = workoutDatabase.getExercisesByWorkoutId(lastWorkout.getId());
                double lastVolume = calculateTotalVolume(lastExercises);

                if (totalVolume > lastVolume) {
                    double improvement = ((totalVolume - lastVolume) / lastVolume) * 100;
                    notificationService.showProgressUpdate(
                            "New Volume Record!",
                            String.format(Locale.getDefault(),
                                    "You've increased your total workout volume by %.1f%%",
                                    improvement)
                    );
                }
            }
        }
    }

    private double calculateTotalVolume() {
        return calculateTotalVolume(exercisesList);
    }

    private double calculateTotalVolume(List<Exercise> exercises) {
        double totalVolume = 0;
        for (Exercise exercise : exercises) {
            totalVolume += exercise.getWeight() * exercise.getSets() * exercise.getReps();
        }
        return totalVolume;
    }

    private void checkExerciseListEmpty() {
        if (exercisesList.isEmpty()) {
            exerciseListRecyclerView.setVisibility(View.GONE);
        } else {
            exerciseListRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}