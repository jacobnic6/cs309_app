package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.exercises.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
	
}