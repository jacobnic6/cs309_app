package com.example.androidexample.api;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

/**
 * Service class for managing meal-related API operations with the nutrition tracking backend.
 */
public class MealService {

    /** Tag used for logging messages related to MealService operations */
    private static final String TAG = "MealService";

    /** Base URL for the meal service API endpoints */
    private static final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";

    /** Request queue used for making API requests through the Volley library */
    private final RequestQueue requestQueue;

    /**
     * Callback interface for handling meal service API responses.
     */
    public interface MealServiceCallback {
        void onSuccess(JSONObject response);
        void onError(String error);
    }

    /**
     * Constructs a new MealService with the specified Volley request queue.
     */
    public MealService(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    /**
     * Creates an empty meal list for the specified user and date.
     * Sends a POST request to the endpoint: /meals/{userId}/{date}
     *
     * @param userId The user ID for which to create the meal list
     * @param date The date for the meal list, formatted as yyyy-MM-dd
     * @param callback Handle API response
     */
    public void createEmptyMealList(String userId, String date, MealServiceCallback callback) {
        String url = String.format("%s/meals/%s/%s", BASE_URL, userId, date);
        Log.d(TAG, "Creating empty meal list at URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                response -> {
                    Log.d(TAG, "Successfully created empty meal list");
                    Log.d(TAG, "Response: " + response.toString());
                    callback.onSuccess(response);
                },
                error -> {
                    Log.e(TAG, "Error creating empty meal list: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e(TAG, "Error status code: " + error.networkResponse.statusCode);
                        Log.e(TAG, "Error data: " + new String(error.networkResponse.data));
                    }
                    callback.onError("Failed to create empty meal list");
                }
        );

        requestQueue.add(request);
    }

    /**
     * Retrieves the total nutritional information for all meals on a specific date.
     * Sends a GET request to the endpoint: /meals/totals/{date}/?username={userId}
     */
    public void getMealTotals(String date, String userId, MealServiceCallback callback) {
        String url = String.format("%s/meals/totals/%s/?username=%s", BASE_URL, date, userId);
        Log.d(TAG, "Getting meal totals from URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d(TAG, "Successfully fetched meal totals for date: " + date);
                    Log.d(TAG, "Response: " + response.toString());
                    callback.onSuccess(response);
                },
                error -> {
                    Log.e(TAG, "Error fetching meal totals: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e(TAG, "Error status code: " + error.networkResponse.statusCode);
                        Log.e(TAG, "Error data: " + new String(error.networkResponse.data));
                    }
                    callback.onError("Failed to fetch meal totals");
                }
        );

        requestQueue.add(request);
    }

    /**
     * Retrieves all meals for a specific date and user.
     * Sends a GET request to the endpoint: /meals/{date}/{userId}
     */
    public void getMealsByDate(String date, String userId, MealServiceCallback callback) {
        String url = String.format("%s/meals/%s/%s", BASE_URL, userId, date);
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
                    if (error.networkResponse != null) {
                        Log.e(TAG, "Error status code: " + error.networkResponse.statusCode);
                        Log.e(TAG, "Error data: " + new String(error.networkResponse.data));
                    }
                    callback.onError("Failed to fetch meals");
                }
        );

        requestQueue.add(request);
    }

    /**
     * Adds a new meal for the specified date and user.
     * Sends a POST request to the endpoint: /meals/food/{userId}/{date}
     */
    public void addMeal(String date, String userId, JSONObject mealData, MealServiceCallback callback) {
        String url = String.format("%s/meals/food/%s/%s", BASE_URL, userId, date);
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
                    if (error.networkResponse != null) {
                        Log.e(TAG, "Error status code: " + error.networkResponse.statusCode);
                        Log.e(TAG, "Error data: " + new String(error.networkResponse.data));
                    }
                    callback.onError("Failed to add meal");
                }
        );

        requestQueue.add(request);
    }

    /**
     * Updates an existing meal for the user.
     * Sends a PUT request to the endpoint: /meals/2
     */
    public void updateMeal(String date, String userId, String mealType, JSONObject mealData, MealServiceCallback callback) {
        String url = String.format("%s/meals/2", BASE_URL);
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
                    if (error.networkResponse != null) {
                        Log.e(TAG, "Error status code: " + error.networkResponse.statusCode);
                        Log.e(TAG, "Error data: " + new String(error.networkResponse.data));
                    }
                    callback.onError("Failed to update meal");
                }
        );

        requestQueue.add(request);
    }

    /**
     * Deletes a meal for the user.
     * Sends a DELETE request to the endpoint: /meals/meal/6
     */
    public void deleteMeal(String userId, String date, String mealType, MealServiceCallback callback) {
        String url = String.format("%s/meals/meal/6", BASE_URL);
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
                    if (error.networkResponse != null) {
                        Log.e(TAG, "Error status code: " + error.networkResponse.statusCode);
                        Log.e(TAG, "Error data: " + new String(error.networkResponse.data));
                    }
                    callback.onError("Failed to delete meal");
                }
        );

        requestQueue.add(request);
    }
}
