package com.example.androidexample;

public class Exercise {
    private int id;
    private int workoutId;
    private String category;
    private String name;
    private double weightLifted;
    private int repetitions;
    private int setNumber;
    private int restTime;
    private double primaryProgress;
    private double secondaryProgress;
    private String notes;
    private double totalWeight;

    // Constructor for new exercises
    public Exercise(String category, String name, double weightLifted, int repetitions, int setNumber, int restTime) {
        this.category = category;
        this.name = name;
        this.weightLifted = weightLifted;
        this.repetitions = repetitions;
        this.setNumber = setNumber;
        this.restTime = restTime;
        this.totalWeight = weightLifted * repetitions;
    }

    // Constructor for database retrieval
    public Exercise(int id, int workoutId, String category, String name, double weightLifted,
                    int repetitions, int setNumber, int restTime, double primaryProgress,
                    double secondaryProgress, String notes) {
        this.id = id;
        this.workoutId = workoutId;
        this.category = category;
        this.name = name;
        this.weightLifted = weightLifted;
        this.repetitions = repetitions;
        this.setNumber = setNumber;
        this.restTime = restTime;
        this.primaryProgress = primaryProgress;
        this.secondaryProgress = secondaryProgress;
        this.notes = notes;
        this.totalWeight = weightLifted * repetitions;
    }

    // Legacy constructor for backward compatibility
    public Exercise(String category, String name, double weight, int sets, int reps) {
        this.category = category;
        this.name = name;
        this.weightLifted = weight;
        this.setNumber = sets;
        this.repetitions = reps;
        this.restTime = 60; // Default rest time
        this.totalWeight = weight * reps;
    }

    // Getters
    public int getId() { return id; }
    public int getWorkoutId() { return workoutId; }
    public String getCategory() { return category; }
    public String getExerciseName() { return name; }
    public double getWeightLifted() { return weightLifted; }
    public int getRepetitions() { return repetitions; }
    public int getSetNumber() { return setNumber; }
    public int getRestTime() { return restTime; }
    public double getPrimaryProgress() { return primaryProgress; }
    public double getSecondaryProgress() { return secondaryProgress; }
    public String getNotes() { return notes; }
    public double getTotalWeight() { return totalWeight; }

    // Legacy getters for backward compatibility
    public String getName() { return name; }
    public double getWeight() { return weightLifted; }
    public int getSets() { return setNumber; }
    public int getReps() { return repetitions; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setWorkoutId(int workoutId) { this.workoutId = workoutId; }
    public void setCategory(String category) { this.category = category; }
    public void setExerciseName(String name) { this.name = name; }
    public void setWeightLifted(double weightLifted) {
        this.weightLifted = weightLifted;
        updateTotalWeight();
    }
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
        updateTotalWeight();
    }
    public void setSetNumber(int setNumber) { this.setNumber = setNumber; }
    public void setRestTime(int restTime) { this.restTime = restTime; }
    public void setPrimaryProgress(double primaryProgress) { this.primaryProgress = primaryProgress; }
    public void setSecondaryProgress(double secondaryProgress) { this.secondaryProgress = secondaryProgress; }
    public void setNotes(String notes) { this.notes = notes; }

    // Legacy setters for backward compatibility
    public void setName(String name) { this.name = name; }
    public void setWeight(double weight) {
        this.weightLifted = weight;
        updateTotalWeight();
    }
    public void setSets(int sets) { this.setNumber = sets; }
    public void setReps(int reps) {
        this.repetitions = reps;
        updateTotalWeight();
    }

    // Utility methods
    private void updateTotalWeight() {
        this.totalWeight = this.weightLifted * this.repetitions;
    }

    public double calculateVolume() {
        return weightLifted * repetitions;
    }

    public String getDisplayString() {
        return String.format("%s: %d × %.1f lbs (%ds rest)",
                name, repetitions, weightLifted, restTime);
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

    @Override
    public String toString() {
        return String.format("Exercise{id=%d, name='%s', weight=%.1f, reps=%d, set=%d}",
                id, name, weightLifted, repetitions, setNumber);
    }
}