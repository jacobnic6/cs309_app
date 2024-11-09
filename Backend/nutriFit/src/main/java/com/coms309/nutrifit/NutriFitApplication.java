package com.coms309.nutrifit;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.nutrition.Food;
import com.coms309.nutrifit.entity.nutrition.Meal;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
import com.coms309.nutrifit.service.ExerciseServiceHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The type Nutri fit application.
 */
@SpringBootApplication
public class NutriFitApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		SpringApplication.run(NutriFitApplication.class, args);

	}


    /**
     * Run command line runner.
     *
     * @param dataLoader the data loader
     * @return the command line runner
     */
    @Bean
	CommandLineRunner run(DataLoader dataLoader) {
		return args -> {
			dataLoader.loadData();

		};
	}

}



