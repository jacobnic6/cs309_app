package com.coms309.nutrifit.exercises;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Equipment.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @ManyToMany
    private List<Exercise> exercises;

    /**
     * Instantiates a new Equipment.
     *
     * @param name the name
     */
    public Equipment(String name) {
        this.name = name;
        exercises = new ArrayList<>();
    }

    /**
     * Add exercise.
     *
     * @param exercise the exercise
     */
    public void addExercise(Exercise exercise) {

        this.exercises.add(exercise);
    }
}
