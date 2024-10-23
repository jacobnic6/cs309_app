package com.coms309.nutrifit.exercises;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    public Equipment(String name) {
        this.name = name;
        exercises = new ArrayList<>();
    }

    public void addExercise(Exercise exercise) {

        this.exercises.add(exercise);
    }
}
