package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.entity.fitness.UserWeight;
import com.coms309.nutrifit.entity.nutrition.UserMeals;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The type User.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @JsonIgnore
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<UserMeals> meals = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Map<LocalDate, UserMeals> meals;

    @OneToMany(mappedBy = "firstUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Friend> friends;

    @OneToOne(cascade = CascadeType.ALL,  orphanRemoval = true)
    private UserSettings settings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;


    @NotBlank
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @DateTimeFormat
    private LocalDateTime lastLogin;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ImageData> imageData;

    /**
     * The Body weights.
     */
    @JsonProperty("bodyweights")
    @OneToMany(cascade = CascadeType.ALL)
    @Column
   private List<UserWeight> bodyWeights;

    public void addUserWeight(UserWeight userWeight)
        {
            bodyWeights.add(userWeight);
        }

}
