package com.example.androidexample;

public class SessionManager {
    private String username;
    private int userId;
    private static SessionManager instance;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void saveUserSession(String username, int userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public void clearSession() {
        username = null;
        userId = -1;
    }

    public boolean isLoggedIn() {
        return username != null;
    }
}