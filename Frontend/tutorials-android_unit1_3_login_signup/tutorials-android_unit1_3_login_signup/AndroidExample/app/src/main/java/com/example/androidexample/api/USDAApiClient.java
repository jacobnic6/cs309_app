/**
 * Client for interacting with the USDA Food Data Central (FDC) API.
 * This singleton class provides methods to search for food items and their nutritional information
 * using the USDA's FDC database.
 *
 * The client uses Retrofit for HTTP requests and implements a callback pattern
 * for handling asynchronous responses.
 *
 * Example usage:
 * <pre>
 * USDAApiClient client = USDAApiClient.getInstance();
 * client.searchFoods("apple", new USDAApiCallback() {
 *     public void onSuccess(FoodSearchResponse response) {
 *         List<Food> foods = response.getFoods();
 *         // Process food data
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


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
public class USDAApiClient {

    /** Base URL for the USDA Food Data Central API */
    private static final String BASE_URL = "https://api.nal.usda.gov/fdc/v1/";

    /** API key for authentication with the USDA API */
    private static final String API_KEY = "NoJ0axrXD910PtuqgeNC74CGrFtL5rYhN2lbm8Lb";

    /** Singleton instance of the USDAApiClient */
    private static USDAApiClient instance;

    /** Service interface for making API calls */
    private final USDAApiService service;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes the Retrofit service with the base URL and GSON converter.
     */
    private USDAApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(USDAApiService.class);
    }

    /**
     * Gets the singleton instance of the USDAApiClient.
     * Creates a new instance if one doesn't exist.
     *
     * @return The singleton instance of USDAApiClient
     */
    public static synchronized USDAApiClient getInstance() {
        if (instance == null) {
            instance = new USDAApiClient();
        }
        return instance;
    }

    /**
     * Interface defining the API endpoints for the USDA Food Data Central API.
     * Uses Retrofit annotations to define HTTP requests.
     */
    public interface USDAApiService {
        /**
         * Searches for foods in the USDA database.
         *
         * @param apiKey API key for authentication
         * @param query Search query string (e.g., "apple", "chicken breast")
         * @param pageSize Number of results to return per page
         * @return Call object containing the FoodSearchResponse
         */
        @GET("foods/search")
        Call<FoodSearchResponse> searchFoods(
                @Query("api_key") String apiKey,
                @Query("query") String query,
                @Query("pageSize") int pageSize
        );
    }

    /**
     * Searches for food items in the USDA database.
     * This method performs an asynchronous search and returns results through a callback.
     * The search is limited to 25 results per page.
     *
     * @param query The search query string (e.g., "apple", "chicken breast")
     * @param callback Callback to handle the API response or error
     *                 The callback will receive either a FoodSearchResponse with the results
     *                 or an error message if the search fails
     */
    public void searchFoods(String query, USDAApiCallback callback) {
        service.searchFoods(API_KEY, query, 25)
                .enqueue(new Callback<FoodSearchResponse>() {
                    @Override
                    public void onResponse(Call<FoodSearchResponse> call,
                                           retrofit2.Response<FoodSearchResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Error: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<FoodSearchResponse> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    /**
     * Callback interface for handling USDA API responses.
     * Implementations should handle both successful responses and errors appropriately.
     */
    public interface USDAApiCallback {
        /**
         * Called when the API request is successful.
         *
         * @param response FoodSearchResponse containing the search results
         */
        void onSuccess(FoodSearchResponse response);

        /**
         * Called when the API request fails.
         *
         * @param error String description of the error that occurred
         */
        void onError(String error);
    }
}