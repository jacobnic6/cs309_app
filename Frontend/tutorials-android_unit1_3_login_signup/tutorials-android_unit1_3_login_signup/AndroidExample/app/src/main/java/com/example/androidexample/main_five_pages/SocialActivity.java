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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.adapters.FriendAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SocialActivity extends AppCompatActivity implements FriendAdapter.OnFriendClickListener {

    private EditText searchBar;
    private RecyclerView friendsRecyclerView;
    private FriendAdapter friendAdapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        username = getIntent().getStringExtra("Username");
        initializeViews();
        setupRecyclerView();
        getFriends(username);
    }

    private void initializeViews() {
        searchBar = findViewById(R.id.search_bar);
        Button searchButton = findViewById(R.id.search_button);
        Button chatButton = findViewById(R.id.chat_button);
        friendsRecyclerView = findViewById(R.id.friends_recycler_view);

        searchButton.setOnClickListener(v -> {
            String searchQuery = searchBar.getText().toString();
            if (!TextUtils.isEmpty(searchQuery)) {
                searchForFriends(searchQuery);
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
        String url = "http://coms-3090-058.class.las.iastate.edu:8080/users/username/" + searchQuery;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("Volley Response", response.toString());
                    Toast.makeText(this, "User found", Toast.LENGTH_SHORT).show();
                    showAddFriendDialog(searchQuery);
                },
                error -> {
                    Log.e("Volley Error", error.toString());
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void getFriends(String username) {
        String url = "http://coms-3090-058.class.las.iastate.edu:8080/friends/get?userId=" + username;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<FriendAdapter.Friend> friendsList = new ArrayList<>();
                        JSONArray friendsArray = response.getJSONArray("friends");
                        for (int i = 0; i < friendsArray.length(); i++) {
                            JSONObject friendObj = friendsArray.getJSONObject(i);
                            String friendUsername = friendObj.getString("username");
                            friendsList.add(new FriendAdapter.Friend(friendUsername));
                        }
                        friendAdapter.updateFriendsList(friendsList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error fetching friends", Toast.LENGTH_SHORT).show());

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void showAddFriendDialog(String friendUsername) {
        // Your existing showAddFriendPopup() implementation
    }

    @Override
    public void onFriendClick(FriendAdapter.Friend friend) {
        // Handle friend click - maybe open a chat or profile
        Toast.makeText(this, "Clicked on: " + friend.getUsername(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveFriend(FriendAdapter.Friend friend) {
        // Implement friend removal logic
        String url = "http://coms-3090-058.class.las.iastate.edu:8080/friends/" + username + "/" + friend.getUsername();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    Toast.makeText(this, "Friend removed", Toast.LENGTH_SHORT).show();
                    getFriends(username); // Refresh the list
                },
                error -> Toast.makeText(this, "Error removing friend", Toast.LENGTH_SHORT).show());

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
    }
}