package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class editUserActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, emailEditText, firstNameEditText, lastNameEditText;
    private Button updateButton;
    private String getUrl = "http://coms-3090-058.class.las.iastate.edu:8080/users/username/{username}";
    private String putUrl = "http://coms-3090-058.class.las.iastate.edu:8080/users/{id}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.edit_username_edt);
        passwordEditText = findViewById(R.id.edit_password_edt);
        emailEditText = findViewById(R.id.edit_email_edt);
        firstNameEditText = findViewById(R.id.edit_firstname_edt);
        lastNameEditText = findViewById(R.id.edit_lastname_edt);
        updateButton = findViewById(R.id.update_user_btn);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }

    private void updateUser() {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();

                // 1. GET request to get user ID
                String finalGetUrl = getUrl.replace("{username}", username); // Replace {username} with actual username
                StringRequest getRequest = new StringRequest(Request.Method.GET, finalGetUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String id = jsonResponse.getString("id");

                                    // 2. PUT request to update user information
                                    String finalPutUrl = putUrl.replace("1", id); // Replace {id} with actual ID
                                    JSONObject requestBody = new JSONObject();
                                    try {
                                        jsonResponse.put("username", username);
                                        jsonResponse.put("password", password);
                                        jsonResponse.put("email", email);
                                        jsonResponse.put("firstName", firstName);
                                        jsonResponse.put("lastName", lastName);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, finalPutUrl, jsonResponse,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    Toast.makeText(com.example.androidexample.editUserActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                                                    finish(); // Finish the activity
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(com.example.androidexample.editUserActivity.this, "Error updating user: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(putRequest);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(com.example.androidexample.editUserActivity.this, "Error getting user ID", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(com.example.androidexample.editUserActivity.this, "Error getting user ID: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest);
            }
        }