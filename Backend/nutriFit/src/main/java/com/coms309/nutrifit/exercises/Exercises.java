package com.coms309.nutrifit.exercises;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exercises {

    List<Category> categories;

    List<Equipment> equipment;

    List<Muscle> muscles;

    Map<String, List<String>> muscleGroups;

    List<Exercise> exercises;
}
