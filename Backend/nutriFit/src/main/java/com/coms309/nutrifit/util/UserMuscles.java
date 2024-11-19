package com.coms309.nutrifit.util;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The enum User muscles.
 */
public enum UserMuscles {
	/**
	 * Quads user muscles.
	 */

	@JsonProperty("quads")
	QUADS,
	/**
	 * Abs user muscles.
	 */
	@JsonProperty("abs")
	ABS,
	/**
	 * Biceps user muscles.
	 */
	@JsonProperty("biceps")
	BICEPS,
	/**
	 * Chest user muscles.
	 */
	@JsonProperty("chest")
	CHEST,
	/**
	 * Triceps user muscles.
	 */
	@JsonProperty("triceps")
	TRICEPS,
	/**
	 * Back user muscles.
	 */
	@JsonProperty("back")
	BACK,
	/**
	 * Hamstrings user muscles.
	 */
	@JsonProperty("hamstrings")
	HAMSTRINGS,
	/**
	 * Calves user muscles.
	 */
	@JsonProperty("calves")
	CALVES;

}
