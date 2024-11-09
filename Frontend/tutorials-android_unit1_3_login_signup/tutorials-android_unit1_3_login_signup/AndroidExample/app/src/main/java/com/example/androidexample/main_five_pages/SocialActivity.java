package com.example.androidexample.main_five_pages;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.main_five_pages.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;


public class SocialActivity extends AppCompatActivity {

    EditText searchBar;
    TextView friendsListText;
    Button searchButton;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button);
        friendsListText = findViewById(R.id.friendslist_text);

        username = getIntent().getStringExtra("USERNAME");


        Button chatButton = findViewById(R.id.chat_button);

        getFriends();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchBar.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    searchForFriends(searchQuery);
                }
            }
        });

        // The following is for switching to the other four "main pages of the app" - social, exercise, nutrition, and settings
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.social) {
                    startActivity(new Intent(SocialActivity.this, SocialActivity.class));
                    return true;
                } else if (itemId == R.id.workouts) {
                    startActivity(new Intent(SocialActivity.this, WorkoutActivity.class));
                    return true;
                } else if (itemId == R.id.profile) {
                    startActivity(new Intent(SocialActivity.this, UserProfileActivity.class));
                    return true;
                } else if (itemId == R.id.nutrition) {
                    startActivity(new Intent(SocialActivity.this, NutritionActivity.class));
                    return true;
                } else if (itemId == R.id.settings) {
                    startActivity(new Intent(SocialActivity.this, SettingsActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

        private void searchForFriends (String searchQuery){
            String url = "http://coms-3090-058.class.las.iastate.edu:8080/users/username/" + searchQuery;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest( Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Volley Response", response.toString());
                            Toast.makeText(SocialActivity.this, "User found", Toast.LENGTH_SHORT).show();
                            // Handle the response here
                            showAddFriendPopup();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error", error.toString());
                            Toast.makeText(SocialActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                    return params;
                }
            };

            // Adding request to request queue
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
        }

    // pop up for each muscle group
    private void showAddFriendPopup() {
        // Create and show progress popup dialog
        Dialog dialog = new Dialog(SocialActivity.this);
        dialog.setContentView(R.layout.addfriend_popup); // Create a layout for the popup

        // Get references to views in the popup (e.g., TextView, ProgressBar)
        TextView addFriendText = dialog.findViewById(R.id.Add_friend_text);
        Button yesButton = dialog.findViewById(R.id.add_friend_yes_button);
        Button noButton = dialog.findViewById(R.id.add_friend_no_button);

        dialog.show();

        // Set click listeners for buttons
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Yes" button click
                // add the user to a list of friends
                postRequest(searchBar);

                dialog.dismiss();
            }

        }); noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "No" button click
                Toast.makeText(SocialActivity.this, "User not added as a friend", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void postRequest(EditText searchBar) {
        String url = "http://coms-3090-058.class.las.iastate.edu:8080/friends/1/add";
        // Convert input to JSONObject
        JSONObject postBody = null;
        try {
            // etRequest should contain a JSON object string as your POST body
            // similar to what you would have in POSTMAN-body field
            // and the fields should match with the object structure of @RequestBody on sb
            postBody = new JSONObject(searchBar.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(SocialActivity.this, "User added as a friend", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SocialActivity.this, "Error posting request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                //                headers.put("Content-Type", "application/json");
                return headers;
            }

                @Override
                protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //                params.put("param1", "value1");
                //                params.put("param2", "value2");
                return params;
            }
            };

            // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }

        private void getFriends() {
            String url = "http://coms-3090-058.class.las.iastate.edu:8080/users/username";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest( Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Volley Response", response.toString());
                            Toast.makeText(SocialActivity.this, "Friends list found", Toast.LENGTH_SHORT).show();
                            // Handle the response here
                            friendsListText.setText(response.toString());


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error", error.toString());
                            Toast.makeText(SocialActivity.this, "Friends list not found", Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                    return params;
                }
            };

            // Adding request to request queue
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
        }
}
