package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignupActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private Button loginButton;
    private Button signupButton;
    private final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        firstNameEditText = findViewById(R.id.signup_firstname_edt);
        lastNameEditText = findViewById(R.id.signup_lastname_edt);
        emailEditText = findViewById(R.id.signup_email_edt);
        usernameEditText = findViewById(R.id.signup_username_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        confirmEditText = findViewById(R.id.signup_confirm_edt);
        loginButton = findViewById(R.id.signup_login_btn);
        signupButton = findViewById(R.id.signup_signup_btn);

        // Navigate to LoginActivity on login button click
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Signup button click listener
        signupButton.setOnClickListener(v -> {
            // Retrieve input values
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirm = confirmEditText.getText().toString().trim();

            // Check for empty fields
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
                return;
            }

            // Check if passwords match
            if (!password.equals(confirm)) {
                Toast.makeText(SignupActivity.this, "Passwords don't match", Toast.LENGTH_LONG).show();
                return;
            }

            // Check if password has at least one capital letter and one special character
            if (!isValidPassword(password)) {
                Toast.makeText(SignupActivity.this, "Password must contain at least one capital letter and one special character", Toast.LENGTH_LONG).show();
                return;
            }

            // Check if the username is available before proceeding with signup
            checkUsernameAvailability(username, isAvailable -> {
                if (isAvailable) {
                    signupUser(firstName, lastName, email, username, password);
                }
            });
        });
    }

    // Validate password contains at least one capital letter and one special character
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).+$";
        return password.matches(passwordPattern);
    }

    // Check if the username is available
    private void checkUsernameAvailability(String username, ValidationCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(this);
        AtomicBoolean usernameAvailable = new AtomicBoolean(true);

        try {
            // Encode the username to handle special characters
            String usernameCheckUrl = BASE_URL + "/users/username/" + URLEncoder.encode(username, "UTF-8");

            // Check username availability
            StringRequest usernameRequest = new StringRequest(Request.Method.GET, usernameCheckUrl,
                    response -> {
                        try {
                            Log.d("Username Check Response", response);  // Log the response for debugging

                            // If the response is empty, the username is available
                            if (response == null || response.isEmpty()) {
                                usernameAvailable.set(true);  // No user found, username is available
                                Toast.makeText(SignupActivity.this, "Username is available", Toast.LENGTH_LONG).show();
                            } else {
                                // If we get a response, check if the username is part of it
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("username")) {
                                    usernameAvailable.set(false);  // Username is taken
                                    Toast.makeText(SignupActivity.this, "Username is already taken", Toast.LENGTH_LONG).show();
                                } else {
                                    usernameAvailable.set(true);  // Username is available
                                }
                            }

                            // Invoke the callback with the result
                            callback.onValidationResult(usernameAvailable.get());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignupActivity.this, "Error parsing response", Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> {
                        Toast.makeText(SignupActivity.this, "Error checking username: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    });

            // Add request to the queue
            queue.add(usernameRequest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(SignupActivity.this, "Encoding error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Callback interface for validation results
    interface ValidationCallback {
        void onValidationResult(boolean isAvailable);
    }

    // Signup user if username is available
    private void signupUser(final String firstName, final String lastName, final String email, final String username, final String password) {
        String url = BASE_URL + "/users";
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("email", email);
            jsonBody.put("firstName", firstName);
            jsonBody.put("lastName", lastName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Signup success
                    Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    // Signup failure
                    Toast.makeText(SignupActivity.this, "Signup failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            public byte[] getBody() {
                return jsonBody.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        // Add to queue
        queue.add(stringRequest);
    }
}

