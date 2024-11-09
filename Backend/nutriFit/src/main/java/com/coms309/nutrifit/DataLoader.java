package com.coms309.nutrifit;

import com.coms309.nutrifit.exercises.*;
import com.coms309.nutrifit.repo.*;
import com.coms309.nutrifit.service.ExerciseServiceHandler;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Data loader.
 */
@Service
public class DataLoader {
private final int exerciseSize = 1038;

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
    @Autowired
    private ExerciseServiceHandler exerciseServiceHandler;


    /**
     * Load data.
     *
     * @throws IOException the io exception
     */
    public void loadData() throws IOException {
        if(exerciseRepository.existsById(exerciseSize)){
            return;
        }
        exercises = new Exercises();
        JsonFactory factory = new JsonFactory();
List<Category> categoryArrayList = new ArrayList<>();
List<Equipment> equipmentArrayList = new ArrayList<>();

List<Exercise> exerciseArrayList = new ArrayList<>();
ArrayList<MuscleGroup> groups = new ArrayList<>();

        try (JsonParser parser = factory.createParser(new File("src/main/resources/exercises.json"))) {

            String fieldName ="";
            while ( parser.currentValue()!= JsonToken.END_OBJECT  || parser.nextToken() != JsonToken.END_OBJECT  ) {

                parser.nextToken();
                String text = parser.getText();
                if(JsonToken.END_ARRAY.equals(parser.getCurrentToken())) {
                    continue;
                }


                if(JsonToken.FIELD_NAME.equals(parser.getCurrentToken())) {
                    fieldName = parser.getText();

                    parser.nextToken();
                    parser.nextToken();
                    text = parser.getText();
                }

                if(fieldName.equals("categories")) {

                if(text != null) {
                    if(!categoryRepository.existsByName(text)){
                        Category category = new Category(text);
                        categoryArrayList.add(category);
                        categoryRepository.saveAndFlush(category);
                    }

                }




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

                            muscleRepository.save(muscle);
                        }

                    }


                if(fieldName.equals("muscle_groups")) {



                    String groupName = parser.getText();
                    MuscleGroup group = null;
                    ArrayList<Muscle> muscleArrayList = new ArrayList<>();

                    while(!parser.getCurrentToken().equals(JsonToken.END_OBJECT) ) {
                            String t = parser.getText();

                        if(parser.getCurrentToken().equals(JsonToken.END_ARRAY)) {

                            if(!muscleGroupRepository.existsByGroupName(group.getGroupName())){
                                muscleGroupRepository.saveAndFlush(group);
                                for(Muscle muscle : muscleArrayList){
                                    muscleRepository.updateMuscleGroupByName(group, muscle.getName());
                                    muscleRepository.saveAndFlush(muscle);
                                }
                            }


                            muscleArrayList = new ArrayList<>();
                            parser.nextToken();
                            continue;
                        }

                            if(JsonToken.START_ARRAY.equals(parser.getCurrentToken())) {

                                parser.nextToken();


                            }
                            if(parser.getCurrentToken().equals(JsonToken.FIELD_NAME)) {
                                groupName = parser.getText();
                                group = new MuscleGroup(groupName);

                            }else {
                                String muscle = parser.getText();
                                if(muscleRepository.existsByName(muscle)) {
                                    Muscle m = muscleRepository.getByName(muscle);
                                    m.setMuscleGroup(group);
                                    muscleArrayList.add(m);
                                }else{

                                    Muscle m = new Muscle(muscle);
                                    m.setMuscleGroup(group);
                                    muscleArrayList.add(m);
                                }
                            }

                            parser.nextToken();

                        }










                }
                if(fieldName.equals("exercises")||fieldName.equals("exercises_to_merge")) {

//                    ObjectReader reader = mapper.readerFor(Exercises.class);
//                   StringBuilder sb = new StringBuilder();
//                       JsonLocation l = parser.currentLocation();
                        Exercise exercise = new Exercise();
                    while(!parser.getCurrentToken().equals(JsonToken.END_OBJECT) || parser.nextToken().equals(JsonToken.END_ARRAY)) {
                        String t = parser.getText();

                        if(t.equals("exercises_to_merge")){
                            parser.nextToken();
                           parser.nextToken();

                        }
                        if(JsonToken.START_OBJECT.equals(parser.getCurrentToken()) || JsonToken.START_ARRAY.equals(parser.getCurrentToken())) {
                            //parser.nextToken();
                            exercise= mapper.readerFor(Exercise.class).readValue(parser);
                        }

                        if(!exerciseRepository.existsByName(exercise.getName())){
                            exerciseServiceHandler.addExercise(exercise);
                            exerciseArrayList.add(exercise);
                            exerciseRepository.save(exercise);
                        }

                        parser.nextToken();
                    }

                }



            }


        } catch (IOException e) {
            throw new RuntimeException(" could not create parser", e);
        }

        categoryRepository.saveAllAndFlush(categoryArrayList);
        equipmentRepository.saveAll(equipmentArrayList);

        exerciseRepository.saveAll(exerciseArrayList);


        muscleGroupRepository.saveAll(groups);
    }
}

