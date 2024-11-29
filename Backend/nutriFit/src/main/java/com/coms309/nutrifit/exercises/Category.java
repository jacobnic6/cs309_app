package com.coms309.nutrifit.exercises;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Category.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true, name = "name")
	@JsonProperty("category")
	private String name;

	/**
	 * Instantiates a new Category.
	 *
	 * @param name the name
	 */
	public Category(String name) {
		this.name = name;
	}

}