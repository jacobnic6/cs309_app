package com.example.androidexample.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class USDAApiClient {
    private static final String BASE_URL = "https://api.nal.usda.gov/fdc/v1/";
    private static final String API_KEY = "NoJ0axrXD910PtuqgeNC74CGrFtL5rYhN2lbm8Lb";
    private static USDAApiClient instance;
    private final USDAApiService service;

    private USDAApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(USDAApiService.class);
    }

    public static synchronized USDAApiClient getInstance() {
        if (instance == null) {
            instance = new USDAApiClient();
        }
        return instance;
    }

    public interface USDAApiService {
        @GET("foods/search")
        Call<FoodSearchResponse> searchFoods(
                @Query("api_key") String apiKey,
                @Query("query") String query,
                @Query("pageSize") int pageSize
        );
    }

    public void searchFoods(String query, USDAApiCallback callback) {
        service.searchFoods(API_KEY, query, 25)
                .enqueue(new Callback<FoodSearchResponse>() {
                    @Override
                    public void onResponse(Call<FoodSearchResponse> call, retrofit2.Response<FoodSearchResponse> response) {
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

    public interface USDAApiCallback {
        void onSuccess(FoodSearchResponse response);
        void onError(String error);
    }
}
