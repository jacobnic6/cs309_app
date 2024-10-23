package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.exercises.Muscle;
import com.coms309.nutrifit.exercises.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MuscleRepository extends JpaRepository<Muscle, Integer> {
    Muscle findByName(String muscleName);

    boolean existsByName(String name);

    Muscle getByName(String muscle);

    @Transactional
    @Modifying
    @Query("update Muscle m set m.muscleGroup = ?1 where m.name = ?2")
    void updateMuscleGroupByName(@NonNull MuscleGroup muscleGroup, String name);
}