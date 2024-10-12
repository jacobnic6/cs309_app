package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import netscape.javascript.JSObject;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_weights")
public class UserWeightDto
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



        public UserWeightDto(double weight, LocalDate weightDate, User user){
            this.weight = weight;
            this.user = user;
            this.weightDate = weightDate;
        }
        public UserWeightDto(double weight,  User user){
            this.weight = weight;
            this.user = user;
            this.weightDate = LocalDate.now();
        }



    }
