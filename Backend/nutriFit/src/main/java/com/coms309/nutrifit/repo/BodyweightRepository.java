package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.UserWeightDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BodyweightRepository extends JpaRepository<UserWeightDto, Integer>
    {

        boolean existsByWeightDateAndUserId(LocalDate date, int userId);

        UserWeightDto getByWeightDateAndUserId(LocalDate date, int id);



        List<UserWeightDto> getAllByUserId(int id);

        void deleteByWeightDateAndUserId(LocalDate date, int id);

        @Transactional
        @Modifying
        @Query("delete from UserWeightDto u where u.weightDate = ?1 and u.user = ?2")
        int deleteByWeightDateAndUser(@NonNull LocalDate weightDate, @NonNull User user);
    }
