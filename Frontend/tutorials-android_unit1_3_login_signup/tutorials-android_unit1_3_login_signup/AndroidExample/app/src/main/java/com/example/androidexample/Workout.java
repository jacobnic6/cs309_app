package com.example.androidexample;

public class Workout {
    private int id;
    private String name;
    private String date;
    private int exerciseCount;

    public Workout(int id, String name, String date, int exerciseCount) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.exerciseCount = exerciseCount;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public int getExerciseCount() { return exerciseCount; }
}