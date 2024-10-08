package com.coms309.nutrifit;

import com.coms309.nutrifit.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NutriFitApplication {

	public static void main(String[] args) {
		SpringApplication.run(NutriFitApplication.class, args);
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



