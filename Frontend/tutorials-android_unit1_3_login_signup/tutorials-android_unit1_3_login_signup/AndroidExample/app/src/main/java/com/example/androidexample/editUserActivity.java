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
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class editUserActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, emailEditText, firstNameEditText, lastNameEditText;
    private Button updateButton;
    private String url = "http://coms-3090-058.class.las.iastate.edu:8080/users";

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

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("password", password);
            requestBody.put("email", email);
            requestBody.put("firstName", firstName);
            requestBody.put("lastName", lastName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(editUserActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                        // You might want to finish this activity or navigate back to SettingsActivity
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(editUserActivity.this, "Error updating user: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}