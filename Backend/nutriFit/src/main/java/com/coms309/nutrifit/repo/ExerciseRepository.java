package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
}