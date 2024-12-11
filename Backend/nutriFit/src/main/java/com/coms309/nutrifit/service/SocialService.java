package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.Post;
import com.coms309.nutrifit.entity.ProfileDto;
import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.fitness.Workout;
import com.coms309.nutrifit.repo.FriendRepository;
import com.coms309.nutrifit.repo.PostRepository;
import com.coms309.nutrifit.repo.ProfileRepository;
import com.coms309.nutrifit.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SocialService {

	private final FriendRepository friendRepository;

	private final ProfileRepository profileRepository;

	private final UserRepository userRepository;

	private final PostRepository postRepository;

	private final UserServiceHandler userServiceHandler;

	@Autowired
	public SocialService(FriendRepository friendRepository, ProfileRepository profileRepository, UserRepository userRepository, PostRepository postRepository, UserServiceHandler userServiceHandler) {
		this.friendRepository = friendRepository;
		this.profileRepository = profileRepository;
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.userServiceHandler = userServiceHandler;
	}

	public Post postWorkout(Workout workout, String username) {
		User user = userRepository.findByUsername(username);
		Post post;
		if (!postRepository.existsByWorkout_Id(workout.getId()))
		{
			post = new Post();
			post.setUser(user);
		} else
		{
			post = postRepository.findByWorkout_Id(workout.getId());
		}

		post.setWorkout(workout);
		user.getPosts().add(post);
		return postRepository.saveAndFlush(post);

	}

	public List<Post> getFriendPosts(String username) {
		if (!userRepository.existsByUsername(username))
		{
			throw new NullPointerException("User with username: " + username + " not found.");
		}
		User user = userRepository.findByUsername(username);
		List<ProfileDto> friendProfiles = userServiceHandler.getFriendsByUsername(username);
		List<Post> posts = new ArrayList<>();
		for (ProfileDto profileDto : friendProfiles)
		{
		
			postRepository.findByUser_Id(profileDto.getId()).forEach(posts::add);

		}
		posts.sort((Comparator.comparing(Post::getPostDateTime).reversed()));

		return posts;

	}

}
