package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Workout.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "workouts")
public class Workout
    {
        @Id
        @GeneratedValue(strategy =GenerationType.AUTO)
        private int id;


        @ElementCollection
        private List<WorkoutSet> activities;



       private double totalWeight;



        @ManyToOne(fetch = FetchType.EAGER)
        @JsonIgnore
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Profile profile;

        @Column(nullable = false)
        private LocalDate dateTracked;

        /**
         * Instantiates a new Workout.
         *
         * @param profile the profile
         */
        public Workout( Profile profile){

                this.profile = profile;
                activities = new ArrayList<>();
                this.dateTracked = LocalDate.now();

        }

        /**
         * Instantiates a new Workout.
         */
        public Workout(){
            this.profile = null;
            activities = new ArrayList<>();
            this.dateTracked = LocalDate.now();
        }

        /**
         * Add activity.
         *
         * @param set the set
         */
        public void addActivity(WorkoutSet set){
            if(activities == null){
                activities = new ArrayList<>();
            }
            if(!activities.contains(set)){
                activities.add(set);
            }

        }

        /**
         * Update total weight.
         */
        public void updateTotalWeight(){
            if(activities != null){
                double tempTotal = 0;
                for(WorkoutSet set : activities){
                    double weight = set.getWeight() * set.getReps() * set.getSets();
                    tempTotal += weight;
                }
                totalWeight = tempTotal;
            }

        }





    }
