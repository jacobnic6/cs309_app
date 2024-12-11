package com.example.androidexample.api;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

public class SettingsService {
    private static final String TAG = "SettingsService";
    private static final String BASE_URL = "http://coms-3090-058.class.las.iastate.edu:8080";
    private final RequestQueue requestQueue;

    public interface SettingsCallback {
        void onSuccess(JSONObject response);
        void onError(String error);
    }

    public SettingsService(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void getAllSettings(SettingsCallback callback) {
        String url = BASE_URL + "/settings";
        makeRequest(Request.Method.GET, url, null, callback);
    }

    public void getUserSettings(String username, SettingsCallback callback) {
        String url = String.format("%s/settings/username/%s", BASE_URL, username);
        makeRequest(Request.Method.GET, url, null, callback);
    }

    public void createSettings(JSONObject settingsData, SettingsCallback callback) {
        String url = BASE_URL + "/settings";
        makeRequest(Request.Method.POST, url, settingsData, callback);
    }

    public void updateUserSettings(String username, JSONObject settingsData, SettingsCallback callback) {
        String url = String.format("%s/users/%s/settings", BASE_URL, username);
        makeRequest(Request.Method.PUT, url, settingsData, callback);
    }

    public void getSettingsById(int id, SettingsCallback callback) {
        String url = String.format("%s/settings/%d", BASE_URL, id);
        makeRequest(Request.Method.GET, url, null, callback);
    }

    public void updateSettings(int id, JSONObject settingsData, SettingsCallback callback) {
        String url = String.format("%s/settings/%d", BASE_URL, id);
        makeRequest(Request.Method.PUT, url, settingsData, callback);
    }

    public void deleteSettings(int id, SettingsCallback callback) {
        String url = String.format("%s/settings/%d", BASE_URL, id);
        makeRequest(Request.Method.DELETE, url, null, callback);
    }

    private void makeRequest(int method, String url, JSONObject data, SettingsCallback callback) {
        Log.d(TAG, String.format("Making %s request to: %s",
                method == Request.Method.GET ? "GET" :
                        method == Request.Method.POST ? "POST" :
                                method == Request.Method.PUT ? "PUT" : "DELETE", url));

        if (data != null) {
            Log.d(TAG, "With data: " + data.toString());
        }

        JsonObjectRequest request = new JsonObjectRequest(
                method,
                url,
                data,
                response -> {
                    Log.d(TAG, "Success response: " + response.toString());
                    callback.onSuccess(response);
                },
                error -> {
                    Log.e(TAG, "Error: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e(TAG, "Status code: " + error.networkResponse.statusCode);
                        Log.e(TAG, "Error data: " + new String(error.networkResponse.data));
                    }
                    callback.onError(error.getMessage());
                }
        );

        requestQueue.add(request);
    }
}