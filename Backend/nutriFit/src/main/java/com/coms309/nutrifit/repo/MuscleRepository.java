package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.exercises.Muscle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuscleRepository extends JpaRepository<Muscle, Integer> {
    Muscle findByName(String muscleName);
}