package com.example.androidexample;

public class Exercise {
    private int id;
    private int workoutId;
    private String category;
    private String name;
    private double weight;
    private int sets;
    private int reps;
    private String notes;

    // Constructor for new exercises
    public Exercise(String category, String name, double weight, int sets, int reps) {
        this.category = category;
        this.name = name;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
    }

    // Constructor for database retrieval
    public Exercise(int id, int workoutId, String category, String name, double weight, int sets, int reps, String notes) {
        this.id = id;
        this.workoutId = workoutId;
        this.category = category;
        this.name = name;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
        this.notes = notes;
    }

    // Getters
    public int getId() { return id; }
    public int getWorkoutId() { return workoutId; }
    public String getCategory() { return category; }
    public String getName() { return name; }
    public double getWeight() { return weight; }
    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public String getNotes() { return notes; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setWorkoutId(int workoutId) { this.workoutId = workoutId; }
    public void setCategory(String category) { this.category = category; }
    public void setName(String name) { this.name = name; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setSets(int sets) { this.sets = sets; }
    public void setReps(int reps) { this.reps = reps; }
    public void setNotes(String notes) { this.notes = notes; }

    // Utility methods
    public double calculateVolume() {
        return weight * sets * reps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return id == exercise.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}