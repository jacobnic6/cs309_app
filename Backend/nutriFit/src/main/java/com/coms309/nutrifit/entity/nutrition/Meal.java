package com.coms309.nutrifit.entity.nutrition;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
