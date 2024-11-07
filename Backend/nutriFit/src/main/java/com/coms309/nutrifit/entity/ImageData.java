package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

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
        @JsonIgnore
        private byte[] pictureData;

        private String name;

        private String type;


    }
