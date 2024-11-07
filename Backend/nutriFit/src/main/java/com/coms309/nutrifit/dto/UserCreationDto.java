package com.coms309.nutrifit.dto;

import com.coms309.nutrifit.entity.User;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Data
public class UserCreationDto implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
}