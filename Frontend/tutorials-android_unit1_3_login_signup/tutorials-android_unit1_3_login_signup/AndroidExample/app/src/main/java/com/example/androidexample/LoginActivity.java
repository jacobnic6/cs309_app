package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidexample.UserProfileActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
/*
public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable
    private boolean loginSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);            // link to Login activity XML

        // initialize UI elements
        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);    // link to login button in the Login activity XML
        signupButton = findViewById(R.id.login_signup_btn);  // link to signup button in the Login activity XML

        // click listener on login button pressed
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // grab strings from user inputs
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                String url = "http://coms-3090-058.class.las.iastate.edu:8080/users?username=" + username + "&password=" + password;

                RequestQueue queue = Volley.newRequestQueue(this);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean loginSuccessful = jsonObject.getBoolean("success"); // Assuming your API returns a "success" flag

                if (loginSuccessful) {
                    // when login button is pressed, use intent to switch to Login Activity
                    Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                    intent.putExtra("USERNAME", username);  // key-value to pass to the MainActivity
                    intent.putExtra("PASSWORD", password);  // key-value to pass to the MainActivity
                    startActivity(intent);  // go to MainActivity with the key-value data
                }
                else {
                    // Handle login failure
                    // You can show an error message to the user
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                                    })
            }
        });

        // click listener on signup button pressed
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //when signup button is pressed, use intent to switch to Signup Activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);  // go to SignupActivity
            }
        });
    }
}

*/