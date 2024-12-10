package com.example.androidexample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MuscleProgress {
    private int id;
    private String muscleName;
    private double measurement;
    private String date;
    private String notes;

    public MuscleProgress(int id, String muscleName, double measurement, String date, String notes) {
        this.id = id;
        this.muscleName = muscleName;
        this.measurement = measurement;
        this.date = date;
        this.notes = notes;
    }

    // Getters
    public int getId() { return id; }
    public String getMuscleName() { return muscleName; }
    public double getMeasurement() { return measurement; }
    public String getDate() { return date; }
    public String getNotes() { return notes; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setMuscleName(String muscleName) { this.muscleName = muscleName; }
    public void setMeasurement(double measurement) { this.measurement = measurement; }
    public void setDate(String date) { this.date = date; }
    public void setNotes(String notes) { this.notes = notes; }

    // Utility methods
    public Date getDateObject() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public boolean isFromToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        return today.equals(date);
    }

    public double calculateProgress(MuscleProgress previous) {
        if (previous == null) return 0;
        return ((measurement - previous.measurement) / previous.measurement) * 100;
    }
}