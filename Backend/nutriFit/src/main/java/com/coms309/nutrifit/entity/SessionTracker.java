package com.coms309.nutrifit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Session tracker.
 */
//not yet used
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionTracker {

	private String username;

	private String password;

	private User currentUser;

	/**
	 * Instantiates a new Session tracker.
	 *
	 * @param username the username
	 * @param password the password
	 */
	public SessionTracker(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
