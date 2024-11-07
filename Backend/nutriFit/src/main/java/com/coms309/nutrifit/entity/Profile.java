package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile
    {
        @Id
        @GeneratedValue (strategy = GenerationType.IDENTITY)
        private int id;

        @Column
        private String name;



        @OneToOne(mappedBy = "profile")
        @JsonIgnore
        @JoinColumn
        private User user;


        private double weight;

        @Column
        private int height;

        @OneToOne
        @PrimaryKeyJoinColumn
        private ImageData profileImageData;

        public Profile(User user){
            this.user = user;
            this.name = user.getFirstName() + " " + user.getLastName();
            if(user.getBodyWeights().size() != 0){
                this.weight= user.getBodyWeights().get(0).getWeight();
            }




        }


    }
