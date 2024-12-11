package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.services.NotificationService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogWorkoutActivity extends AppCompatActivity implements ExerciseAdapter.ExerciseClickListener {
    private static final String TAG = "LogWorkoutActivity";
    private static final int EXERCISE_SEARCH_REQUEST = 1;

    // UI Components
    private AutoCompleteTextView categorySpinner;
    private TextInputEditText exerciseNameEditText;
    private TextInputEditText weightEditText;
    private TextInputEditText repsEditText;
    private TextInputEditText setsEditText;
    private MaterialButton addExerciseButton;
    private MaterialButton saveWorkoutButton;
    private MaterialButton searchExerciseButton;
    private RecyclerView exerciseListRecyclerView;
    private View emptyStateView;

    // Data
    private List<Exercise> exercisesList;
    private ExerciseAdapter exerciseAdapter;
    private int workoutId = -1;
    private int currentSetNumber = 1;

    // Services
    private WorkoutDatabase workoutDatabase;
    private NotificationService notificationService;
    private RequestQueue requestQueue;
    private final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";
    private final String userId = "billy123"; // Should come from SessionManager

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
        categorySpinner = findViewById(R.id.category_spinner);
        exerciseNameEditText = findViewById(R.id.exercise_name_edt);
        weightEditText = findViewById(R.id.weight_edt);
        repsEditText = findViewById(R.id.reps_edt);
        setsEditText = findViewById(R.id.sets_edt);
        addExerciseButton = findViewById(R.id.add_exercise_btn);
        saveWorkoutButton = findViewById(R.id.save_workout_btn);
        searchExerciseButton = findViewById(R.id.search_exercise_btn);
        exerciseListRecyclerView = findViewById(R.id.exercise_list_recycler);
        emptyStateView = findViewById(R.id.empty_state_view);
    }

    private void setupCategorySpinner() {
        String[] categories = {"Strength", "Cardio", "Flexibility", "Balance"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, categories);
        categorySpinner.setAdapter(adapter);
    }

    private void setupButtons() {
        addExerciseButton.setOnClickListener(v -> addExercise());
        saveWorkoutButton.setOnClickListener(v -> saveWorkout());
        searchExerciseButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExerciseSearchActivity.class);
            startActivityForResult(intent, EXERCISE_SEARCH_REQUEST);
        });
    }

    private void setupRecyclerView() {
        exerciseAdapter = new ExerciseAdapter(exercisesList, this);
        exerciseListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exerciseListRecyclerView.setAdapter(exerciseAdapter);
        updateEmptyState();
    }

    private void handleIntent() {
        workoutId = getIntent().getIntExtra("WORKOUT_ID", -1);
        if (workoutId != -1) {
            fetchWorkoutDetails();
        }
    }

    private void fetchWorkoutDetails() {
        String url = BASE_URL + "/workout/id/" + workoutId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray activities = response.getJSONArray("activities");
                        exercisesList.clear();

                        for (int i = 0; i < activities.length(); i++) {
                            JSONObject exerciseJson = activities.getJSONObject(i);
                            Exercise exercise = new Exercise(
                                    exerciseJson.optString("category", "strength"),
                                    exerciseJson.getString("name"),
                                    exerciseJson.getDouble("weight"),
                                    exerciseJson.getInt("reps"),
                                    exerciseJson.getInt("sets"),
                                    60
                            );
                            exercisesList.add(exercise);
                        }

                        exerciseAdapter.notifyDataSetChanged();
                        updateEmptyState();
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing workout: " + e.getMessage());
                        loadWorkoutFromLocal();
                    }
                },
                error -> {
                    handleVolleyError(error);
                    loadWorkoutFromLocal();
                }
        );

        requestQueue.add(request);
    }

    private void loadWorkoutFromLocal() {
        List<Exercise> localExercises = workoutDatabase.getExercisesByWorkoutId(workoutId);
        exercisesList.clear();
        exercisesList.addAll(localExercises);
        exerciseAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void saveWorkout() {
        if (exercisesList.isEmpty()) {
            Toast.makeText(this, "Please add at least one exercise", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Create the exercise object in the same format as addExercise
            JSONObject exerciseData = new JSONObject();
            exerciseData.put("category", exercisesList.get(0).getCategory().toLowerCase());
            exerciseData.put("exerciseName", exercisesList.get(0).getName().toLowerCase());
            exerciseData.put("weight", exercisesList.get(0).getWeight());
            exerciseData.put("reps", exercisesList.get(0).getReps());
            exerciseData.put("sets", exercisesList.get(0).getSets());

            String url = BASE_URL + "/workout/" + userId + "/" + getCurrentDate();

            Log.d(TAG, "Save URL: " + url);
            Log.d(TAG, "Save Body: " + exerciseData.toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    exerciseData,
                    response -> {
                        Log.d(TAG, "Save Success Response: " + response.toString());
                        handleSaveSuccess();
                    },
                    error -> {
                        if (error.networkResponse != null) {
                            String errorData = new String(error.networkResponse.data);
                            Log.e(TAG, "Error Response: " + errorData);
                            Log.e(TAG, "Status Code: " + error.networkResponse.statusCode);
                            Toast.makeText(this, "Error: " + errorData, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error saving workout: " + error.getMessage());
                            Toast.makeText(this, "Error saving workout", Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000,  // 10 seconds timeout
                    0,      // no retries
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating workout: " + e.getMessage());
            Toast.makeText(this, "Error creating workout", Toast.LENGTH_SHORT).show();
        }
    }


    private void handleSaveSuccess() {
        // Save to local database
        workoutDatabase.saveExercises(workoutId, exercisesList);

        // Show notification
        notificationService.showWorkoutComplete("Workout", exercisesList.size());

        // Show success message and close activity
        Toast.makeText(this, "Workout saved successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void handleSaveError(Exception error) {
        Log.e(TAG, "Error saving workout: " + error.getMessage());
        Toast.makeText(this, "Error saving workout: " + error.getMessage(), Toast.LENGTH_LONG).show();
    }

    private double calculateTotalWeight() {
        double total = 0;
        for (Exercise exercise : exercisesList) {
            total += exercise.getWeight() * exercise.getReps() * exercise.getSets();
        }
        return total;
    }
    private void addExercise() {
        if (!validateInputs()) return;

        try {
            // Create request body matching API format
            JSONObject requestBody = new JSONObject();
            requestBody.put("category", categorySpinner.getText().toString().toLowerCase());
            requestBody.put("exerciseName", exerciseNameEditText.getText().toString().toLowerCase());
            requestBody.put("weight", Double.parseDouble(weightEditText.getText().toString()));
            requestBody.put("reps", Integer.parseInt(repsEditText.getText().toString()));
            requestBody.put("sets", Integer.parseInt(setsEditText.getText().toString()));

            String url = BASE_URL + "/workout/add/" + getCurrentDate() + "/" + userId;

            // Log request details
            Log.d(TAG, "Request URL: " + url);
            Log.d(TAG, "Request Body: " + requestBody.toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    requestBody,
                    response -> {
                        Log.d(TAG, "Success Response: " + response.toString());

                        Exercise exercise = new Exercise(
                                categorySpinner.getText().toString().toLowerCase(),
                                exerciseNameEditText.getText().toString().toLowerCase(),
                                Double.parseDouble(weightEditText.getText().toString()),
                                Integer.parseInt(repsEditText.getText().toString()),
                                Integer.parseInt(setsEditText.getText().toString()),
                                60
                        );

                        exercisesList.add(exercise);
                        exerciseAdapter.notifyItemInserted(exercisesList.size() - 1);
                        clearInputs();
                        updateEmptyState();
                        Toast.makeText(this, "Exercise added successfully", Toast.LENGTH_SHORT).show();
                    },
                    this::handleVolleyError
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

            // Set shorter timeout with no retries
            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000, // 10 seconds
                    0,     // no retries
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            requestQueue.add(request);
        } catch (Exception e) {
            Log.e(TAG, "Error creating exercise: " + e.getMessage());
            Toast.makeText(this, "Error creating exercise", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleVolleyError(VolleyError error) {
        String errorMessage = "Error occurred";

        if (error.networkResponse != null) {
            String responseData = new String(error.networkResponse.data);
            Log.e(TAG, "Error Response: " + responseData);
            Log.e(TAG, "Status Code: " + error.networkResponse.statusCode);
            errorMessage = responseData;
        } else if (error.getMessage() != null) {
            Log.e(TAG, "Error Message: " + error.getMessage());
            errorMessage = error.getMessage();
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private boolean validateInputs() {
        if (categorySpinner.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (exerciseNameEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter an exercise name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (weightEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter weight", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (repsEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter reps", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (setsEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter sets", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            double weight = Double.parseDouble(weightEditText.getText().toString().trim());
            int reps = Integer.parseInt(repsEditText.getText().toString().trim());
            int sets = Integer.parseInt(setsEditText.getText().toString().trim());

            if (weight <= 0 || reps <= 0 || sets <= 0) {
                Toast.makeText(this, "Values must be greater than 0", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void clearInputs() {
        categorySpinner.setText("");
        exerciseNameEditText.setText("");
        weightEditText.setText("");
        repsEditText.setText("");
        setsEditText.setText("");
    }

    private void updateEmptyState() {
        boolean isEmpty = exercisesList.isEmpty();
        emptyStateView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        exerciseListRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onExerciseRemoved(Exercise exercise) {
        int position = exercisesList.indexOf(exercise);
        exercisesList.remove(exercise);
        exerciseAdapter.notifyItemRemoved(position);
        updateEmptyState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXERCISE_SEARCH_REQUEST && resultCode == RESULT_OK && data != null) {
            String selectedExercise = data.getStringExtra("selected_exercise");
            exerciseNameEditText.setText(selectedExercise);
        }
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}