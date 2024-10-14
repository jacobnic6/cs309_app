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


import com.example.androidexample.DeleteUserActivity;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.editUserActivity;
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
public class SettingsActivity extends AppCompatActivity {

    private Button editUserButton;
    private Button deleteUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editUserButton = findViewById(R.id.edit_user_btn);
        deleteUserButton = findViewById(R.id.delete_user_btn);

        editUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, editUserActivity.class);
                startActivity(intent);
            }
        });
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, DeleteUserActivity.class);
                startActivity(intent);
                }
            });

        // The following is for switching to the other four "main pages of the app" - social, exercise, nutrition, and settings
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()

        {
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem item){
                int itemId = item.getItemId();

                if (itemId == R.id.social) {
                    startActivity(new Intent(SettingsActivity.this, SocialActivity.class));
                    return true;
                } else if (itemId == R.id.workouts) {
                    startActivity(new Intent(SettingsActivity.this, WorkoutActivity.class));
                    return true;
                } else if (itemId == R.id.profile) {
                    startActivity(new Intent(SettingsActivity.this, UserProfileActivity.class));
                    return true;
                } else if (itemId == R.id.nutrition) {
                    startActivity(new Intent(SettingsActivity.this, NutritionActivity.class));
                    return true;
                } else if (itemId == R.id.settings) {
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
