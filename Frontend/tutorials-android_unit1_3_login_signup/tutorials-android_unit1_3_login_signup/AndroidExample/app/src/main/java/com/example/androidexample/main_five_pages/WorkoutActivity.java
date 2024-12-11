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
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.LogWorkoutActivity;
import com.example.androidexample.MuscleProgressActivity;
import com.example.androidexample.R;
import com.example.androidexample.SessionManager;
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

    // Data and Adapters
    private WorkoutAdapter workoutAdapter;
    private List<Workout> workoutsList;
    private String userId;

    // Services and Utilities
    private final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";
    private WorkoutDatabase workoutDatabase;
    private NotificationService notificationService;
    private RequestQueue requestQueue;
    private SessionManager sessionManager;

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
    }

    private void initializeServices() {
        workoutDatabase = new WorkoutDatabase(this);
        notificationService = new NotificationService(this);
        requestQueue = Volley.newRequestQueue(this);
        sessionManager = SessionManager.getInstance(this);
        workoutsList = new ArrayList<>();
        userId = sessionManager.getUserId();

        if (userId == null) {
            userId = getIntent().getStringExtra("Username");
            if (userId == null) {
                Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
    }

    private void initializeViews() {
        workoutRecyclerView = findViewById(R.id.workout_recycler_view);
        emptyStateView = findViewById(R.id.empty_state_view);
        workoutCountText = findViewById(R.id.workout_count_text);
        streakCountText = findViewById(R.id.streak_count_text);
        trackProgressFab = findViewById(R.id.track_progress_fab);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        View addWorkoutButton = findViewById(R.id.add_workout_btn);
        if (addWorkoutButton != null) {
            addWorkoutButton.setOnClickListener(v -> createEmptyWorkout());
        } else {
            Log.e(TAG, "Add workout button not found in layout");
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
    }

    private void setupRecyclerView() {
        if (workoutRecyclerView != null) {
            workoutAdapter = new WorkoutAdapter(workoutsList, this);
            workoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            workoutRecyclerView.setAdapter(workoutAdapter);
        } else {
            Log.e(TAG, "RecyclerView not found in layout");
        }
    }

    private void setupBottomNavigation() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.workouts);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                Intent intent = null;
                int itemId = item.getItemId();

                if (itemId == R.id.social) {
                    intent = new Intent(this, SocialActivity.class);
                } else if (itemId == R.id.workouts) {
                    return true;
                } else if (itemId == R.id.profile) {
                    intent = new Intent(this, UserProfileActivity.class);
                } else if (itemId == R.id.nutrition) {
                    intent = new Intent(this, NutritionActivity.class);
                } else if (itemId == R.id.settings) {
                    intent = new Intent(this, SettingsActivity.class);
                }

                if (intent != null) {
                    intent.putExtra("Username", userId);
                    startActivity(intent);
                    return true;
                }
                return false;
            });
        }
    }

    private void setupFab() {
        if (trackProgressFab != null) {
            trackProgressFab.setOnClickListener(v -> {
                Intent intent = new Intent(this, MuscleProgressActivity.class);
                startActivity(intent);
            });
        }
    }

    private void createEmptyWorkout() {
        String url = String.format("%s/workout/%s/%s", BASE_URL, userId, getCurrentDate());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(),
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
        String url = String.format("%s/workout/%s", BASE_URL, userId);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        workoutsList.clear();
                        JSONArray workoutArray = response.getJSONArray("workouts");

                        for (int i = 0; i < workoutArray.length(); i++) {
                            JSONObject workoutJson = workoutArray.getJSONObject(i);
                            Workout workout = new Workout(
                                    workoutJson.getInt("id"),
                                    workoutJson.optString("workoutName", "Workout " + (i + 1)),
                                    workoutJson.getString("dateTracked"),
                                    workoutJson.optJSONArray("activities").length()
                            );

                            // Set total weight if available
                            if (workoutJson.has("totalWeight")) {
                                workout.setTotalWeight(workoutJson.getDouble("totalWeight"));
                            } else {
                                calculateTotalWeight(workout, workoutJson);
                            }

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

    private void calculateTotalWeight(Workout workout, JSONObject workoutJson) {
        try {
            double totalWeight = 0;
            JSONArray activities = workoutJson.getJSONArray("activities");
            for (int j = 0; j < activities.length(); j++) {
                JSONObject activity = activities.getJSONObject(j);
                totalWeight += activity.getDouble("weight") *
                        activity.getInt("sets") *
                        activity.getInt("reps");
            }
            workout.setTotalWeight(totalWeight);
        } catch (Exception e) {
            Log.e(TAG, "Error calculating total weight: " + e.getMessage());
        }
    }

    private void loadLocalWorkouts() {
        List<Workout> localWorkouts = workoutDatabase.getAllWorkouts();
        workoutsList.clear();
        workoutsList.addAll(localWorkouts);
        updateWorkoutList();
    }

    private void updateWorkoutList() {
        if (workoutAdapter != null) {
            workoutAdapter.notifyDataSetChanged();
            updateEmptyState();
        }
    }

    private void updateEmptyState() {
        boolean isEmpty = workoutsList.isEmpty();
        if (emptyStateView != null) {
            emptyStateView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        }
        if (workoutRecyclerView != null) {
            workoutRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }
        if (workoutCountText != null) {
            workoutCountText.setText(String.valueOf(workoutsList.size()));
        }
    }

    private void updateStats() {
        if (workoutCountText != null) {
            workoutCountText.setText(String.valueOf(workoutsList.size()));
        }

        int streak = calculateStreak();
        if (streakCountText != null) {
            streakCountText.setText(String.valueOf(streak));
        }

        if (streak >= 3) {
            notificationService.showWorkoutStreak(streak);
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
                if (nextDate != null && currentDate != null) {
                    long diffInDays = (currentDate.getTime() - nextDate.getTime()) / (1000 * 60 * 60 * 24);
                    if (diffInDays == 1) {
                        streak++;
                        currentDate = nextDate;
                    } else {
                        break;
                    }
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
        String url = String.format("%s/workout/id/%d", BASE_URL, workout.getId());

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
                error -> {
                    Log.e(TAG, "Error deleting workout: " + error.getMessage());
                    Toast.makeText(this, "Error deleting workout", Toast.LENGTH_SHORT).show();
                }
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