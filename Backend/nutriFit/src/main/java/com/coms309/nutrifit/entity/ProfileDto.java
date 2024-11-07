package com.coms309.nutrifit.entity;

import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileDto {

    private int id;
    private String name;
    private double weight;
    private int height;


    private byte[] image;

    public ProfileDto(Profile profile, byte[] image) {
        this.id = profile.getId();
        this.name = profile.getName();
        this.weight = profile.getWeight();
        this.height = profile.getHeight();
        this.image = image;

    }
}
