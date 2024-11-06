package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageData, Integer>
    {

      ImageData findByName(String fileName);
    }