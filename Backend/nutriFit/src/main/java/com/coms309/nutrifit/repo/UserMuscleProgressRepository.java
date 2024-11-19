package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.fitness.UserMuscleProgress;
import com.coms309.nutrifit.util.UserMuscles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface User muscle progress repository.
 */
public interface UserMuscleProgressRepository extends JpaRepository<UserMuscleProgress, Integer> {
	/**
	 * Find by muscle and profile boolean.
	 *
	 * @param muscle  the muscle
	 * @param profile the profile
	 *
	 * @return the boolean
	 */
	boolean findByMuscleAndProfile(UserMuscles muscle, Profile profile);

	/**
	 * Find all by profile name list.
	 *
	 * @param username the username
	 *
	 * @return the list
	 */
	List<UserMuscleProgress> findAllByProfile_Name(String username);

	/**
	 * Find user muscle progress by profile name and and muscle user muscle progress.
	 *
	 * @param username   the username
	 * @param musclename the musclename
	 *
	 * @return the user muscle progress
	 */
	UserMuscleProgress findUserMuscleProgressByProfile_NameAndAndMuscle(String username, String musclename);
}