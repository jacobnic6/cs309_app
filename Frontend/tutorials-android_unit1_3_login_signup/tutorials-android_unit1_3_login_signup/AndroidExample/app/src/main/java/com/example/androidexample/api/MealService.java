package com.example.androidexample.api;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

public class MealService {
    private static final String TAG = "MealService";
    private static final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";
    private final RequestQueue requestQueue;

    public interface MealServiceCallback {
        void onSuccess(JSONObject response);
        void onError(String error);
    }

    public MealService(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    // Get meals for a specific date
    public void getMealsByDate(String date, String userId, MealServiceCallback callback) {
        String url = String.format("%s/meals/%s/%s", BASE_URL, date, userId);
        Log.d(TAG, "Getting meals from URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d(TAG, "Successfully fetched meals for date: " + date);
                    Log.d(TAG, "Response: " + response.toString());
                    callback.onSuccess(response);
                },
                error -> {
                    Log.e(TAG, "Error fetching meals: " + error.toString());
                    callback.onError("Failed to fetch meals");
                }
        );

        requestQueue.add(request);
    }

    // Add a new meal
    public void addMeal(String date, String userId, JSONObject mealData, MealServiceCallback callback) {
        String url = String.format("%s/meals/add/%s/%s", BASE_URL, date, userId);
        Log.d(TAG, "Adding meal at URL: " + url);
        Log.d(TAG, "Meal data: " + mealData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                mealData,
                response -> {
                    Log.d(TAG, "Successfully added meal");
                    Log.d(TAG, "Response: " + response.toString());
                    callback.onSuccess(response);
                },
                error -> {
                    Log.e(TAG, "Error adding meal: " + error.toString());
                    callback.onError("Failed to add meal");
                }
        );

        requestQueue.add(request);
    }

    // Update an existing meal
    public void updateMeal(String date, String userId, String mealType, JSONObject mealData, MealServiceCallback callback) {
        String url = String.format("%s/meals/%s/%s", BASE_URL, date, userId);
        Log.d(TAG, "Updating meal at URL: " + url);
        Log.d(TAG, "Meal data: " + mealData.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                mealData,
                response -> {
                    Log.d(TAG, "Successfully updated meal");
                    Log.d(TAG, "Response: " + response.toString());
                    callback.onSuccess(response);
                },
                error -> {
                    Log.e(TAG, "Error updating meal: " + error.toString());
                    callback.onError("Failed to update meal");
                }
        );

        requestQueue.add(request);
    }

    // Delete a meal
    public void deleteMeal(String date, String userId, String mealType, MealServiceCallback callback) {
        String url = String.format("%s/meals/%s/%s", BASE_URL, date, userId);
        Log.d(TAG, "Deleting meal at URL: " + url);

        JSONObject deleteData = new JSONObject();
        try {
            deleteData.put("mealType", mealType);
        } catch (Exception e) {
            Log.e(TAG, "Error creating delete request data", e);
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                deleteData,
                response -> {
                    Log.d(TAG, "Successfully deleted meal");
                    Log.d(TAG, "Response: " + response.toString());
                    callback.onSuccess(response);
                },
                error -> {
                    Log.e(TAG, "Error deleting meal: " + error.toString());
                    callback.onError("Failed to delete meal");
                }
        );

        requestQueue.add(request);
    }
}