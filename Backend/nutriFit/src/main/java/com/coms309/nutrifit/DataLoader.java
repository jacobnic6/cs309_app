package com.coms309.nutrifit;

import com.coms309.nutrifit.exercises.*;
import com.coms309.nutrifit.repo.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataLoader {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private MuscleRepository muscleRepository;

    @Autowired
    private MuscleGroupRepository muscleGroupRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
   private ObjectMapper mapper;


    public DataLoader(ObjectMapper mapper, ExerciseRepository exerciseRepository) {
        this.mapper = mapper;
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional
    public void loadData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(Paths.get("src/main/resources/exercises.json").toFile(), Map.class);

        // Load categories
        List<String> categories = (List<String>) data.get("categories");
        categories.forEach(name -> categoryRepository.save(new Category(name)));

        // Load equipment
        List<String> equipmentList = (List<String>) data.get("equipment");
        equipmentList.forEach(name -> equipmentRepository.save(new Equipment(name)));

        // Load muscles
        List<String> muscles = (List<String>) data.get("muscles");
        muscles.forEach(name -> muscleRepository.save(new Muscle(name)));

        // Load muscle groups
        Map<String, List<String>> muscleGroups = (Map<String, List<String>>) data.get("muscle_groups");
        muscleGroups.forEach((groupName, musclesInGroup) -> {
            musclesInGroup.forEach(muscleName -> {
                Muscle muscle = muscleRepository.findByName(muscleName);
                MuscleGroup group = new MuscleGroup();
                group.setGroupName(groupName);
                group.setMuscle(muscle);
                muscleGroupRepository.save(group);
            });
        });

        // Load exercises
        List<Map<String, Object>> exercises = (List<Map<String, Object>>) data.get("exercises");
        for (Map<String, Object> exerciseData : exercises) {
            Exercise exercise = new Exercise();
            exercise.setName((String) exerciseData.get("name"));
            exercise.setDescription((String) exerciseData.get("description"));
            exercise.setCategory(categoryRepository.findByName((String) exerciseData.get("category")));
            exercise.setVideoUrl((String) exerciseData.get("video"));

            // Set equipment
            List<String> equipmentNames = (List<String>) exerciseData.get("equipment");
            List<Equipment> equipment = equipmentNames.stream()
                    .map(name -> equipmentRepository.findByName(name))
                    .toList();
            exercise.setEquipment(equipment);

            // Set primary and secondary muscles
            List<String> primaryMuscles = (List<String>) exerciseData.get("primary_muscles");
            List<String> secondaryMuscles = (List<String>) exerciseData.get("secondary_muscles");

            exercise.setPrimaryMuscles(primaryMuscles.stream()
                    .map(name -> muscleRepository.findByName(name))
                    .toList());
            exercise.setSecondaryMuscles(secondaryMuscles.stream()
                    .map(name -> muscleRepository.findByName(name))
                    .toList());

            // Set instructions
            exercise.setInstructions((List<String>) exerciseData.get("instructions"));

            exerciseRepository.save(exercise);
        }
    }
}