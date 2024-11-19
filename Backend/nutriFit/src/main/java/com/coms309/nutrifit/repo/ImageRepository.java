package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * The interface Image repository.
 */
@Repository
public interface ImageRepository extends JpaRepository<ImageData, Integer> {

    /**
     * Find by name image data.
     *
     * @param fileName the file name
     * @return the image data
     */
    ImageData findByName(String fileName);

    @Query("select (count(i) > 0) from ImageData i where upper(i.name) = upper(?1) ")
    boolean existsByNameAndTypeAllIgnoreCase(@NonNull String name);


}