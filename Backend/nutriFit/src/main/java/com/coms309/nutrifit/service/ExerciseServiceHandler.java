package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.Exercise;
import com.coms309.nutrifit.repo.ExerciseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ExerciseServiceHandler {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void importExercises(String path) throws IOException {
        File file = new File(path);
     List<Exercise> exerciseList = objectMapper.readValue(file, List.class );
        exerciseRepository.saveAll(exerciseList);
    }
}
