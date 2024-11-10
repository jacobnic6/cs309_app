package com.coms309.nutrifit.entity.nutrition;

import com.coms309.nutrifit.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type User meals.
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMeals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "userMeals", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> mealList = new ArrayList<>();

    @ElementCollection
    private Map<String, Integer> nutrientTotals = new HashMap<>();


}
