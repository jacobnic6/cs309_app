package com.coms309.nutrifit.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.coms309.nutrifit.entity.User}
 */
@Data
public class UserDto implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String username;

}