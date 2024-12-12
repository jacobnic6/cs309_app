package com.example.androidexample.services;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

public class ExerciseLibraryService {
    private static final String TAG = "ExerciseLibraryService";
    private final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";
    private final RequestQueue requestQueue;

    public interface ExerciseLibraryCallback {
        void onSuccess(List<String> exercises);
        void onError(String error);
    }

    public ExerciseLibraryService(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void searchExercisesByName(String query, ExerciseLibraryCallback callback) {
        String url = BASE_URL + "/exercise/name/" + query;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        List<String> exercises = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            exercises.add(response.getString(i));
                        }
                        callback.onSuccess(exercises);
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing exercises: " + e.getMessage());
                        callback.onError("Error parsing exercises");
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching exercises: " + error.getMessage());
                    callback.onError("Error fetching exercises");
                }
        );

        requestQueue.add(request);
    }

    public void getExercisesByMuscle(String muscle, ExerciseLibraryCallback callback) {
        String url = BASE_URL + "/exercise/muscle/" + muscle;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        List<String> exercises = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            exercises.add(response.getString(i));
                        }
                        callback.onSuccess(exercises);
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing exercises: " + e.getMessage());
                        callback.onError("Error parsing exercises");
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching exercises: " + error.getMessage());
                    callback.onError("Error fetching exercises");
                }
        );

        requestQueue.add(request);
    }

    public void cacheExercises() {
        // TODO: Implement local caching of exercises for offline use
    }
}