package com.example.androidexample;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;



public class FriendProfileActivity extends AppCompatActivity {
    private TextView usernameText;
    private TextView bioText;
    private TextView ageText;
    private TextView weightText;
    private TextView heightText;
    private RecyclerView muscleProgressRecyclerView;
    private MuscleProgressAdapter muscleProgressAdapter;
    private static final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        initializeViews();  // Initialize views first
        setupRecyclerView();  // Then set up the RecyclerView
        loadFriendData();
    }


    private void initializeViews() {
        usernameText = findViewById(R.id.friend_username);
        bioText = findViewById(R.id.friend_bio);
        ageText = findViewById(R.id.friend_age);
        weightText = findViewById(R.id.friend_weight);
        heightText = findViewById(R.id.friend_height);
        muscleProgressRecyclerView = findViewById(R.id.muscle_progress_recycler);

        Log.d("ViewInitialization", "muscleProgressRecyclerView: " + muscleProgressRecyclerView);
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }


    private void setupRecyclerView() {
        // Initialize adapter with empty list and false for isHistoryView
        muscleProgressAdapter = new MuscleProgressAdapter(new ArrayList<>(), false);
        muscleProgressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        muscleProgressRecyclerView.setAdapter(muscleProgressAdapter);
    }

    private void loadFriendData() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        // Retrieve extras
        String username = intent.getStringExtra("friendUsername");
        String bio = intent.getStringExtra("friendBio");
        int age = intent.getIntExtra("friendAge", 0);
        double weight = intent.getDoubleExtra("friendWeight", 0.0);
        int height = intent.getIntExtra("friendHeight", 0);

        // Set the data to views
        usernameText.setText(username);
        bioText.setText(bio != null ? bio : "No bio available");
        ageText.setText(getString(R.string.age_format, age));
        weightText.setText(getString(R.string.weight_format, weight));
        heightText.setText(getString(R.string.height_format, height));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // This will close the activity and return to previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchFriendProfile(String username) {
        String url = BASE_URL + "/users/username/" + username;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        updateFriendProfile(response);
                    } catch (JSONException e) {
                        Log.e("ProfileError", "Error parsing profile data", e);
                        Toast.makeText(this, "Error loading profile data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("ProfileError", "Error fetching profile", error);
                    Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show();
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void updateFriendProfile(JSONObject profileData) throws JSONException {
        usernameText.setText(profileData.getString("name"));
        bioText.setText(profileData.optString("bio", "No bio available"));
        ageText.setText(getString(R.string.age_format, profileData.optInt("age", 0)));
        weightText.setText(getString(R.string.weight_format, profileData.optDouble("weight", 0.0)));
        heightText.setText(getString(R.string.height_format, profileData.optInt("height", 0)));

        // Parse and update muscle progress
        JSONObject muscleProgressObj = profileData.optJSONObject("muscleProgress");
        if (muscleProgressObj != null) {
            List<MuscleProgress> muscleProgressList = new ArrayList<>();
            Iterator<String> keys = muscleProgressObj.keys();

            while (keys.hasNext()) {
                String muscleName = keys.next();
                JSONObject progressObj = muscleProgressObj.getJSONObject(muscleName);

                // Create MuscleProgress object with the correct constructor
                MuscleProgress progress = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    progress = new MuscleProgress(
                            0,                                                // id (use 0 for friend's progress)
                            muscleName,                                      // muscle name
                            progressObj.optDouble("total_progress", 0.0),    // measurement
                            java.time.LocalDate.now().toString(),            // current date
                            ""                                               // empty notes for friend's progress
                    );
                }

                muscleProgressList.add(progress);
            }


            muscleProgressAdapter.updateData(muscleProgressList);
        }
    }
}