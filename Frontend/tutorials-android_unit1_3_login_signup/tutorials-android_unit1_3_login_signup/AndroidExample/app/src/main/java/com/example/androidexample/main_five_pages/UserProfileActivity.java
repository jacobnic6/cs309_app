package com.example.androidexample.main_five_pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.WorkoutDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    private EditText weightInput, heightInput, bioInput, nameInput, ageInput, emailInput, goalInput;
    private Button postWeightButton, saveProfileButton;
    private TextView pastWeightsTextView, usernameText, levelText;
    private ImageView profileImage;
    private RecyclerView muscleProgressRecycler;
    private MuscleProgressAdapter muscleProgressAdapter;
    private WorkoutDatabase workoutDatabase;
    private String username;

    private static final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";
    private String weightUrl;
    private String profileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        workoutDatabase = new WorkoutDatabase(this);
        username = getIntent().getStringExtra("Username");
        if (username == null) {
            username = "msbecker";
        }

        weightUrl = BASE_URL + "/bodyweights/" + username;
        profileUrl = BASE_URL + "/profile/" + username;

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        loadProfileData();
        getWeightData();
    }

    private void initializeViews() {
        // Profile views
        nameInput = findViewById(R.id.name_input);
        ageInput = findViewById(R.id.age_input);
        heightInput = findViewById(R.id.height_input);
        emailInput = findViewById(R.id.email_input);
        goalInput = findViewById(R.id.goal_input);
        bioInput = findViewById(R.id.bio_input);
        saveProfileButton = findViewById(R.id.save_profile_button);
        levelText = findViewById(R.id.level_text);

        // Weight tracking views
        weightInput = findViewById(R.id.weight_input);
        postWeightButton = findViewById(R.id.post_weight_button);
        pastWeightsTextView = findViewById(R.id.past_weights_textview);

        // User info views
        usernameText = findViewById(R.id.username_text);
        profileImage = findViewById(R.id.profile_image);
        muscleProgressRecycler = findViewById(R.id.muscle_progress_recycler);

        usernameText.setText(username);
        setupBottomNavigation();
    }

    private void setupRecyclerView() {
        muscleProgressAdapter = new MuscleProgressAdapter();
        muscleProgressRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        muscleProgressRecycler.setAdapter(muscleProgressAdapter);
    }

    private void setupClickListeners() {
        postWeightButton.setOnClickListener(v -> postWeight());
        saveProfileButton.setOnClickListener(v -> updateProfile());
    }

    private void loadProfileData() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, profileUrl, null,
                response -> {
                    try {
                        updateBasicProfile(response);
                        if (response.has("muscleProgress")) {
                            updateMuscleProgress(response.getJSONObject("muscleProgress"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing profile data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error loading profile: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show());

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void updateBasicProfile(JSONObject response) throws JSONException {
        if (response.has("name")) nameInput.setText(response.getString("name"));
        if (response.has("age")) ageInput.setText(String.valueOf(response.getInt("age")));
        if (response.has("height")) heightInput.setText(String.valueOf(response.getInt("height")));
        if (response.has("bio")) bioInput.setText(response.getString("bio"));
        if (response.has("fitnessGoal")) goalInput.setText(response.getString("fitnessGoal"));
        if (response.has("weight")) weightInput.setText(String.valueOf(response.getDouble("weight")));
    }

    private void updateMuscleProgress(JSONObject muscleProgress) throws JSONException {
        List<MuscleProgressItem> progressItems = new ArrayList<>();
        Iterator<String> keys = muscleProgress.keys();

        while (keys.hasNext()) {
            String muscle = keys.next();
            JSONObject stats = muscleProgress.getJSONObject(muscle);

            progressItems.add(new MuscleProgressItem(
                    muscle,
                    stats.getDouble("percentage"),
                    stats.getInt("tier"),
                    stats.getDouble("total_progress"),
                    stats.getDouble("amount_to_next_tier")
            ));
        }

        muscleProgressAdapter.updateItems(progressItems);
        updateTotalLevel(progressItems);
    }

    private void updateTotalLevel(List<MuscleProgressItem> items) {
        double totalProgress = 0;
        for (MuscleProgressItem item : items) {
            totalProgress += item.totalProgress;
        }
        int level = (int) Math.floor(totalProgress / 10);
        levelText.setText("Level " + level);
    }

    private void postWeight() {
        String weight = weightInput.getText().toString();
        if (weight.isEmpty()) {
            Toast.makeText(this, "Please enter a weight", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject postBody = new JSONObject();
        try {
            postBody.put("username", username);
            postBody.put("weight", weight);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, weightUrl, postBody,
                response -> {
                    Toast.makeText(UserProfileActivity.this,
                            "Weight posted successfully", Toast.LENGTH_SHORT).show();
                    getWeightData();
                    weightInput.setText("");
                },
                error -> Toast.makeText(UserProfileActivity.this,
                        "Error posting weight: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void getWeightData() {
        StringRequest request = new StringRequest(Request.Method.GET, weightUrl,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        StringBuilder weightsBuilder = new StringBuilder();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataPoint = jsonArray.getJSONObject(i);
                            float weight = (float) dataPoint.getDouble("weight");
                            weightsBuilder.append(weight);

                            if (i < jsonArray.length() - 1) {
                                weightsBuilder.append(", ");
                            }
                        }

                        pastWeightsTextView.setText(weightsBuilder.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(UserProfileActivity.this,
                                "Error parsing weight data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(UserProfileActivity.this,
                        "Error fetching weight data: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void updateProfile() {
        JSONObject profileData = new JSONObject();
        try {
            profileData.put("name", nameInput.getText().toString());
            profileData.put("age", Integer.parseInt(ageInput.getText().toString()));
            profileData.put("height", Integer.parseInt(heightInput.getText().toString()));
            profileData.put("email", emailInput.getText().toString());
            profileData.put("fitnessGoal", goalInput.getText().toString());
            profileData.put("bio", bioInput.getText().toString());
        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Please check your inputs", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, profileUrl, profileData,
                response -> Toast.makeText(UserProfileActivity.this,
                        "Profile updated successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(UserProfileActivity.this,
                        "Error updating profile: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            int itemId = item.getItemId();

            if (itemId == R.id.social) {
                intent = new Intent(this, SocialActivity.class);
            } else if (itemId == R.id.workouts) {
                intent = new Intent(this, WorkoutActivity.class);
            } else if (itemId == R.id.profile) {
                return true; // Already on profile
            } else if (itemId == R.id.nutrition) {
                intent = new Intent(this, NutritionActivity.class);
            } else if (itemId == R.id.settings) {
                intent = new Intent(this, SettingsActivity.class);
            } else {
                return false;
            }

            intent.putExtra("Username", username);
            startActivity(intent);
            return true;
        });
    }

    // Inner classes
    private static class MuscleProgressItem {
        String muscleName;
        double percentage;
        int tier;
        double totalProgress;
        double amountToNextTier;

        MuscleProgressItem(String muscleName, double percentage, int tier,
                           double totalProgress, double amountToNextTier) {
            this.muscleName = muscleName;
            this.percentage = percentage;
            this.tier = tier;
            this.totalProgress = totalProgress;
            this.amountToNextTier = amountToNextTier;
        }
    }

    private static class MuscleProgressAdapter extends RecyclerView.Adapter<MuscleProgressAdapter.ViewHolder> {
        private List<MuscleProgressItem> items = new ArrayList<>();

        void updateItems(List<MuscleProgressItem> newItems) {
            items = newItems;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_muscle_progress, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MuscleProgressItem item = items.get(position);
            holder.muscleName.setText(item.muscleName);
            holder.progressBar.setProgress((int) item.percentage);
            holder.levelText.setText("Level " + (int)(item.totalProgress / 10));

            // Set tier indicator (trophy)
            holder.tierIndicator.setImageResource(
                    item.tier > 0 ? R.drawable.ic_trophy_gold : R.drawable.ic_trophy_gray
            );

            // Calculate and display next milestone
            String nextMilestone = String.format("%.1f to next level", item.amountToNextTier);
            holder.measurementText.setText(nextMilestone);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView muscleName;
            ProgressBar progressBar;
            ImageView tierIndicator;
            TextView levelText;
            TextView measurementText;

            ViewHolder(@NonNull View view) {
                super(view);
                muscleName = view.findViewById(R.id.muscle_name);
                progressBar = view.findViewById(R.id.progress_bar);
                tierIndicator = view.findViewById(R.id.tier_indicator);
                levelText = view.findViewById(R.id.level_text);
                measurementText = view.findViewById(R.id.measurement_text);
            }
        }
    }
}