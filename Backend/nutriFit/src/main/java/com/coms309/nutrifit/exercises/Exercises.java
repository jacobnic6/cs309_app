package com.coms309.nutrifit.exercises;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The type Exercises.
 */
@Getter
@Setter
@AllArgsConstructor
public class Exercises {

    private int id;


    /**
     * The Categories.
     */
    List<Category> categories;

    /**
     * The Equipment.
     */
    List<Equipment> equipment;

    /**
     * The Muscles.
     */
    List<Muscle> muscles;

    /**
     * The Muscle groups.
     */
    Map<String, List<Muscle>> muscleGroups;

    /**
     * The Exercises.
     */
    List<Exercise> exercises;

    /**
     * Instantiates a new Exercises.
     */
    public Exercises(){
        categories = new ArrayList<>();
        equipment = new ArrayList<>();
        muscles = new ArrayList<>();
        muscleGroups = new HashMap<>();
        exercises = new ArrayList<>();
    }

}
