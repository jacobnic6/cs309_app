package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer>
    {
    }