package com.coms309.nutrifit.dto;

import lombok.*;

import java.io.Serializable;

/**
 * The type Profile dto.
 */
@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto implements Serializable {


    private int id;
    private String name;
    private double weight;
    private int height;


    private byte[] image;


}
