package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.exercises.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Muscle group repository.
 */
@Repository
public interface MuscleGroupRepository extends JpaRepository<MuscleGroup, Integer> {
    /**
     * Exists by group name boolean.
     *
     * @param groupName the group name
     * @return the boolean
     */
    boolean existsByGroupName(String groupName);
}