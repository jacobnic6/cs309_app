package com.example.androidexample.main_five_pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SocialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_social);

    // The following is for switching to the other four "main pages of the app" - social, exercise, nutrition, and settings
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item)
        {
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
}
