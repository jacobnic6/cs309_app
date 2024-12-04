package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.fitness.Workout;
import com.coms309.nutrifit.entity.fitness.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Integer> {
	@Query("select w from WorkoutSet w where w.workout.id = ?1 and upper(w.exerciseName) = upper(?2)")
	WorkoutSet findByWorkout_IdAndExerciseNameIgnoreCase(@NonNull int id, @NonNull String exerciseName);

	boolean existsByWorkout_IdAndExerciseNameIgnoreCase(@NonNull int id, @NonNull String exerciseName);

	WorkoutSet findByWorkoutAndExerciseName(@NonNull Workout workout, @NonNull String exerciseName);
}