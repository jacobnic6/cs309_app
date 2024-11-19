package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.exercises.Muscle;
import com.coms309.nutrifit.exercises.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Muscle repository.
 */
@Repository
public interface MuscleRepository extends JpaRepository<Muscle, Integer> {
	/**
	 * Find by name muscle.
	 *
	 * @param muscleName the muscle name
	 *
	 * @return the muscle
	 */
	Muscle findByName(String muscleName);

	/**
	 * Exists by name boolean.
	 *
	 * @param name the name
	 *
	 * @return the boolean
	 */
	boolean existsByName(String name);

	/**
	 * Gets by name.
	 *
	 * @param muscle the muscle
	 *
	 * @return the by name
	 */
	Muscle getByName(String muscle);

	/**
	 * Update muscle group by name.
	 *
	 * @param muscleGroup the muscle group
	 * @param name        the name
	 */
	@Transactional
	@Modifying
	@Query("update Muscle m set m.muscleGroup = ?1 where m.name = ?2")
	void updateMuscleGroupByName(@NonNull MuscleGroup muscleGroup, String name);

}