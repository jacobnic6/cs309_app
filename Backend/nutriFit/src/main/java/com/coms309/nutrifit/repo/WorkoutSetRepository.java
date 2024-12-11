package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.fitness.Workout;
import com.coms309.nutrifit.entity.fitness.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Integer> {

	WorkoutSet findByWorkoutAndExerciseName(@NonNull Workout workout, @NonNull String exerciseName);
}