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

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
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

    private Exercises exercises;


    public DataLoader(ObjectMapper mapper, ExerciseRepository exerciseRepository) {
        this.mapper = mapper;
        this.exerciseRepository = exerciseRepository;
        exercises = new Exercises();
    }

    public void readJson(String filename) throws IOException {
        JsonFactory factory = new JsonFactory();
        URL url;
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(filename).openStream(), "UTF-8"));
    //DataReader dr = new JsonReader(br);

    }


    @Transactional
    public void loadData() throws IOException {

        JsonFactory factory = new JsonFactory();
List<Category> categoryArrayList = new ArrayList<>();
List<Equipment> equipmentArrayList = new ArrayList<>();
List<Muscle> muscleArrayList = new ArrayList<>();
List<Exercise> exerciseArrayList = new ArrayList<>();
ArrayList<MuscleGroup> groups = new ArrayList<>();

        try (JsonParser parser = factory.createParser(new File("src/main/resources/exercises.json"))) {


            while ( parser.currentValue()!= JsonToken.END_OBJECT  || parser.nextToken() != JsonToken.END_OBJECT  ) {

                parser.nextToken();
                String text = parser.getText();
                String fieldName ="";


                if(JsonToken.FIELD_NAME.equals(parser.getCurrentToken())) {
                    fieldName = parser.getText();


                }

                if(fieldName.equals("categories")) {




                        Category category = new Category(text);
                        categoryArrayList.add(category);
                        categoryRepository.saveAndFlush(category);


                }
                if(fieldName.equals("equipment")) {





                        if(text != null) {
                            if(equipmentRepository.existsByName(text)){
                                Equipment equipment = equipmentRepository.getEquipmentByName(text);
                                equipmentArrayList.add(equipment);
                                equipmentRepository.save(equipment);
                            }else {

                                Equipment equipment = new Equipment(text);
                                equipmentArrayList.add(equipment);
                                equipmentRepository.save(equipment);
                            }
                        }






                }
                if(fieldName.equals("muscles")) {



                        if(!muscleRepository.existsByName(text)){
                            Muscle muscle = new Muscle(text);
                            muscleArrayList.add(muscle);
                            muscleRepository.save(muscle);
                        }

                    }


                if(fieldName.equals("muscle_groups")) {


                        String groupName = "";


                        parser.nextToken();
                        while(parser.nextToken() != JsonToken.END_ARRAY) {

                            if(parser.nextToken() == JsonToken.START_ARRAY) {

                                groupName = parser.getText();

                            }else{
                                String muscle = parser.getText();
                                if(!muscleRepository.existsByName(muscle)) {
                                    Muscle m = new Muscle(muscle);
                                    muscleArrayList.add(m);

                                }else{

                                    muscleArrayList.add(muscleRepository.getByName(muscle));
                                }

                            }

                        }
                        MuscleGroup muscleGroup = new MuscleGroup(groupName, muscleArrayList);

                        muscleGroupRepository.saveAndFlush(muscleGroup);

                }
                if(fieldName.equals("exercises")) {
                    ObjectMapper mapper = new ObjectMapper();
                }

            }


        } catch (IOException e) {
            throw new RuntimeException(" could not create parser", e);
        }

        categoryRepository.saveAllAndFlush(categoryArrayList);
        equipmentRepository.saveAll(equipmentArrayList);
        muscleRepository.saveAll(muscleArrayList);
        exerciseRepository.saveAll(exerciseArrayList);


        muscleGroupRepository.saveAll(groups);
    }
}

