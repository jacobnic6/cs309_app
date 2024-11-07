package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Friend;
import com.coms309.nutrifit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {

    boolean existsByFirstUserAndSecondUser(User user, User user2);

    List<Friend> findByFirstUser(User firstUser);
    List<Friend> findBySecondUser(User secondUser);

}