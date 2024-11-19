package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.exercises.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Category repository.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	/**
	 * Find by name category.
	 *
	 * @param category the category
	 *
	 * @return the category
	 */
	Category findByName(String category);

	/**
	 * Exists by name boolean.
	 *
	 * @param text the text
	 *
	 * @return the boolean
	 */
	boolean existsByName(String text);

	/**
	 * Gets by name.
	 *
	 * @param name the name
	 *
	 * @return the by name
	 */
	Category getByName(String name);
}