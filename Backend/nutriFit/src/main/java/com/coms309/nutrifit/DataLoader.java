package com.coms309.nutrifit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.coms309.nutrifit.exercises.Exercise;
import com.coms309.nutrifit.repo.ExerciseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader  implements CommandLineRunner{

    @Autowired
    private final ObjectMapper mapper;
    @Autowired
    private final ExerciseRepository exerciseRepository;

    public DataLoader(ObjectMapper mapper, ExerciseRepository exerciseRepository) {
        this.mapper = mapper;
        this.exerciseRepository = exerciseRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        List<Exercise> exercises = new ArrayList<>();

        JsonNode   node;

        try(InputStream inputStream = getClass().getResourceAsStream("/exercises.json")) {

          exercises = mapper.readValue(inputStream, new TypeReference<List<Exercise>>() {});

          for (Exercise exercise : exercises) {
              exerciseRepository.saveAndFlush(exercise);
          }
        }catch (Exception e){
           throw new RuntimeException(" Could not Read JSON data", e);
        }




    }
}
