package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Friend;
import com.coms309.nutrifit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Friend repository.
 */
@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {

    /**
     * Exists by first user and second user boolean.
     *
     * @param user  the user
     * @param user2 the user 2
     * @return the boolean
     */
    boolean existsByFirstUserAndSecondUser(User user, User user2);

    /**
     * Find by first user list.
     *
     * @param firstUser the first user
     * @return the list
     */
    List<Friend> findByFirstUser(User firstUser);

    /**
     * Find by second user list.
     *
     * @param secondUser the second user
     * @return the list
     */
    List<Friend> findBySecondUser(User secondUser);

}