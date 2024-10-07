package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.users.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Integer>
    {

    }