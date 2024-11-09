package com.coms309.nutrifit.entity.nutrition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "meal_id", referencedColumnName = "id")
    private Meal meal;


    private String foodName;


    private String foodType;


    private int amount;

   @ElementCollection
    private Map<String, Integer> nutrients;



   private void addNutrient(String nutrient, int amount) {
       if(!nutrients.containsKey(nutrient)){
           nutrients.put(nutrient, amount);
       }
       else if (nutrients.containsKey(nutrient)){
           nutrients.put(nutrient, nutrients.get(nutrient) + amount);
       }

   }
}
