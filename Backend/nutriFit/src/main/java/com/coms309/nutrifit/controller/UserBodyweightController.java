package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.fitness.UserWeight;
import com.coms309.nutrifit.service.BodyweightServiceHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * The type User bodyweight controller.
 */
@Tag(name = "Bodyweight Management")
@RestController()
@RequestMapping("/bodyweights")
public class UserBodyweightController {

	private final BodyweightServiceHandler bodyweightServiceHandler;

	/**
	 * Instantiates a new User bodyweight controller.
	 *
	 * @param bodyweightServiceHandler the bodyweight service handler
	 */
	@Autowired
	public UserBodyweightController(BodyweightServiceHandler bodyweightServiceHandler) {
		this.bodyweightServiceHandler = bodyweightServiceHandler;
	}

	/**
	 * Gets user weights.
	 *
	 * @param username the username
	 *
	 * @return the user weights
	 */
//LIST all of a specific user's weight
	@Operation(summary = "List all weights of a specific user", description = "List all weights for specific user.")
	@GetMapping(path = "/{username}")
	public List<UserWeight> getUserWeights(@PathVariable String username) {

		return bodyweightServiceHandler.getUserWeights(username);

	}

	/**
	 * Add user weight user weight.
	 *
	 * @param username the username
	 * @param weight   the weight
	 *
	 * @return the user weight
	 */
//CREATE
	@Operation(summary = "Add a new user weight", description = "Adds a bodyweight for a specific user.")
	@PostMapping(path = "/{username}")
	public UserWeight addUserWeight(@PathVariable String username, @RequestBody UserWeight weight) {

		return bodyweightServiceHandler.addUserWeight(username, weight);

	}

	/**
	 * Get user weight by date user weight.
	 *
	 * @param username the username
	 * @param date     the date
	 *
	 * @return the user weight
	 */
//READ reads by date in the body
	@Operation(summary = "Get bodyweight on specified date.", description = "Returns a bodyweight for specific user on specified date.")
	@GetMapping(path = "/{username}/{date}")
	public UserWeight getUserWeightByDate(@PathVariable String username, @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		return bodyweightServiceHandler.getWeightByDate(username, date);
	}

	/**
	 * Update user weight user weight.
	 *
	 * @param username   the username
	 * @param userWeight the user weight
	 *
	 * @return the user weight
	 */
//UPDATE
	@Operation(summary = "Update user bodyweight.", description = "Updates user weight to the weight provided. The date is inside the UserWeight object.")
	@PutMapping(path = "/{username}")
	public UserWeight updateUserWeight(@PathVariable String username, @RequestBody UserWeight userWeight) {
		return bodyweightServiceHandler.updateUserWeight(username, userWeight);
	}

	/**
	 * Delete user weight string.
	 *
	 * @param username the username
	 * @param date     the date
	 *
	 * @return the string
	 */
//DELETE
	@Operation(summary = "Delete user weight on specific date.", description = "Takes username and a date as input and deletes the bodyweight if found.")
	@DeleteMapping(path = "/{username}/{date}")
	public String deleteUserWeight(@PathVariable String username, @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		return bodyweightServiceHandler.deleteUserWeight(username, date);
	}

}
