package com.example.androidexample.main_five_pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.LogWorkoutActivity;
import com.example.androidexample.MuscleProgressActivity;
import com.example.androidexample.R;
import com.example.androidexample.Workout;
import com.example.androidexample.WorkoutAdapter;
import com.example.androidexample.WorkoutDatabase;
import com.example.androidexample.services.NotificationService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkoutActivity extends AppCompatActivity implements WorkoutAdapter.WorkoutClickListener {
    private static final String TAG = "WorkoutActivity";

    // UI Components
    private RecyclerView workoutRecyclerView;
    private View emptyStateView;
    private TextView workoutCountText;
    private TextView streakCountText;
    private ExtendedFloatingActionButton trackProgressFab;
    private BottomNavigationView bottomNavigationView;

    // Adapters and Data
    private WorkoutAdapter workoutAdapter;
    private List<Workout> workoutsList;

    // Services and Utilities
    private final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";
    private WorkoutDatabase workoutDatabase;
    private NotificationService notificationService;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        initializeServices();
        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupBottomNavigation();
        setupFab();

        refreshWorkouts();
        updateStats();
    }

    private void initializeServices() {
        workoutDatabase = new WorkoutDatabase(this);
        notificationService = new NotificationService(this);
        requestQueue = Volley.newRequestQueue(this);
        workoutsList = new ArrayList<>();
    }

    private void initializeViews() {
        workoutRecyclerView = findViewById(R.id.workout_recycler_view);
        emptyStateView = findViewById(R.id.empty_state_view);
        workoutCountText = findViewById(R.id.workout_count_text);
        streakCountText = findViewById(R.id.streak_count_text);
        trackProgressFab = findViewById(R.id.track_progress_fab);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        findViewById(R.id.add_workout_btn).setOnClickListener(v -> startNewWorkout());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupRecyclerView() {
        workoutAdapter = new WorkoutAdapter(workoutsList, this);
        workoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutRecyclerView.setAdapter(workoutAdapter);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.workouts);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.social) {
                startActivity(new Intent(this, SocialActivity.class));
                return true;
            } else if (itemId == R.id.workouts) {
                return true;
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(this, UserProfileActivity.class));
                return true;
            } else if (itemId == R.id.nutrition) {
                startActivity(new Intent(this, NutritionActivity.class));
                return true;
            } else if (itemId == R.id.settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupFab() {
        trackProgressFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, MuscleProgressActivity.class);
            startActivity(intent);
        });
    }

    private void startNewWorkout() {
        String userId = "billy123"; // Should come from SharedPreferences
        String currentDate = getCurrentDate();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                String.format("%s/workout/%s/%s", BASE_URL, userId, currentDate),
                null,
                response -> {
                    try {
                        int workoutId = response.getInt("id");
                        Intent intent = new Intent(this, LogWorkoutActivity.class);
                        intent.putExtra("WORKOUT_ID", workoutId);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing workout creation response: " + e.getMessage());
                        Toast.makeText(this, "Error creating workout", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error creating workout: " + error.getMessage());
                    Toast.makeText(this, "Error creating workout", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(request);
    }

    private void refreshWorkouts() {
        String userId = "billy123"; // Should come from SharedPreferences
        String url = String.format("%s/workouts/%s", BASE_URL, userId);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray workoutsArray;
                        // Check if response is a JSONObject with "workouts" array or direct array
                        if (response.has("workouts")) {
                            workoutsArray = response.getJSONArray("workouts");
                        } else {
                            // If response is directly a JSONArray, convert it
                            workoutsArray = new JSONArray(response.toString());
                        }

                        workoutsList.clear();

                        for (int i = 0; i < workoutsArray.length(); i++) {
                            JSONObject workoutJson = workoutsArray.getJSONObject(i);
                            Workout workout = new Workout(
                                    workoutJson.getInt("id"),
                                    workoutJson.getString("name"),
                                    workoutJson.getString("date"),
                                    workoutJson.getJSONArray("exercises").length()
                            );
                            workoutsList.add(workout);
                            workoutDatabase.saveWorkout(workout, true);
                        }

                        updateWorkoutList();
                        updateStats();
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing workouts: " + e.getMessage());
                        loadLocalWorkouts();
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching workouts: " + error.getMessage());
                    loadLocalWorkouts();
                }
        );

        requestQueue.add(request);
    }

    private void loadLocalWorkouts() {
        List<Workout> localWorkouts = workoutDatabase.getAllWorkouts();
        workoutsList.clear();
        workoutsList.addAll(localWorkouts);
        updateWorkoutList();
    }

    private void updateWorkoutList() {
        workoutAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        boolean isEmpty = workoutsList.isEmpty();
        emptyStateView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        workoutRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void updateStats() {
        // Update total workout count
        workoutCountText.setText(String.valueOf(workoutsList.size()));

        // Calculate and update streak
        int streak = calculateStreak();
        streakCountText.setText(String.valueOf(streak));

        // Show streak achievement if applicable
        if (streak >= 3) {
            notificationService.showWorkoutReminder(
                    "Workout Streak!",
                    String.format(Locale.getDefault(),
                            "Amazing! You've worked out %d days in a row!", streak)
            );
        }
    }

    private int calculateStreak() {
        if (workoutsList.isEmpty()) return 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        int streak = 1;

        try {
            Date currentDate = sdf.parse(workoutsList.get(0).getDate());
            for (int i = 1; i < workoutsList.size(); i++) {
                Date nextDate = sdf.parse(workoutsList.get(i).getDate());
                long diffInDays = (currentDate.getTime() - nextDate.getTime()) / (1000 * 60 * 60 * 24);
                if (diffInDays == 1) {
                    streak++;
                    currentDate = nextDate;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error calculating streak: " + e.getMessage());
        }

        return streak;
    }

    @Override
    public void onWorkoutClick(Workout workout) {
        Intent intent = new Intent(this, LogWorkoutActivity.class);
        intent.putExtra("WORKOUT_ID", workout.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Workout workout) {
        String url = BASE_URL + "/workout/" + workout.getId();

        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                response -> {
                    workoutDatabase.deleteWorkout(workout.getId());
                    int position = workoutsList.indexOf(workout);
                    workoutsList.remove(workout);
                    workoutAdapter.notifyItemRemoved(position);
                    updateEmptyState();
                    updateStats();
                    Toast.makeText(this, "Workout deleted successfully", Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(this, "Error deleting workout", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(deleteRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshWorkouts();
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}