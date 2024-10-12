package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


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

    /* The Following buttons will be used to switch the intent to the respective page
    private Button SocialButton;      // Define Social button variable
    private Button ExerciseButton;    // Define Exercise button variable
    private Button NutritionButton;   // Define Nutrition button variable
    private Button SettingsButton;    // Define Settings button variable


    /* The following is for switching to the other four "main pages of the app" - social, exercise, nutrition, and settings
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);            // link to Login activity XML

        // initialize UI elements
        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);    // link to login button in the Login activity XML
        signupButton = findViewById(R.id.login_signup_btn);  // link to signup button in the Login activity XML

        // click listener on Social button pressed
        SocialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // when signup button is pressed, use intent to switch to Signup Activity
                Intent intent = new Intent(UserProfileActivity.this, SocialActivity.class);
                startActivity(intent);  // go to SocialActivity
            }
        });

        // click listener on Social button pressed
        ExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        // when signup button is pressed, use intent to switch to Signup Activity
        Intent intent = new Intent(UserProfileActivity.this, SocialActivity.class);
                startActivity(intent);  // go to SocialActivity
            }
        });

        // click listener on Social button pressed
        NutritionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // when signup button is pressed, use intent to switch to Signup Activity
                Intent intent = new Intent(UserProfileActivity.this, SocialActivity.class);
                startActivity(intent);  // go to SocialActivity
            }
        });

        // click listener on Social button pressed
        SettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // when signup button is pressed, use intent to switch to Signup Activity
                Intent intent = new Intent(UserProfileActivity.this, SocialActivity.class);
                startActivity(intent);  // go to SocialActivity
            }
        });
    }

     */


