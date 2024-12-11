package com.coms309.nutrifit.repo;

import com.coms309.nutrifit.entity.Post;
import com.coms309.nutrifit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
	boolean existsByWorkout_Id(@NonNull int id);

	Post findByWorkout_Id(@NonNull int id);

	@Query("select p from Post p where p.user.id = ?1") List<Post> findByUser_Id(@NonNull int id);

	List<Post> findByUser_IdAndUser_Friends_SecondUserOrderByPostDateTimeDesc(@NonNull int id, @NonNull User secondUser);

}