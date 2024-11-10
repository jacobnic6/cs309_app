package com.coms309.nutrifit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Profile dto.
 */
@Getter
@Setter
@NoArgsConstructor
public class ProfileDto {

    private int id;
    private String name;
    private double weight;
    private int height;


    private byte[] image;

    /**
     * Instantiates a new Profile dto.
     *
     * @param profile the profile
     * @param image   the image
     */
    public ProfileDto(Profile profile, byte[] image) {
        this.id = profile.getId();
        this.name = profile.getName();
        this.weight = profile.getWeight();
        this.height = profile.getHeight();
        this.image = image;

    }
}
