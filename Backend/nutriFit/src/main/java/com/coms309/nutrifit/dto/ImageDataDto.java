package com.coms309.nutrifit.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.coms309.nutrifit.entity.ImageData}
 */
@Value
public class ImageDataDto implements Serializable {
    byte[] pictureData;
    String name;
    String type;
}