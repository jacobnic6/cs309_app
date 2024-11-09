package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Profile repository.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer>
    {
        /**
         * Find by user profile.
         *
         * @param user the user
         * @return the profile
         */
        Profile findByUser(User user);
    }