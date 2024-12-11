package com.example.androidexample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Workout {
    private int id;
    private String name;
    private String date;
    private int exerciseCount;
    private double totalWeight;
    private boolean isSynced;
    private List<Exercise> activities;

    // Constructor for new workouts
    public Workout(int id, String name, String date, int exerciseCount) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.exerciseCount = exerciseCount;
        this.totalWeight = 0.0;
        this.isSynced = false;
    }

    // Constructor with total weight
    public Workout(int id, String name, String date, int exerciseCount, double totalWeight) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.exerciseCount = exerciseCount;
        this.totalWeight = totalWeight;
        this.isSynced = false;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public int getExerciseCount() { return exerciseCount; }
    public double getTotalWeight() { return totalWeight; }
    public boolean isSynced() { return isSynced; }
    public List<Exercise> getActivities() { return activities; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDate(String date) { this.date = date; }
    public void setExerciseCount(int exerciseCount) { this.exerciseCount = exerciseCount; }
    public void setTotalWeight(double totalWeight) { this.totalWeight = totalWeight; }
    public void setSynced(boolean synced) { isSynced = synced; }
    public void setActivities(List<Exercise> activities) {
        this.activities = activities;
        updateTotalWeight();
    }

    // Utility methods
    private void updateTotalWeight() {
        if (activities != null) {
            totalWeight = 0;
            for (Exercise exercise : activities) {
                totalWeight += exercise.getWeightLifted() * exercise.getRepetitions();
            }
        }
    }

    public void addActivity(Exercise exercise) {
        if (activities != null) {
            activities.add(exercise);
            totalWeight += exercise.getWeightLifted() * exercise.getRepetitions();
            exerciseCount = activities.size();
        }
    }

    public void removeActivity(Exercise exercise) {
        if (activities != null && activities.remove(exercise)) {
            totalWeight -= exercise.getWeightLifted() * exercise.getRepetitions();
            exerciseCount = activities.size();
        }
    }

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

    public String getFormattedTotalWeight() {
        return String.format(Locale.getDefault(), "%.1f lbs", totalWeight);
    }

    public String getDisplayTitle() {
        if (name != null && !name.isEmpty()) {
            return name;
        }
        SimpleDateFormat displayFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        try {
            Date workoutDate = getDateObject();
            return workoutDate != null ? displayFormat.format(workoutDate) : "Workout";
        } catch (Exception e) {
            return "Workout";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workout workout = (Workout) o;
        return id == workout.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "Workout{id=%d, name='%s', date='%s', exercises=%d, totalWeight=%.1f}",
                id, name, date, exerciseCount, totalWeight);
    }

    // Static factory method for creating empty workout
    public static Workout createEmpty(String date) {
        return new Workout(-1, "", date, 0, 0.0);
    }
}