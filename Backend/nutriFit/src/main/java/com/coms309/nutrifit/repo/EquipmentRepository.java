package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.exercises.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Equipment repository.
 */
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    /**
     * Find by name equipment.
     *
     * @param name the name
     * @return the equipment
     */
    Equipment findByName(String name);

    /**
     * Exists by name boolean.
     *
     * @param equip the equip
     * @return the boolean
     */
    boolean existsByName(String equip);

    /**
     * Gets equipment by name.
     *
     * @param equip the equip
     * @return the equipment by name
     */
    Equipment getEquipmentByName(String equip);
}