package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Integer> {
  List<Workout> findWorkoutsByProfile(Profile profile);


}