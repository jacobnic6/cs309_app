package com.coms309.nutrifit;

import com.coms309.nutrifit.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Nutri fit application tests.
 */
@SpringBootTest
class NutriFitApplicationTests {

	@Autowired
	private UserController userController;

	/**
	 * Context loads.
	 */
	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
	}

}
