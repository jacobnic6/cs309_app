package com.coms309.nutrifit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.coms309.nutrifit.entity.User}
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("username")

	private String username;

	@JsonProperty("weight")
	private double weight;

	@JsonProperty("height")
	private int height;

}