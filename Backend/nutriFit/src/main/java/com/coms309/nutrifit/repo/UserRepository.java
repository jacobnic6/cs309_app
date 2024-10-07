package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.users.User;
import com.coms309.nutrifit.users.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  public User findById(int id);

  public void deleteById(int id);

public boolean existsUserByIdOrEmailOrUsername(int id, String email, String username);


}