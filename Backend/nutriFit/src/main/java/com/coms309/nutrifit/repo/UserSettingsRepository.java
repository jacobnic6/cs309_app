package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface User settings repository.
 */
@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Integer> {

	/**
	 * Find by id user settings.
	 *
	 * @param id the id
	 *
	 * @return the user settings
	 */
	UserSettings findById(int id);

	/**
	 * Remove user settings by id.
	 *
	 * @param id the id
	 */
	void removeUserSettingsById(int id);
}