package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Workout repository.
 */
@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Integer> {
    /**
     * Find workouts by profile list.
     *
     * @param profile the profile
     * @return the list
     */
    List<Workout> findWorkoutsByProfile(Profile profile);


}