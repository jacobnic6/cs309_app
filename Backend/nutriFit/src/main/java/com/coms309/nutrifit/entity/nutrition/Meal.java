package com.coms309.nutrifit.entity.nutrition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Food> foods;


    @ElementCollection
    private Map<String, Integer> mealNutrients = new HashMap<>();


    @Enumerated(EnumType.STRING )
    private MealType mealType;

    public Meal(UserMeals userMeals, List<Food> foods, Map<String, Integer> nutrientAmounts, String mealType){
        this.userMeals = userMeals;
        this.foods = foods;
        this.mealNutrients = nutrientAmounts;
        mealType.toUpperCase();
        this.mealType = MealType.valueOf(mealType);
    }


}
