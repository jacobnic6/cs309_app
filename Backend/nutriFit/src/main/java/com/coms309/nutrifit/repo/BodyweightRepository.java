package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.fitness.UserWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * The interface Bodyweight repository.
 */
@Repository
public interface BodyweightRepository extends JpaRepository<UserWeight, Integer> {
	/**
	 * Find by weight date and user username user weight.
	 *
	 * @param weightDate the weight date
	 * @param username   the username
	 *
	 * @return the user weight
	 */
	@Query("select u from UserWeight u where u.weightDate = ?1 and u.user.username = ?2")
	UserWeight findByWeightDateAndUser_Username(@NonNull LocalDate weightDate, @NonNull String username);

	/**
	 * Exists by weight date and user username boolean.
	 *
	 * @param weightDate the weight date
	 * @param username   the username
	 *
	 * @return the boolean
	 */
	@Query("select (count(u) > 0) from UserWeight u where u.weightDate = ?1 and u.user.username = ?2")
	boolean existsByWeightDateAndUser_Username(@NonNull LocalDate weightDate, @NonNull String username);

	/**
	 * Find by user username order by weight date asc list.
	 *
	 * @param username the username
	 *
	 * @return the list
	 */
	List<UserWeight> findByUser_UsernameOrderByWeightDateAsc(@NonNull String username);

	/**
	 * Exists by weight date and user id boolean.
	 *
	 * @param date   the date
	 * @param userId the user id
	 *
	 * @return the boolean
	 */
	boolean existsByWeightDateAndUserId(LocalDate date, int userId);

	/**
	 * Gets by weight date and user id.
	 *
	 * @param date the date
	 * @param id   the id
	 *
	 * @return the by weight date and user id
	 */
	UserWeight getByWeightDateAndUserId(LocalDate date, int id);

	/**
	 * Gets all by user id.
	 *
	 * @param id the id
	 *
	 * @return the all by user id
	 */
	List<UserWeight> getAllByUserId(int id);

	/**
	 * Delete by weight date and user id.
	 *
	 * @param date the date
	 * @param id   the id
	 */
	void deleteByWeightDateAndUserId(LocalDate date, int id);

	/**
	 * Delete by weight date and user int.
	 *
	 * @param weightDate the weight date
	 * @param user       the user
	 *
	 * @return the int
	 */
	@Transactional
	@Modifying
	@Query("delete from UserWeight u where u.weightDate = ?1 and u.user = ?2")
	int deleteByWeightDateAndUser(@NonNull LocalDate weightDate, @NonNull User user);

}
