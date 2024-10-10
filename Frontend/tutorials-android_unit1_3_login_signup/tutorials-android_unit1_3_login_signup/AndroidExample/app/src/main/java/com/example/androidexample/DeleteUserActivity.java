package com.example.androidexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class DeleteUserActivity extends AppCompatActivity {

    private EditText userIdEditText;  // Input field for User ID
    private Button confirmDeleteButton;
    private Button cancelDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        // Initialize UI elements
        userIdEditText = findViewById(R.id.delete_user_id_edt);  // ID input field
        confirmDeleteButton = findViewById(R.id.confirm_delete_btn);
        cancelDeleteButton = findViewById(R.id.cancel_delete_btn);

        // Set a click listener for the Confirm Delete button
        confirmDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the confirmation dialog before deleting
                showDeleteConfirmationDialog();
            }
        });

        // Set a click listener for the Cancel button
        cancelDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the previous screen without deleting
                finish();
            }
        });
    }

    // Method to display a confirmation dialog before proceeding with the delete operation
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteUserActivity.this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.");

        // Yes button to confirm the deletion
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    // Parse the user ID from the input field
                    int userId = Integer.parseInt(userIdEditText.getText().toString().trim());
                    if (userId > 0) {
                        // Call deleteUser method with the entered User ID
                        deleteUser(userId);
                    } else {
                        Toast.makeText(DeleteUserActivity.this, "Please enter a positive User ID", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(DeleteUserActivity.this, "Invalid User ID. Please enter a numeric ID.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // No button to cancel the deletion
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the confirmation dialog
        builder.create().show();
    }

    // Method to delete a user using their ID
    private void deleteUser(final int userId) {
        // Update this URL with your actual endpoint (this example assumes a REST endpoint using the User ID)
        String url = "http://coms-3090-058.class.las.iastate.edu:8080/users" + userId;

        // Create a DELETE request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response
                        Toast.makeText(DeleteUserActivity.this, "User deleted successfully", Toast.LENGTH_LONG).show();
                        // Navigate back to the main screen after deletion
                        Intent intent = new Intent(DeleteUserActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors in the response
                        Toast.makeText(DeleteUserActivity.this, "Failed to delete user: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // Add the request to the Volley queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
