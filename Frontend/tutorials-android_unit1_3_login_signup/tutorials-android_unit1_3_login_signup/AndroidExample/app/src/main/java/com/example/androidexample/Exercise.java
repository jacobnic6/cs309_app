package com.example.androidexample;

public class Exercise {
    private String category;
    private String name;
    private double weight;
    private int sets;
    private int reps;

    public Exercise(String category, String name, double weight, int sets, int reps) {
        this.category = category;
        this.name = name;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
    }

    public String getCategory() { return category; }
    public String getName() { return name; }
    public double getWeight() { return weight; }
    public int getSets() { return sets; }
    public int getReps() { return reps; }
}
