package com.example.androidexample.main_five_pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileActivity extends AppCompatActivity {

    private EditText weightInput, heightInput, bioInput, nameInput, ageInput, emailInput, goalInput;
    private Button postWeightButton, saveProfileButton;
    private TextView pastWeightsTextView, usernameText;
    private ImageView profileImage;
    private String username;

    private String weightUrl = "http://coms-3090-058.class.las.iastate.edu:8080/bodyweights/msbecker";
    private String profileUrl = "http://coms-3090-058.class.las.iastate.edu:8080/profile/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        // Get username from intent or set default for direct launch
        username = getIntent().getStringExtra("Username");
        if (username == null) {
            username = "msbecker"; // Set your test username here
        }

        initializeViews();
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

        // Weight tracking views
        weightInput = findViewById(R.id.weight_input);
        postWeightButton = findViewById(R.id.post_weight_button);
        pastWeightsTextView = findViewById(R.id.past_weights_textview);

        // User info views
        usernameText = findViewById(R.id.username_text);
        profileImage = findViewById(R.id.profile_image);

        // Set username in UI
        usernameText.setText(username);

        setupBottomNavigation();
    }

    private void setupClickListeners() {
        postWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postWeight();
            }
        });

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void loadProfileData() {
        String url = profileUrl + username;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("name")) {
                                nameInput.setText(response.getString("name"));
                            }
                            if (response.has("age")) {
                                ageInput.setText(response.getString("age"));
                            }
                            if (response.has("height")) {
                                heightInput.setText(response.getString("height"));
                            }
                            if (response.has("email")) {
                                emailInput.setText(response.getString("email"));
                            }
                            if (response.has("goal")) {
                                goalInput.setText(response.getString("goal"));
                            }
                            if (response.has("bio")) {
                                bioInput.setText(response.getString("bio"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UserProfileActivity.this,
                                    "Error parsing profile data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserProfileActivity.this,
                                "Error loading profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
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
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, weightUrl, postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(UserProfileActivity.this,
                                "Weight posted successfully", Toast.LENGTH_SHORT).show();
                        getWeightData();
                        weightInput.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserProfileActivity.this,
                                "Error posting weight: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void getWeightData() {
        StringRequest request = new StringRequest(Request.Method.GET, weightUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserProfileActivity.this,
                                "Error fetching weight data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void updateProfile() {
        String url = profileUrl + username;

        JSONObject profileData = new JSONObject();
        try {
            profileData.put("name", nameInput.getText().toString());
            profileData.put("age", ageInput.getText().toString());
            profileData.put("height", heightInput.getText().toString());
            profileData.put("email", emailInput.getText().toString());
            profileData.put("goal", goalInput.getText().toString());
            profileData.put("bio", bioInput.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, profileData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(UserProfileActivity.this,
                                "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserProfileActivity.this,
                                "Error updating profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Intent intent;

                if (itemId == R.id.social) {
                    intent = new Intent(UserProfileActivity.this, SocialActivity.class);
                    intent.putExtra("Username", username);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.workouts) {
                    intent = new Intent(UserProfileActivity.this, WorkoutActivity.class);
                    intent.putExtra("Username", username);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.profile) {
                    return true; // Already on profile
                } else if (itemId == R.id.nutrition) {
                    intent = new Intent(UserProfileActivity.this, NutritionActivity.class);
                    intent.putExtra("Username", username);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.settings) {
                    intent = new Intent(UserProfileActivity.this, SettingsActivity.class);
                    intent.putExtra("Username", username);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}