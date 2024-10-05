package com.coms309.nutrifit.users;

import org.springframework.data.jpa.repository.JpaRepository;
public interface UserSettingsRepository extends JpaRepository<UserSettings, Integer>
    {
    }