package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    /**
     * Find by id user.
     *
     * @param id the id
     * @return the user
     */
    public User findById(int id);


    /**
     * Delete by id.
     *
     * @param id the id
     */
    public void deleteById(int id);

    /**
     * Gets id by username.
     *
     * @param username the username
     * @return the id by username
     */
    public int getIdByUsername(String username);


    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     */
    User findByUsername(String username);

    /**
     * Exists user by id or email or username boolean.
     *
     * @param id       the id
     * @param email    the email
     * @param username the username
     * @return the boolean
     */
    boolean existsUserByIdOrEmailOrUsername(int id, String email, String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}