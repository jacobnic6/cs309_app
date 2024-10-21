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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_weights")
public class UserWeight
    {


        //private int userId;
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private double weight;

        @Column(unique = true)
        private LocalDate weightDate;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        @JsonIgnore
        @OnDelete(action = OnDeleteAction.CASCADE)
        private User user;



        public UserWeight(double weight, LocalDate weightDate, User user){
            this.weight = weight;
            this.user = user;
            this.weightDate = weightDate;
        }
        public UserWeight(double weight, User user){
            this.weight = weight;
            this.user = user;
            this.weightDate = LocalDate.now();
        }



    }
