package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.UserSettings;
import com.coms309.nutrifit.service.UserSettingsServiceHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type User settings controller.
 */
@Tag(name = "Settings Management")
@RestController
public class UserSettingsController {

	@Autowired
	private UserSettingsServiceHandler settingsServiceHandler;

	/**
	 * Create user string.
	 *
	 * @param settings the settings
	 *
	 * @return the string
	 */
//CREATE
	@Operation(summary = "Create a user settings page",
			description = "Takes input of a settings page and adds it to the db",
			tags = {"Settings Management"})
	@PostMapping(path = "/settings")
	String createUser(@RequestBody UserSettings settings) {

		return settingsServiceHandler.createUserSettings(settings);
	}

	/**
	 * Gets user settings.
	 *
	 * @param id the id
	 *
	 * @return the user settings
	 */
//READ
	@Operation(summary = "Read a user settings page",
			description = "Takes input of a settings id and adds it to the db",
			tags = {"Settings Management"})
	@GetMapping(path = "/settings/{id}")
	public UserSettings getUserSettings(@PathVariable int id) {
		return settingsServiceHandler.getSettingsById(id);
	}

	/**
	 * Update user settings user settings.
	 *
	 * @param id       the id
	 * @param settings the settings
	 *
	 * @return the user settings
	 */
//UPDATE
	@Operation(summary = "Create a user settings page",
			description = "Takes input of a settings page id and adds it to the db",
			tags = {"Settings Management"})
	@PutMapping(path = "/settings/{id}")
	public UserSettings updateUserSettings(@PathVariable int id, @RequestBody UserSettings settings) {

		return settingsServiceHandler.updateSettings(id, settings);
	}

	/**
	 * Gets all user settings.
	 *
	 * @return the all user settings
	 */
//LIST
	@Operation(summary = "List all settings pages",
			description = "Returns a list of every user settings page",
			tags = {"Settings Management"})
	@GetMapping(path = "/settings")
	public List<UserSettings> getAllUserSettings() {

		return settingsServiceHandler.listAllUserSettings();
	}

	/**
	 * Delete user settings string.
	 *
	 * @param id the id
	 *
	 * @return the string
	 */
//DELETE
	@Operation(summary = "Delete a user settings page",
			description = "Takes input of a settings page id and removes it from the db",
			tags = {"Settings Management"})
	@DeleteMapping("/settings/{id}")
	public String deleteUserSettings(@PathVariable int id) {

		return settingsServiceHandler.deleteSettings(id);
	}

}
