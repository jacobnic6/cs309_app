package com.example.androidexample.main_five_pages;

import static com.example.androidexample.R.id.searchView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.StringRequest;
import com.example.androidexample.R;
import com.example.androidexample.main_five_pages.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

public class SocialActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnMenuItemClickListener(new SearchView.() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchForFriends(query);
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    searchForFriends(newText);
                }
                return true;
            }
        });

    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));


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



    private void searchForFriends(String query) {
    String url = "" + query;


    }
}
