package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.entity.fitness.Workout;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * The type Post.
 */

@Entity
@Data
@AllArgsConstructor
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@DateTimeFormat
	private LocalDateTime postDateTime;

	@Column(length = 500)
	private String message;

	@OneToOne
	@PrimaryKeyJoinColumn
	@JoinColumn(name = "photo_id")
	private ImageData photo;

	@OneToOne
	@PrimaryKeyJoinColumn
	private Workout workout;

	public Post() {
		this.postDateTime = LocalDateTime.now();
	}

	//
	@JsonGetter("postedBy")
	public String getPostedBy() {
		return user.getUsername();
	}

}
