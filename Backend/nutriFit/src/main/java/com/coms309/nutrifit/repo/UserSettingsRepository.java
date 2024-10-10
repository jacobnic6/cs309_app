package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Integer>
    {

       UserSettings findById(int id);

        void removeUserSettingsById(int id);
    }