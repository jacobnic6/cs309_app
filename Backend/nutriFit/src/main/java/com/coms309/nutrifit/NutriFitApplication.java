package com.coms309.nutrifit;

import com.coms309.nutrifit.entity.Exercise;
import com.coms309.nutrifit.repo.ExerciseRepository;
import com.coms309.nutrifit.repo.UserRepository;
import com.coms309.nutrifit.service.ExerciseServiceHandler;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.Map;

@SpringBootApplication
public class NutriFitApplication {

	public static void main(String[] args) {
		SpringApplication.run(NutriFitApplication.class, args);

	}


	@Bean
CommandLineRunner initExercises(ExerciseServiceHandler exerciseService) {
	return args -> {


		};
	}

}



