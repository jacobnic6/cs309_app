package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public abstract class WorkoutListActivity extends AppCompatActivity implements WorkoutAdapter.WorkoutClickListener {
    private RecyclerView workoutListRecyclerView;
    private FloatingActionButton addWorkoutFab;
    private View emptyStateText;
    private WorkoutAdapter adapter;
    private final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";
    private WorkoutDatabase workoutDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        workoutDatabase = new WorkoutDatabase(this);

        initializeViews();
        setupRecyclerView();
        setupAddButton();
        fetchWorkoutsFromLocalDatabase();
        fetchWorkoutsFromBackend();
    }

    private void initializeViews() {
        workoutListRecyclerView = findViewById(R.id.workout_list_recycler);
        addWorkoutFab = findViewById(R.id.add_workout_fab);
        emptyStateText = findViewById(R.id.empty_state_text);
    }

    private void setupRecyclerView() {
        workoutListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupAddButton() {
        addWorkoutFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, LogWorkoutActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchWorkoutsFromLocalDatabase();
    }

    private void fetchWorkoutsFromLocalDatabase() {
        List<Workout> workouts = workoutDatabase.getAllWorkouts();
        updateWorkoutList(workouts);
    }

    private void fetchWorkoutsFromBackend() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_URL, null,
                response -> {
                    List<Workout> workouts = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject workout = response.getJSONObject(i);
                            Workout fetchedWorkout = new Workout(
                                    workout.getInt("id"),
                                    workout.getString("workoutName"),
                                    workout.getString("dateTracked"),
                                    workout.getJSONArray("exercises").length()
                            );
                            workouts.add(fetchedWorkout);
                            workoutDatabase.saveWorkout(fetchedWorkout,true);
                        }
                        updateWorkoutList(workouts);
                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing workouts", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error fetching workouts", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void updateWorkoutList(List<Workout> workouts) {
        adapter = new WorkoutAdapter(workouts, this);
        workoutListRecyclerView.setAdapter(adapter);

        // Update empty state visibility
        if (workouts.isEmpty()) {
            emptyStateText.setVisibility(View.VISIBLE);
            workoutListRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateText.setVisibility(View.GONE);
            workoutListRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onWorkoutClick(Workout workout) {  // Changed from int workoutId
        Intent intent = new Intent(this, LogWorkoutActivity.class);
        intent.putExtra("WORKOUT_ID", workout.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Workout workout) {  // Changed from onDeleteWorkout
        workoutDatabase.deleteWorkout(workout.getId());
        fetchWorkoutsFromLocalDatabase();

        String deleteUrl = BASE_URL + "/workout/id" + workout.getId();
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, deleteUrl,
                response -> {
                    Toast.makeText(this, "Workout deleted successfully", Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(this, "Error deleting workout", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(deleteRequest);
    }
}