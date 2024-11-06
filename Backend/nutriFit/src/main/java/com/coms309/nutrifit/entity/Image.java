package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image
    {
        @Id
        private int id;

        @JsonIgnore
        @ManyToOne
        private User user;

        @Lob
        private byte[] picture;

        private String name;

//        private void loadImage(String path){
//            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
//            if(inputStream == null){
//                throw new RuntimeException("Could not load image " + path);
//            }
//        }
//
//        public Image(String name){
//            this.name = name;
//            loadImage(name);
//        }

    }
