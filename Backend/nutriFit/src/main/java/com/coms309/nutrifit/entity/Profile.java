package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile
    {
        @Id
        @Column(name = "user_id")
        private int id;

        private String name;



        @OneToOne
        @MapsId
        @JoinColumn(name = "user_id")
        @JsonIgnore
        private User user;


        private double weight;

        @OneToMany(mappedBy = "profile")
        private List<Workout> workouts;

        @Column
        private int height;

        @OneToOne
        @PrimaryKeyJoinColumn
        private ImageData profileImageData;

        public Profile(User user){
            this.user = user;
            this.name =  user.getFirstName() + " " + user.getLastName();
            if(user.getBodyWeights().size() != 0){
                this.weight= user.getBodyWeights().get(0).getWeight();
            }




        }
        public void AddWorkout(Workout workout){
            if(this.workouts == null){
                this.workouts = new ArrayList<>();
            }
            workouts.add(workout);
        }


    }
