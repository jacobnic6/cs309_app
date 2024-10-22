package com.coms309.nutrifit.exercises;

import jakarta.persistence.Entity;
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
