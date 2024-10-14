package com.example.androidexample.main_five_pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;


import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.main_five_pages.SettingsActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    private EditText weightInput;
    private Button postWeightButton;
    private LineChart weightChart;
    //private String url = "http://coms-3090-058.class.las.iastate.edu:8080/users";
    private String url = "http://3a3a3fa2-d4e1-4281-8a26-1ee024d50f35.mock.pstmn.io";
    private String username;
    private float xValue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        username = getIntent().getStringExtra("USERNAME");

        weightInput = findViewById(R.id.weight_input);
        postWeightButton = findViewById(R.id.post_weight_button);
        weightChart = findViewById(R.id.weight_chart);


        postWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postWeight();
            }
        });

        getWeightData();

        // The following is for switching to the other four "main pages of the app" - social, exercise, nutrition, and settings
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.social) {
                        startActivity(new Intent(UserProfileActivity.this, SocialActivity.class));
                        return true;
                    } else if (itemId == R.id.workouts) {
                        startActivity(new Intent(UserProfileActivity.this, WorkoutActivity.class));
                        return true;
                    } else if (itemId == R.id.profile) {
                        startActivity(new Intent(UserProfileActivity.this, UserProfileActivity.class));
                        return true;
                    } else if (itemId == R.id.nutrition) {
                        startActivity(new Intent(UserProfileActivity.this, NutritionActivity.class));
                        return true;
                    } else if (itemId == R.id.settings) {
                        startActivity(new Intent(UserProfileActivity.this, SettingsActivity.class));
                        return true;
                    }
                    return false;
                }
        });
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(UserProfileActivity.this, "Weight posted successfully", Toast.LENGTH_SHORT).show();
                        getWeightData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserProfileActivity.this, "Error posting weight: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void getWeightData() {
        String getUrl = url + "?username=" + username;

        StringRequest request = new StringRequest(Request.Method.GET, getUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<Entry> entries = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dataPoint = jsonArray.getJSONObject(i);
                                float weight = (float) dataPoint.getDouble("weight");
                                entries.add(new Entry(xValue++, weight));
                            }

                            LineDataSet dataSet = new LineDataSet(entries, "Weight");
                            dataSet.setColor(Color.BLUE);
                            dataSet.setCircleColor(Color.RED);

                            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(dataSet);

                            LineData lineData = new LineData(dataSets);
                            weightChart.setData(lineData);
                            weightChart.invalidate();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UserProfileActivity.this, "Error parsing weight data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserProfileActivity.this, "Error fetching weight data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }


}