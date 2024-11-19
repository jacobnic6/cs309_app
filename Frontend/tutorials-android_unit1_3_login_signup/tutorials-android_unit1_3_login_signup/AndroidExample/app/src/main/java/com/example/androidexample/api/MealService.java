package com.example.androidexample.api;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

/**
 * Service class for managing meal activity API operations with the nutrition feeding into the backend.
 * This class provides methods for creating, retrieving, updating, and deleting meal information
 * through REST API endpoints. It uses the Volley library for making network requests and provides
 * automatic callbacks for handling API responses.
 *
 * <p>Example usage:</p>
 * <pre>
 * RequestQueue queue = Volley.newRequestQueue(context);
 * MealService mealService = new MealService(queue);
 *
 * mealService.getMealsByDate("2024-03-20", "userId", new MealServiceCallback() {
 *     public void onSuccess(JSONObject response) {
 *         // Handle successful response
 *     }
 *     public void onError(String error) {
 *         // Handle error
 *     }
 * });
 * </pre>
 *
 * <p>This class ensures that API requests and responses are logged for debugging purposes.
 * Errors are logged in detail, including network response status codes and error data when available.</p>
 *
 * @author Michael Becker
 * @version 1.0
 * @since 2024-03-20
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
     * Implementations of this interface should handle both successful responses and errors.
     */
    public interface MealServiceCallback {
        /**
         * Called when the API request completes successfully.
         *
         * @param response JSONObject containing the API response data
         */
        void onSuccess(JSONObject response);

        /**
         * Called when the API request fails.
         *
         * @param error A String description of the error that occurred
         */
        void onError(String error);
    }

    /**
     * Constructs a new MealService with the Volley request queue.
     *
     * @param requestQueue The Volley RequestQueue used for making API requests
     */
    public MealService(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    /**
     * Retrieves the total nutritional information for all meals on a specific date.
     * Sends a GET request to the endpoint: /meals/totals/{date}/?username={userId}
     *
     * @param date The date to retrieve meal totals, formatted as yyyy-MM-dd
     * @param userId User ID
     * @param callback Handle API response
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
     *
     * @param date The date to retrieve meals, formatted as yyyy-MM-dd
     * @param userId User ID for the user
     * @param callback Handle API response
     */
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
     * Adds a new meal for the date and user.
     * Sends a POST request to the endpoint: /meals/food/{date}/{userId}
     *
     * @param date The dat to add the meal, formatted as yyyy-MM-dd
     * @param userId User iD for the user
     * @param mealData A JSONObject containing meal details, such as:
     *                 - foodName: The name of the food item
     *                 - servingSize: The size of the serving
     *                 - calories: The number of calories
     *                 - protein: The amount of protein in grams
     *                 - carbs: The amount of carbohydrates in grams
     *                 - fat: The amount of fat in grams
     * @param callback Handle API response
     */
    public void addMeal(String date, String userId, JSONObject mealData, MealServiceCallback callback) {
        String url = String.format("%s/meals/food/%s/%s", BASE_URL, date, userId);
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

    /**
     * Updates an existing meal for the user.
     * Sends a PUT request to the endpoint: /meals/2
     *
     * @param date The date of the meal to update, formatted as yyyy-MM-dd
     * @param userId User ID
     * @param mealType The type of meal (e.g., breakfast, lunch, dinner, snacks)
     * @param mealData A JSONObject containing updated meal details
     * @param callback Handle API response
     */
    public void updateMeal(String date, String userId, String mealType, JSONObject mealData, MealServiceCallback callback) {
        String url = String.format("%s/meals/2");
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

    /**
     * Deletes a meal for the user.
     * Sends a DELETE request to the endpoint: /meals/meal/6
     *
     * @param date The date of the meal to delete, formatted as yyyy-MM-dd
     * @param userId User ID
     * @param mealType The type of meal to delete ( breakfast, lunch, dinner, snacks)
     * @param callback Handle API response
     */
    public void deleteMeal(String date, String userId, String mealType, MealServiceCallback callback) {
        String url = String.format("%s/meals/meal/6", BASE_URL, date, userId);
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
