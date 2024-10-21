package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workouts")
public class Workout
    {
        @Id
        @GeneratedValue(strategy =GenerationType.IDENTITY)
        private int id;

        @Column(nullable = false)
       private double totalWeight;

       @Column(nullable = false)
        private String exerciseName;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        @JsonIgnore
        @OnDelete(action = OnDeleteAction.CASCADE)
        private User user;

        @Column(nullable = false)
        private LocalDate updatedAt;

        public Workout(double totalWeight, String exerciseName, User user){
                this.totalWeight = totalWeight;
                this.exerciseName = exerciseName;
                this.user = user;
                this.updatedAt = LocalDate.now();
        }

    }
