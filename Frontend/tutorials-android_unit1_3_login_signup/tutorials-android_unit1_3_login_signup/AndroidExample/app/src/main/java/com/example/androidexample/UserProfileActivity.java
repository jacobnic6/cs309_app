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

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {



    /* The Following buttons will be used to switch the intent to the respective page*/
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
}

