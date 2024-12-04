package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.exercises.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Exercise repository.
 */
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
	/**
	 * Exists by name boolean.
	 *
	 * @param name the name
	 *
	 * @return the boolean
	 */
	boolean existsByName(String name);

	@Query("select e from Exercise e where upper(e.name) like upper(concat('%', ?1, '%'))")
	List<Exercise> findByNameContainsIgnoreCase(@NonNull String name);

	List<Exercise> findByPrimaryMuscles_NameIgnoreCase(@NonNull String name);

	@Query("select e from Exercise e where upper(e.name) = upper(?1)")
	Exercise findByNameIgnoreCase(@NonNull String name);

}