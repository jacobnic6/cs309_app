package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText passwordEditText;   // define EditText variable for password input
    private Button submitButton;         // define Button variable
    private TextView helloTextView;      // define TextView variable for displaying "Hello, World!"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // link to Main activity XML

        // Initialize UI elements
        passwordEditText = findViewById(R.id.passwordEditText);  // Link to password EditText in XML
        submitButton = findViewById(R.id.submitButton);          // Link to submit Button in XML
        helloTextView = findViewById(R.id.helloTextView);        // Link to hello TextView in XML
        helloTextView.setVisibility(View.GONE);                  // Hide the "Hello, World!" text initially

        // Set up button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input from the password field
                String inputPassword = passwordEditText.getText().toString();

                // Check if the password is correct
                if (inputPassword.equals("coms309")) {

                    helloTextView.setVisibility(View.VISIBLE);
                } else {

                    Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
