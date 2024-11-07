package com.example.androidexample.main_five_pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
    private TextView pastWeightsTextView;
    //private LineChart weightChart;
    private String url = "http://coms-3090-058.class.las.iastate.edu:8080/bodyweights/username";
    // private String url = "http://3a3a3fa2-d4e1-4281-8a26-1ee024d50f35.mock.pstmn.io";
    private String username;
    //private float xValue = 0;

    // Body diagram
    private ImageView frontAvatar;
    private ImageView backAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        username = getIntent().getStringExtra("USERNAME");

        weightInput = findViewById(R.id.weight_input);
        postWeightButton = findViewById(R.id.post_weight_button);
        //weightChart = findViewById(R.id.weight_chart);
        pastWeightsTextView = findViewById(R.id.past_weights_textview);

        // Muscle diagram
        frontAvatar = findViewById(R.id.front_avatar);
        backAvatar = findViewById(R.id.back_avatar);
        backAvatar.setOnTouchListener(touchListener);
        frontAvatar.setOnTouchListener(touchListener);


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

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

            if (v == frontAvatar) {
                System.out.println(x + " " + y);

                // front
                if (isQuadsRegion(x, y)) {
                    showProgressPopup("Quads");
                } else if (isAbsRegion(x, y)) {
                    showProgressPopup("Abs");
                } else if (isbicpsRegion(x, y)) {
                    showProgressPopup("Biceps");
                } else if (ischestRegion(x, y)) {
                    showProgressPopup("Chest");
                }
            }
                if (v == backAvatar) {
                // back
                    System.out.println(x + " " + y);

                 if (istricepsRegion(x, y)) {
                    showProgressPopup("Triceps");
                }
                if (isbackRegion(x, y)) {
                    showProgressPopup("Back");
                }
                if (isHamstringsRegion(x, y)) {
                    showProgressPopup("Hamstrings");
                }
                if (isCalvesRegion(x, y)) {
                    showProgressPopup("Calves");
                }
            }
            }
            return true;
        }
    };

// helper methods for defining the specific regions within the body diagram left muscles
private boolean isQuadsRegion(int x, int y) {
    // 133 x 194 on right image
    Rect QuadsRectLeft = new Rect(127, 409, 234, 650);
    Rect QuadsRectRight = new Rect(263, 409, 369, 650);
    return QuadsRectLeft.contains(x, y) || QuadsRectRight.contains(x, y);
}

    private boolean isAbsRegion(int x, int y) {
        // 124 x 126 on right image
        Rect AbsRect = new Rect(156, 270, 340, 415);
        return AbsRect.contains(x, y);
    }

private boolean isbicpsRegion(int x, int y) {
    // 34 x 83 on right image
    Rect bicepsRectLeft = new Rect(57, 222, 130, 320);
    Rect bicepsRectRight = new Rect(366, 222, 437, 320);
    return bicepsRectLeft.contains(x, y) || bicepsRectRight.contains(x, y);
    }

    private boolean ischestRegion(int x, int y) {
        // 123 x 63 on right image
        Rect ChestRect = new Rect(140, 140, 360,250);
        return ChestRect.contains(x, y);
    }

    private boolean istricepsRegion(int x, int y) {
        // 44 x 65 on left image
        Rect TricepsRectLeft = new Rect(42, 212, 110, 290);
        Rect TricepsRectRight = new Rect(361, 212, 429, 290);
        return TricepsRectLeft.contains(x, y) || TricepsRectRight.contains(x, y);
    }

    private boolean isbackRegion(int x, int y) {
        // 135 x 202 on left image
        Rect BackRect = new Rect(115, 96, 355, 409);
        return BackRect.contains(x, y);
    }

    private boolean isHamstringsRegion(int x, int y) {
        // 134 x 168 on the left image
        Rect HamstringsRectLeft = new Rect(113, 530, 219, 686);
        Rect HamstringsRectRight = new Rect(248, 530, 351, 686);
        return HamstringsRectLeft.contains(x, y) || HamstringsRectRight.contains(x, y);
    }

    private boolean isCalvesRegion(int x, int y) {
        // 111 x 170 on left image
        Rect CalvesRectLeft = new Rect(103, 690, 206, 890);
        Rect CalvesRectRight = new Rect(258, 690, 361, 890);
        return CalvesRectLeft.contains(x, y) || CalvesRectRight.contains(x, y);
    }


    // pop up for each muscle group
    private void showProgressPopup(String muscleGroup) {
        // Create and show progress popup dialog
        Dialog dialog = new Dialog(UserProfileActivity.this);
        dialog.setContentView(R.layout.progress_popup); // Create a layout for the popup

        // Get references to views in the popup (e.g., TextView, ProgressBar)
        TextView progressText = dialog.findViewById(R.id.progress_text);
        ProgressBar progressBar = dialog.findViewById(R.id.progress_bar);

        // Set progress data and update progress bar
        progressText.setText(muscleGroup + " Progress: " + getProgressForMuscle(muscleGroup));
        progressBar.setProgress(getProgressPercentageForMuscle(muscleGroup));

        dialog.show();
    }

    // Helper method to get progress for a specific muscle group
    private int getProgressForMuscle(String muscleGroup) {
        return 0;
    }

    // Helper method to get progress percentage for a specific muscle group
    private int getProgressPercentageForMuscle(String muscleGroup) {
        return 0;
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
                        weightInput.setText("");
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
            StringRequest request = new StringRequest(Request.Method.GET, url,
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

                                pastWeightsTextView.setText(weightsBuilder.toString()); // Set text to TextView

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(com.example.androidexample.main_five_pages.UserProfileActivity.this, "Error parsing weight data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(com.example.androidexample.main_five_pages.UserProfileActivity.this, "Error fetching weight data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
    }