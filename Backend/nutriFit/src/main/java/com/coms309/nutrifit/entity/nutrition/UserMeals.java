package com.coms309.nutrifit.entity.nutrition;

import com.coms309.nutrifit.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.crossstore.HashMapChangeSet;

import javax.crypto.MacSpi;
import java.io.Serializable;
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
public class UserMeals
{

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
    private List<Meal> mealList = new ArrayList<>() ;

    @ElementCollection
    private Map<String, Integer> nutrientTotals = new HashMap<>();






}
