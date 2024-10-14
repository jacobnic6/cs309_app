package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private Button loginButton;
    private Button signupButton;

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

            // Check for empty
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

            // signup request
            signupUser(firstName, lastName, email, username, password);
        });
    }

    // validate password contains at least one capital letter and one special character
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).+$";
        return password.matches(passwordPattern);
    }

    private void signupUser(final String firstName, final String lastName, final String email, final String username, final String password) {
        String url = "http://coms-3090-058.class.las.iastate.edu:8080/users";  // Replace with correct URL
        RequestQueue queue = Volley.newRequestQueue(this);

        //Request body
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
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // sign up
                        Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignupActivity.this, DeleteUserActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  failure
                        Toast.makeText(SignupActivity.this, "Signup failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
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

        // add to queue
        queue.add(stringRequest);
    }
}
