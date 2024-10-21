package com.coms309.nutrifit;

import com.coms309.nutrifit.entity.Exercise;
import com.coms309.nutrifit.repo.UserRepository;
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
		try
			{
				//ObjectMapper objectMapper = new ObjectMapper();
				 //objectMapper.readValue(new File("src/main/resources/exercises.json"), Map.class);
				JsonFactory jasonFactory = new JsonFactory();
				JsonParser jsonParser = jasonFactory.createJsonParser(new File("exercises.json"));
				while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
					//get the current token
					String fieldname = jsonParser.getCurrentName();
					if ("name".equals(fieldname)) {
						//move to next token
						jsonParser.nextToken();
						System.out.println(jsonParser.getText());
					}
				}
			}
			catch (Exception e){
			e.printStackTrace();
			}
	}


	@Bean
CommandLineRunner initUser(UserRepository userRepository) {
	return args -> {
//			User user1 = new User( "John", "Smith" , "john12@mail.com","jSmith123",  "password"  );
//
//
//
//			userRepository.save(user1);
//
		};
	}

}



