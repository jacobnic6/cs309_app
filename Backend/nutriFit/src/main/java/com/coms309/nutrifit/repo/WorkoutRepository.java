package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.fitness.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The interface Workout repository.
 */
@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Integer> {
	/**
	 * Find workouts by profile list.
	 *
	 * @param profile the profile
	 *
	 * @return the list
	 */
	List<Workout> findWorkoutsByProfile(Profile profile);

	/**
	 * Find by profile user username and date tracked optional.
	 *
	 * @param username    the username
	 * @param dateTracked the date tracked
	 *
	 * @return the optional
	 */
	Optional<Workout> findByProfile_User_UsernameAndDateTracked(@NonNull String username, LocalDate dateTracked);

	/**
	 * Find by profile id and date tracked list.
	 *
	 * @param id          the id
	 * @param dateTracked the date tracked
	 *
	 * @return the list
	 */
	List<Workout> findByProfile_IdAndDateTracked(@NonNull int id, @NonNull LocalDate dateTracked);

	/**
	 * Exists by profile and date tracked boolean.
	 *
	 * @param profile the profile
	 * @param date    the date
	 *
	 * @return the boolean
	 */
	boolean existsByProfileAndDateTracked(Profile profile, LocalDate date);

	/**
	 * Find workout by profile and date tracked workout.
	 *
	 * @param profile the profile
	 * @param date    the date
	 *
	 * @return the workout
	 */
	Workout findWorkoutByProfileAndDateTracked(Profile profile, LocalDate date);

	/**
	 * Exists by profile name and date tracked boolean.
	 *
	 * @param username the username
	 * @param date     the date
	 *
	 * @return the boolean
	 */
	boolean existsByProfile_NameAndDateTracked(String username, LocalDate date);

	@Query("select w from Workout w where w.profile.name = ?1 and w.dateTracked = ?2")
	Workout findByProfile_NameAndDateTracked(@NonNull String name, @NonNull LocalDate dateTracked);

}