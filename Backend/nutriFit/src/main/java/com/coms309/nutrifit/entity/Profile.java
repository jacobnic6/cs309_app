package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile
    {
        @Id
        private int id;

        private String name;

        @JsonIgnore
        @OneToOne
        private User user;

        @OneToOne
        private UserWeight weight;

        @Column
        private int height;

        @OneToOne
        private Image profileImage;

        public Profile(User user){
            this.user = user;
            this.name = user.getFirstName();
            this.weight= user.getBodyWeights().get(0);



        }


    }
