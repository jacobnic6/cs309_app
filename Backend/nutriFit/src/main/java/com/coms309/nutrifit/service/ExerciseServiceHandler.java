package com.coms309.nutrifit.service;

import com.coms309.nutrifit.exercises.Category;
import com.coms309.nutrifit.exercises.Equipment;
import com.coms309.nutrifit.exercises.Exercise;
import com.coms309.nutrifit.exercises.Muscle;
import com.coms309.nutrifit.repo.CategoryRepository;
import com.coms309.nutrifit.repo.EquipmentRepository;
import com.coms309.nutrifit.repo.ExerciseRepository;
import com.coms309.nutrifit.repo.MuscleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseServiceHandler {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MuscleRepository muscleRepository;

    public void importExercises(String path) throws IOException {
        File file = new File(path);
     List<Exercise> exerciseList = objectMapper.readValue(file, List.class );
        exerciseRepository.saveAll(exerciseList);
    }

    public void addExercise(Exercise exercise) {
        if(exercise.getCategory() == null) {
            exercise.setCategory(new Category("strength"));
        }
        String name = exercise.getCategory().getName();
        Category repoC = categoryRepository.getByName(name);
        exercise.setCategory(repoC);

        List<Equipment> equipment = exercise.getEquipment();
        List<Equipment> newEquipment = new ArrayList<>();
        for (Equipment equip : equipment) {
           Equipment e = equipmentRepository.getEquipmentByName(equip.getName());
            newEquipment.add(e);
        }
        exercise.setEquipment(newEquipment);

List<Muscle> primary = exercise.getPrimaryMuscles();
        List<Muscle> newPrim = new ArrayList<>();
        for (Muscle muscle : primary) {
   newPrim.add(muscleRepository.getByName(muscle.getName()));
}
        exercise.setPrimaryMuscles(newPrim);
newPrim = new ArrayList<>();
List<Muscle> secondary = exercise.getSecondaryMuscles();
for (Muscle muscle : secondary) {
    newPrim.add(muscleRepository.getByName(muscle.getName()));
}
exercise.setSecondaryMuscles(newPrim);

    }
}
