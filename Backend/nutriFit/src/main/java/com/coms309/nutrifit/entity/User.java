package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.entity.nutrition.UserMeals;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The type User.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("user")
public class User {


    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMeals> meals = new ArrayList<>();

    @OneToMany(mappedBy = "firstUser")
    @JsonIgnore
    private List<Friend> friends;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private UserSettings settings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false, unique = true )
    private String username;

    @Column(nullable = false)
    private String password;

   @DateTimeFormat
    private LocalDateTime lastLogin;

   @OneToMany(cascade = CascadeType.ALL)
   @JsonIgnore
   private List<ImageData> imageData = new ArrayList<>();

    /**
     * The Body weights.
     */
    @JsonProperty("bodyweights")
   @OneToMany(cascade = CascadeType.ALL)
   @Column
   List<UserWeight> bodyWeights = new ArrayList<>();


    /**
     * Add body weight.
     *
     * @param weightDto the weight dto
     */
    public void addBodyWeight(UserWeight weightDto)
        {
            if(bodyWeights == null){
                bodyWeights = new ArrayList<>();
            }

            bodyWeights.add(weightDto);
        }




    /**
     * Instantiates a new User.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @param email     the email
     * @param username  the username
     * @param password  the password
     */
    public User(String firstName, String lastName, String email, String username, String password)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.username = username;
            this.password = password;

            this.lastLogin = LocalDateTime.now();
            meals = new ArrayList<>();
            friends = new ArrayList<>();
            imageData = new ArrayList<>();
            bodyWeights = new ArrayList<>();


        }


}
