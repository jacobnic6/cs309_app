package com.coms309.nutrifit.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {

  User findById(int id);

  void deleteById(int id);
}