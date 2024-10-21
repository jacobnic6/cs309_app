package com.coms309.nutrifit;

import com.coms309.nutrifit.exercises.*;
import com.coms309.nutrifit.repo.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

       Exercises exercises = new Exercises();



    }
}

//@Transactional
//public void loadData() throws IOException {
//
//    JsonFactory factory = new JsonFactory();
//
//    try (JsonParser parser = factory.createParser(Paths.get("src/main/resources/exercises.json").toFile())) {
//
//
//        while (!parser.isClosed() && parser.nextToken() != JsonToken.NOT_AVAILABLE) {
//
//            String fieldName = parser.currentName();
//
//            if(fieldName.equals("categories")) {
//                while(parser.nextToken() != JsonToken.END_ARRAY) {
//                    parser.nextToken();
//                    Category category = new Category(parser.getText());
//                    categoryRepository.save(category);
//                }
//
//            }
//            if(fieldName.equals("equipment")) {
//                while(parser.nextToken() != JsonToken.END_ARRAY) {
//                    parser.nextToken();
//                    Equipment equipment = new Equipment(parser.getText());
//                    equipmentRepository.save(equipment);
//                }
//
//
//            }
//            if(fieldName.equals("muscles")) {
//                while(parser.nextToken() != JsonToken.END_ARRAY) {
//                    parser.nextToken();
//                    Muscle muscle = new Muscle(parser.getText());
//                    muscleRepository.save(muscle);
//                }
//
//            }
//            if(fieldName.equals("muscle_groups")) {
//
//                while(parser.nextToken() != JsonToken.END_OBJECT) {
//                    String groupName = "";
//
//                    ArrayList<Muscle> muscleList = new ArrayList<>();
//                    parser.nextToken();
//                    while(parser.nextToken() != JsonToken.END_ARRAY) {
//
//                        if(parser.nextToken() == JsonToken.START_ARRAY) {
//                            muscleList = new ArrayList<>();
//                            groupName = parser.getText();
//
//                        }else{
//
//                            muscleList.add(new Muscle(parser.getText()));
//                        }
//
//                    }
//                    MuscleGroup muscleGroup = new MuscleGroup(groupName, muscleList);
//                    muscleGroupRepository.save(muscleGroup);
//                }
//            }
//            if(fieldName.equals("exercises")) {
//                ObjectMapper mapper = new ObjectMapper();
//            }
//
//        }
//
//
//    } catch (IOException e) {
//        throw new RuntimeException(" could not create parser", e);
//    }
//
//}