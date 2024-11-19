package com.coms309.nutrifit.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * The type Friend.
 */
//not yet used
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friends")
public class Friend {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	@Column(name = "date_added")
	private LocalDate dateAdded;

	/**
	 * The First user.
	 */
	@NotBlank
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(referencedColumnName = "id")
	private User firstUser;

	/**
	 * The Second user.
	 */
	@NotBlank
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(referencedColumnName = "id")
	private User secondUser;

}
