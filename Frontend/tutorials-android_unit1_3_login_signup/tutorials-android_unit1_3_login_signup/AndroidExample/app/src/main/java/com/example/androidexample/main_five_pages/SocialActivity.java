package com.example.androidexample.main_five_pages;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.androidexample.FriendProfileActivity;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.adapters.FriendAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SocialActivity extends AppCompatActivity implements FriendAdapter.OnFriendClickListener {

    private EditText searchBar;
    private RecyclerView friendsRecyclerView;
    private FriendAdapter friendAdapter;
    private String username;
    private static final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        username = getIntent().getStringExtra("Username");
        if (username == null) {
            Toast.makeText(this, "Error: Username not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupRecyclerView();
        getFriends();
    }

    private void initializeViews() {
        searchBar = findViewById(R.id.search_bar);
        Button searchButton = findViewById(R.id.search_button);
        friendsRecyclerView = findViewById(R.id.friends_recycler_view);

        searchButton.setOnClickListener(v -> {
            String searchQuery = searchBar.getText().toString().trim();
            if (!TextUtils.isEmpty(searchQuery)) {
                searchForFriends(searchQuery);
            } else {
                Toast.makeText(this, "Please enter a username to search", Toast.LENGTH_SHORT).show();
            }
        });

        setupBottomNavigation();
    }

    private void setupRecyclerView() {
        friendAdapter = new FriendAdapter(this, this);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerView.setAdapter(friendAdapter);
    }

    private void searchForFriends(String searchQuery) {
        String url = BASE_URL + "/users/username/" + searchQuery;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("Search Response", response.toString());
                    try {
                        String foundUsername = response.getString("username");
                        showAddFriendDialog(foundUsername);
                    } catch (JSONException e) {
                        Log.e("Search Error", "Error parsing response", e);
                        Toast.makeText(this, "Error processing search result", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Search Error", "Error searching for user", error);
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void getFriends() {
        String url = BASE_URL + "/friends/get?userId=" + username;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<FriendAdapter.Friend> friendsList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject friendObj = response.getJSONObject(i);

                            int id = friendObj.getInt("id");
                            String friendUsername = friendObj.getString("name");
                            int age = friendObj.optInt("age", 0);

                            FriendAdapter.Friend friend = new FriendAdapter.Friend(id, friendUsername, age);

                            // Parse additional profile data
                            String bio = friendObj.optString("bio", "");
                            double weight = friendObj.optDouble("weight", 0.0);
                            int height = friendObj.optInt("height", 0);

                            // Parse muscle progress
                            JSONObject muscleProgressObj = friendObj.optJSONObject("muscleProgress");
                            Map<String, FriendAdapter.MuscleProgress> muscleProgress = new HashMap<>();

                            if (muscleProgressObj != null) {
                                Iterator<String> keys = muscleProgressObj.keys();
                                while (keys.hasNext()) {
                                    String muscle = keys.next();
                                    JSONObject progressObj = muscleProgressObj.getJSONObject(muscle);

                                    FriendAdapter.MuscleProgress progress = new FriendAdapter.MuscleProgress();
                                    progress.muscle = muscle;
                                    progress.percentage = progressObj.optDouble("percentage", 0.0);
                                    progress.tier = progressObj.optInt("tier", 0);
                                    progress.total_progress = progressObj.optDouble("total_progress", 0.0);
                                    progress.amount_to_next_tier = progressObj.optDouble("amount_to_next_tier", 0.0);

                                    muscleProgress.put(muscle, progress);
                                }
                            }

                            friend.setFullProfile(bio, weight, height, muscleProgress);
                            friendsList.add(friend);
                        }

                        friendAdapter.updateFriendsList(friendsList);

                    } catch (JSONException e) {
                        Log.e("Friends Error", "Error parsing friends", e);
                        Toast.makeText(this, "Error loading friends", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Friends Error", "Error fetching friends", error);
                    Toast.makeText(this, "Error fetching friends", Toast.LENGTH_SHORT).show();
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void showAddFriendDialog(String friendUsername) {
        new AlertDialog.Builder(this)
                .setTitle("Add Friend")
                .setMessage("Would you like to add " + friendUsername + " as a friend?")
                .setPositiveButton("Add", (dialog, which) -> addFriend(username, friendUsername))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addFriend(String userId, String friendUsername) {
        String url = BASE_URL + "/friends/" + 6 + "/add";

        JSONObject postBody = new JSONObject();
        try {
            postBody.put("username", friendUsername);
        } catch (JSONException e) {
            Log.e("Add Friend Error", "Error creating request body", e);
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postBody,
                response -> {
                    Toast.makeText(this, "Friend added successfully", Toast.LENGTH_SHORT).show();
                    getFriends(); // Refresh the friends list
                },
                error -> {
                    Log.e("Add Friend Error", "Error adding friend", error);
                    String errorMessage = "Error adding friend";
                    if (error.networkResponse != null) {
                        errorMessage += " (Error " + error.networkResponse.statusCode + ")";
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    public void onFriendClick(FriendAdapter.Friend friend) {
        Intent intent = new Intent(this, FriendProfileActivity.class);
        intent.putExtra("friendId", friend.getId());
        intent.putExtra("friendUsername", friend.getUsername());
        intent.putExtra("friendAge", friend.getAge());
        intent.putExtra("friendBio", friend.getBio());
        intent.putExtra("friendWeight", friend.getWeight());
        intent.putExtra("friendHeight", friend.getHeight());
        startActivity(intent);
    }

    @Override
    public void onRemoveFriend(FriendAdapter.Friend friend) {
        new AlertDialog.Builder(this)
                .setTitle("Remove Friend")
                .setMessage("Are you sure you want to remove " + friend.getUsername() + " from your friends?")
                .setPositiveButton("Remove", (dialog, which) -> removeFriend(friend))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void removeFriend(FriendAdapter.Friend friend) {
        String url = BASE_URL + "/friends/" + username + "/" + friend.getUsername();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    Toast.makeText(this, "Friend removed successfully", Toast.LENGTH_SHORT).show();
                    friendAdapter.removeFriend(friend);
                },
                error -> {
                    Log.e("Remove Friend Error", "Error removing friend", error);
                    Toast.makeText(this, "Error removing friend", Toast.LENGTH_SHORT).show();
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if (item.getItemId() == R.id.social) {
                    return true;
                } else if (item.getItemId() == R.id.workouts) {
                    intent = new Intent(SocialActivity.this, WorkoutActivity.class);
                } else if (item.getItemId() == R.id.profile) {
                    intent = new Intent(SocialActivity.this, UserProfileActivity.class);
                } else if (item.getItemId() == R.id.nutrition) {
                    intent = new Intent(SocialActivity.this, NutritionActivity.class);
                } else if (item.getItemId() == R.id.settings) {
                    intent = new Intent(SocialActivity.this, SettingsActivity.class);
                } else {
                    return false;
                }
                intent.putExtra("Username", username);
                startActivity(intent);
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.social);
    }
}