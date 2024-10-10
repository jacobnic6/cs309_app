package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    private EditText weightInput;
    private Button submitButton;
    private GraphView weightGraph;
    private double xValue = 0;
    private static final int MAX_DATA_POINTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        weightInput = findViewById(R.id.input_weight);
        submitButton = findViewById(R.id.submit_input_weight);
        weightGraph = findViewById(R.id.graph);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = weightInput.getText().toString();
                submitWeightData(weight);
            }
        });
    }

    private void submitWeightData(String weight) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://coms-3090-058.class.las.iastate.edu:8080/users";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response (e.g., update graph)
                        androidx.media3.common.util.Log.d("Success", response);
                        updateGraph(weight);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                androidx.media3.common.util.Log.e("Error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("weight", weight);
                // Add other user data as needed
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void updateGraph(String weight) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        series.appendData(new DataPoint(xValue++, Double.parseDouble(weight)), true, MAX_DATA_POINTS);
        weightGraph.addSeries(series); // Add series to the graph
    }
}





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


