//package com.example.androidexample;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//public class DeleteUserActivity extends AppCompatActivity {
//
//    private Button confirmDeleteButton;
//    private Button cancelDeleteButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_delete_user);
//
//        // Initialize UI elements
//        confirmDeleteButton = findViewById(R.id.confirm_delete_btn);
//        cancelDeleteButton = findViewById(R.id.cancel_delete_btn);
//
//        // Set a click listener for the Confirm Delete button
//        confirmDeleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Show the confirmation dialog before deleting
//                showDeleteConfirmationDialog();
//            }
//        });
//
//        // Set a click listener for the Cancel button
//        cancelDeleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Go back to the settings screen without deleting
//                finish();
//            }
//        });
//    }
//
//    private void showDeleteConfirmationDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteUserActivity.this);
//        builder.setTitle("Confirm Delete");
//        builder.setMessage("Are you sure you want to delete your account?");
//
//        // Yes button
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                deleteUser("testuser");  // Replace "testuser" with the actual username if needed
//            }
//        });
//
//        // No button
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        // Show the dialog
//        builder.create().show();
//    }
//
//    private void deleteUser(final String username) {
//        String url = "http://<your-server-ip>:8080/users/delete/" + username;
//
//        // Create a DELETE request using Volley
//        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(DeleteUserActivity.this, "User deleted successfully", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(DeleteUserActivity.this, SettingsActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(DeleteUserActivity.this, "Failed to delete user: " + error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//
//        // Add the request to the Volley queue
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
//    }
//}

