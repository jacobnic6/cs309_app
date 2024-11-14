package com.coms309.nutrifit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.lang.NonNull;

import java.io.Serializable;

/**
 * DTO for {@link com.coms309.nutrifit.entity.User}
 */
@Builder
@Data
@Getter
@Setter
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