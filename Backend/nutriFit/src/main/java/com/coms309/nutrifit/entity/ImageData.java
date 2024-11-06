package com.coms309.nutrifit.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageData
    {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;




        @Lob
        private byte[] pictureData;

        private String name;

        private String type;


    }
