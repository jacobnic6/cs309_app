package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Image repository.
 */
@Repository
public interface ImageRepository extends JpaRepository<ImageData, Integer>
    {

        /**
         * Find by name image data.
         *
         * @param fileName the file name
         * @return the image data
         */
        ImageData findByName(String fileName);
    }