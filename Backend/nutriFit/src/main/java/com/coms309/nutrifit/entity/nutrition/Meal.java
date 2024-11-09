package com.coms309.nutrifit.entity.nutrition;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_meals_id", referencedColumnName = "id")
    private UserMeals userMeals;

    @OneToMany(mappedBy = "meal")
    private List<Food> foods;


    @ElementCollection
    private Map<String, Integer> nutrientAmounts;

    @Enumerated
    @JsonProperty("meal_type")
    private MealType mealType;
}
