package com.coms309.nutrifit.entity.nutrition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Meal.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_meals_id", referencedColumnName = "id")
    @JsonIgnore
    private UserMeals userMeals;


    @JsonProperty("foodName")
    private String foodName;

    @JsonProperty("servingSize")
    private String servingSize;


    @JsonProperty(value = "calories", defaultValue = "0", required = true)
    private int calories;

    @JsonProperty(value = "protein", defaultValue = "0", required = true)
    private int protein;


    @JsonProperty(value = "carbs", defaultValue = "0", required = true)
    private int carbs;

    @JsonProperty(value = "fat", defaultValue = "0", required = true)
    private int fat;

    @JsonProperty("mealType")
    private String mealType;


}
