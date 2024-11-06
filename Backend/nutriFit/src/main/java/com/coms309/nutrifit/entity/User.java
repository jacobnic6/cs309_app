package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {


    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;



    @OneToOne(cascade = CascadeType.ALL)
    private UserSettings settings;

    @OneToOne(cascade = CascadeType.ALL)
    private Profile profile;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

   @DateTimeFormat
    private LocalDateTime lastLogin;

   @OneToMany
   private List<Image> images;


   @JsonProperty("bodyweights")
   @OneToMany(cascade = CascadeType.ALL)
   @Column
   List<UserWeight> bodyWeights;

    public void addBodyWeight(UserWeight weightDto)
        {
            if(bodyWeights == null){
                bodyWeights = new ArrayList<>();
            }

            bodyWeights.add(weightDto);
        }
    public void addPicture(Image image){
        if(images == null){
            images = new ArrayList<>();
        }
        images.add(image);
    }


    public User(String firstName, String lastName, String email, String username, String password)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.username = username;
            this.password = password;

            this.lastLogin = LocalDateTime.now();
        bodyWeights = new ArrayList<>();
        images = new ArrayList<>();
        profile = new Profile(this);




        }

}
