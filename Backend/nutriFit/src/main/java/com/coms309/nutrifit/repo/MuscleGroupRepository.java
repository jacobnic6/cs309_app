package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.exercises.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuscleGroupRepository extends JpaRepository<MuscleGroup, Integer> {
    boolean existsByGroupName(String groupName);
}