package com.coms309.nutrifit;

import com.coms309.nutrifit.exercises.*;
import com.coms309.nutrifit.repo.*;
import com.coms309.nutrifit.service.ExerciseServiceHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Data loader.
 */
@Service
public class DataLoader {
	private final int exerciseSize = 1038;

	private final CategoryRepository categoryRepository;

	private final EquipmentRepository equipmentRepository;

	private final MuscleRepository muscleRepository;

	private final MuscleGroupRepository muscleGroupRepository;

	private final ExerciseRepository exerciseRepository;

	private final ObjectMapper mapper;

	private final ExerciseServiceHandler exerciseServiceHandler;

	@Autowired
	public DataLoader(CategoryRepository categoryRepository, EquipmentRepository equipmentRepository, MuscleRepository muscleRepository, MuscleGroupRepository muscleGroupRepository, ExerciseRepository exerciseRepository, ObjectMapper mapper, ExerciseServiceHandler exerciseServiceHandler) {
		this.categoryRepository = categoryRepository;
		this.equipmentRepository = equipmentRepository;
		this.muscleRepository = muscleRepository;
		this.muscleGroupRepository = muscleGroupRepository;
		this.exerciseRepository = exerciseRepository;
		this.mapper = mapper;
		this.exerciseServiceHandler = exerciseServiceHandler;
	}

	/**
	 * Load data.
	 *
	 * @throws IOException the io exception
	 */

	public void loadData() throws IOException {
		if (exerciseRepository.count() >= exerciseSize)
		{

			return;
		}

		JsonNode rootNode = mapper.readTree(new File("src/main/resources/exercises.json"));
		List<String> fieldNames = new ArrayList<>();
		rootNode.fieldNames().forEachRemaining(fieldNames::add);

		for (String fieldName : fieldNames)
		{
			JsonNode childNode = rootNode.get(fieldName);
			switch (fieldName)
			{
				case "categories":
					List<Category> categoryArrayList = new ArrayList<>();
					for (JsonNode node : childNode)
					{
						Category category = mapper.convertValue(node, Category.class);
						if (!categoryRepository.existsByName(category.getName()))
						{

							categoryArrayList.add(category);
						}

					}
					categoryRepository.saveAllAndFlush(categoryArrayList);
					break;
				case "equipment":
					List<Equipment> equipmentArrayList = new ArrayList<>();
					for (JsonNode node : childNode)
					{
						Equipment equipment = mapper.convertValue(node, Equipment.class);

						if (!equipmentRepository.existsByName(equipment.getName()))
						{

							equipmentArrayList.add(equipment);
						}
					}
					equipmentRepository.saveAllAndFlush(equipmentArrayList);
					break;
				case "muscles":
					List<Muscle> musclesList = new ArrayList<>();
					for (JsonNode node : childNode)
					{
						Muscle muscle = mapper.convertValue(node, Muscle.class);

						if (!muscleRepository.existsByName(muscle.getName()))
						{

							musclesList.add(muscle);
						}
					}
					muscleRepository.saveAll(musclesList);
					break;
				case "muscle_groups":

					ArrayList<String> groupNames = new ArrayList<>();
					childNode.fieldNames().forEachRemaining(groupNames::add);

					for (String groupName : groupNames)
					{
						if (muscleGroupRepository.existsByGroupName(groupName))
						{
							continue;
						}
						MuscleGroup g = new MuscleGroup(groupName);
						muscleGroupRepository.save(g);

						List<String> muscles = new ArrayList<>();
						JsonNode node = childNode.get(groupName);

						node.elements().forEachRemaining(muscle -> {
							muscles.add(muscle.asText());
						});
						for (String muscle : muscles)
						{
							Muscle m;
							if (muscleRepository.existsByName(muscle))
							{
								m = muscleRepository.getByName(muscle);

							} else
							{
								m = muscleRepository.save(new Muscle(muscle));

							}
							m.setMuscleGroup(g);
							g.addMuscle(m);
							muscleGroupRepository.saveAndFlush(g);
							muscleRepository.saveAndFlush(m);

						}

					}
					break;
				case "exercises":

					loadExercise(rootNode.get("exercises"));
					break;

				case "exercises_to_merge":

					loadExercise(rootNode.get("exercises_to_merge"));
					break;

			}
		}

	}

	private void loadExercise(JsonNode childNode) {
		childNode.elements().forEachRemaining(node -> {
			Exercise exercise = mapper.convertValue(node, Exercise.class);
			if (!exerciseRepository.existsByName(exercise.getName()))
			{

				exerciseServiceHandler.addExercise(exercise);

				exerciseRepository.saveAndFlush(exercise);

			}

		});
	}
}

