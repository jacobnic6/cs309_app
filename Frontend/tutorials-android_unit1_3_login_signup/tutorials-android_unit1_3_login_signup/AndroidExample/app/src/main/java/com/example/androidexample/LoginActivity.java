package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.androidexample.main_five_pages.UserProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable

    private String loginUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);            // link to Login activity XML

        /* initialize UI elements */
        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);    // link to login button in the Login activity XML
        signupButton = findViewById(R.id.login_signup_btn);  // link to signup button in the Login activity XML

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Need to implement here the volley logic and checkers for the requirements to login
                checkLoginCredentials(username, password);
            }
        });


        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);  // go to SignupActivity
            }
        });
    }

        private void checkLoginCredentials(String username, String password){
            String url = loginUrl + "?username=" + username + "&password=" + password;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonRespone = new JSONObject(response);
                    boolean success = jsonRespone.getBoolean("success");

                    if (success) {
                        Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "Error during login: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    });
                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
            }
            }
