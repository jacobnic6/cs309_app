/**
 * Service class for handling meal-related API operations with the nutrition tracking backend.
 * This class provides methods for creating, reading, updating, and deleting meal information
 * through REST API endpoints.
 *
 * The service uses Volley for network requests and provides asynchronous callbacks
 * for handling API responses.
 *
 * Example usage:
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
 * @author Michael Becker
 * @version 1.0
 * @since 2024-03-20
 */
package com.example.androidexample.api;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

public class MealService {

    /** Tag for logging purposes */
    private static final String TAG = "MealService";

    /** Base URL for the meal service API endpoints */
    private static final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";

    /** Request queue for handling API requests */
    private final RequestQueue requestQueue;

    /**
     * Callback interface for handling meal service API responses.
     * Implementations should handle both successful responses and errors appropriately.
     */
    public interface MealServiceCallback {
        /**
         * Called when the API request is successful.
         *
         * @param response JSONObject containing the API response data
         */
        void onSuccess(JSONObject response);

        /**
         * Called when the API request fails.
         *
         * @param error String description of the error that occurred
         */
        void onError(String error);
    }

    /**
     * Constructs a new MealService with the specified request queue.
     *
     * @param requestQueue Volley RequestQueue used for making API requests
     */
    public MealService(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    /**
     * Retrieves the total nutritional information for all meals on a specific date.
     * Makes a GET request to /meals/totals/{date}/?username={userId}
     *
     * @param date The date for which to retrieve meal totals (format: yyyy-MM-dd)
     * @param userId The unique identifier of the user
     * @param callback Callback to handle the API response
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
     * Makes a GET request to /meals/{date}/{userId}
     *
     * @param date The date for which to retrieve meals (format: yyyy-MM-dd)
     * @param userId The unique identifier of the user
     * @param callback Callback to handle the API response
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
     * Adds a new meal to the specified date for the user.
     * Makes a POST request to /meals/food/{date}/{userId}
     *
     * @param date The date for which to add the meal (format: yyyy-MM-dd)
     * @param userId The unique identifier of the user
     * @param mealData JSONObject containing the meal information including:
     *                 - foodName: name of the food
     *                 - servingSize: size of the serving
     *                 - calories: number of calories
     *                 - protein: grams of protein
     *                 - carbs: grams of carbohydrates
     *                 - fat: grams of fat
     * @param callback Callback to handle the API response
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
     * Updates an existing meal for the specified user.
     * Makes a PUT request to /meals/2
     *
     * @param date The date of the meal to update (format: yyyy-MM-dd)
     * @param userId The unique identifier of the user
     * @param mealType The type of meal (breakfast, lunch, dinner, snacks)
     * @param mealData JSONObject containing the updated meal information
     * @param callback Callback to handle the API response
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
     * Deletes a meal for the specified user.
     * Makes a DELETE request to /meals/meal/6
     *
     * @param date The date of the meal to delete (format: yyyy-MM-dd)
     * @param userId The unique identifier of the user
     * @param mealType The type of meal to delete (breakfast, lunch, dinner, snacks)
     * @param callback Callback to handle the API response
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