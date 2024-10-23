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


@Getter
@Setter
@AllArgsConstructor
public class Exercises {

    private int id;


    List<Category> categories;

    List<Equipment> equipment;

    List<Muscle> muscles;

    Map<String, List<Muscle>> muscleGroups;

    List<Exercise> exercises;

    public Exercises(){
        categories = new ArrayList<>();
        equipment = new ArrayList<>();
        muscles = new ArrayList<>();
        muscleGroups = new HashMap<>();
        exercises = new ArrayList<>();
    }

}
