package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    Optional<Workout> findByProfile_User_UsernameAndDateTracked(@NonNull String username, LocalDate dateTracked);

    List<Workout> findByProfile_IdAndDateTracked(@NonNull int id, @NonNull LocalDate dateTracked);


    boolean existsByProfileAndDateTracked(Profile profile, LocalDate date);

    Workout findWorkoutByProfileAndDateTracked(Profile profile, LocalDate date);
}