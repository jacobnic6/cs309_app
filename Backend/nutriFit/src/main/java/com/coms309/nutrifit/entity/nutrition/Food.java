package com.coms309.nutrifit.entity.nutrition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Food.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Food  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "meal_id", referencedColumnName = "id")
    @JsonIgnore
    private Meal meal;


    private String foodName;


    private String foodType;


    private int amount;



   @ElementCollection
    private Map<String, Integer> foodNutrients = new HashMap<>();



}
