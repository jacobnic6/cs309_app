package com.example.androidexample.main_five_pages;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.DeleteUserActivity;
import com.example.androidexample.R;
import com.example.androidexample.SessionManager;
import com.example.androidexample.api.SettingsService;
import com.example.androidexample.editUserActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    private SettingsService settingsService;
    private String username;

    // UI Elements
    private SwitchMaterial profileVisibilitySwitch;
    private SwitchMaterial biometricVisibilitySwitch;
    private SwitchMaterial messageNotificationsSwitch;
    private SwitchMaterial friendRequestNotificationsSwitch;
    private SwitchMaterial workoutRemindersSwitch;
    private RadioGroup measurementUnitsGroup;
    private Button editUserButton;
    private Button deleteUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        username = getIntent().getStringExtra("Username");
        if (username == null || username.isEmpty()) {
            username = SessionManager.getInstance().getUsername();
        }

        settingsService = new SettingsService(Volley.newRequestQueue(this));

        initializeViews();
        setupClickListeners();
        loadUserSettings();
        setupBottomNavigation();
    }

    private void initializeViews() {
        profileVisibilitySwitch = findViewById(R.id.profile_visibility_switch);
        biometricVisibilitySwitch = findViewById(R.id.biometric_visibility_switch);
        messageNotificationsSwitch = findViewById(R.id.message_notifications_switch);
        friendRequestNotificationsSwitch = findViewById(R.id.friend_request_notifications_switch);
        workoutRemindersSwitch = findViewById(R.id.workout_reminders_switch);
        measurementUnitsGroup = findViewById(R.id.measurement_units_group);
        editUserButton = findViewById(R.id.edit_user_btn);
        deleteUserButton = findViewById(R.id.delete_user_btn);
    }

    private void setupClickListeners() {
        editUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, editUserActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        deleteUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeleteUserActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        profileVisibilitySwitch.setOnCheckedChangeListener((button, isChecked) -> saveSettings());
        biometricVisibilitySwitch.setOnCheckedChangeListener((button, isChecked) -> saveSettings());
        messageNotificationsSwitch.setOnCheckedChangeListener((button, isChecked) -> saveSettings());
        friendRequestNotificationsSwitch.setOnCheckedChangeListener((button, isChecked) -> saveSettings());
        workoutRemindersSwitch.setOnCheckedChangeListener((button, isChecked) -> saveSettings());
        measurementUnitsGroup.setOnCheckedChangeListener((group, checkedId) -> saveSettings());
    }

    private void loadUserSettings() {
        settingsService.getUserSettings(username, new SettingsService.SettingsCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    profileVisibilitySwitch.setChecked(response.getString("profile_visibility").equals("PUBLIC"));
                    biometricVisibilitySwitch.setChecked(response.getString("biometric_visibility").equals("PUBLIC"));
                    messageNotificationsSwitch.setChecked(response.getBoolean("message_notifications"));
                    friendRequestNotificationsSwitch.setChecked(response.getBoolean("friend_request_notifications"));
                    workoutRemindersSwitch.setChecked(response.getBoolean("workout_reminders_enabled"));

                    boolean isImperial = response.getString("measurement_units").equals("IMPERIAL");
                    measurementUnitsGroup.check(isImperial ? R.id.imperial_radio : R.id.metric_radio);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing settings", e);
                    Toast.makeText(SettingsActivity.this, "Error loading settings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading settings: " + error);
                Toast.makeText(SettingsActivity.this, "Error loading settings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSettings() {
        try {
            JSONObject settings = new JSONObject();
            settings.put("profile_visibility", profileVisibilitySwitch.isChecked() ? "PUBLIC" : "PRIVATE");
            settings.put("biometric_visibility", biometricVisibilitySwitch.isChecked() ? "PUBLIC" : "PRIVATE");
            settings.put("message_notifications", messageNotificationsSwitch.isChecked());
            settings.put("friend_request_notifications", friendRequestNotificationsSwitch.isChecked());
            settings.put("workout_reminders_enabled", workoutRemindersSwitch.isChecked());
            settings.put("measurement_units",
                    measurementUnitsGroup.getCheckedRadioButtonId() == R.id.imperial_radio ? "IMPERIAL" : "METRIC");

            settingsService.updateUserSettings(username, settings, new SettingsService.SettingsCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Toast.makeText(SettingsActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Error saving settings: " + error);
                    Toast.makeText(SettingsActivity.this, "Error saving settings", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, "Error creating settings JSON", e);
            Toast.makeText(this, "Error saving settings", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            if (item.getItemId() == R.id.social) {
                intent = new Intent(this, SocialActivity.class);
            } else if (item.getItemId() == R.id.workouts) {
                intent = new Intent(this, WorkoutActivity.class);
            } else if (item.getItemId() == R.id.profile) {
                intent = new Intent(this, UserProfileActivity.class);
            } else if (item.getItemId() == R.id.nutrition) {
                intent = new Intent(this, NutritionActivity.class);
            } else if (item.getItemId() == R.id.settings) {
                return true;
            } else {
                return false;
            }
            intent.putExtra("Username", username);
            startActivity(intent);
            return true;
        });
    }
}