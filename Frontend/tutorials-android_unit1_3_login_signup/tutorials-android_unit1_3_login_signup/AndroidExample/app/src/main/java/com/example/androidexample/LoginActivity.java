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

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

        private EditText usernameEditText;
        private EditText passwordEditText;
        private Button loginButton;
        private Button signupButton;

        private String loginUrl = "http://coms-3090-058.class.las.iastate.edu:8080/users"; // Replace with your API endpoint

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            usernameEditText = findViewById(R.id.login_username_edt);
            passwordEditText = findViewById(R.id.login_password_edt);
            loginButton = findViewById(R.id.login_login_btn);
            signupButton = findViewById(R.id.login_signup_btn);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    checkLoginCredentials(username, password);
                }
            });

            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(com.example.androidexample.LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            });
        }

        private void checkLoginCredentials(String username, String password) {
            String url = loginUrl + "/username/?username=" + username;

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                String passwordDB = jsonResponse.getString("password");

                                if (password.equals(passwordDB)) {
                                    Intent intent = new Intent(com.example.androidexample.LoginActivity.this, UserProfileActivity.class);
                                    intent.putExtra("Username", username);
                                    intent.putExtra("Password", password);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(com.example.androidexample.LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(com.example.androidexample.LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(com.example.androidexample.LoginActivity.this, "Error during login: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            // Add the request to the VolleySingleton queue
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
    }