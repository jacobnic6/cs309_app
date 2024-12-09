package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Profile repository.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

	/**
	 * Find by user profile.
	 *
	 * @param user the user
	 *
	 * @return the profile
	 */
	Profile findByUser(User user);

	/**
	 * Find by user username optional.
	 *
	 * @param username the username
	 *
	 * @return the optional
	 */
	Optional<Profile> findByUser_Username(@NonNull String username);

	/**
	 * Find by name profile.
	 *
	 * @param username the username
	 *
	 * @return the profile
	 */
	Profile findByName(String username);
}