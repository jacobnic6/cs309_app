package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {


    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;


//    @ManyToMany
//    @JoinTable(name = "friendships", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "friend_id"))
//    @JsonIgnore
//    private Set<User> friends;

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

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

   @DateTimeFormat
    private LocalDateTime lastLogin;



   @OneToMany(cascade = CascadeType.ALL)
   @JsonIgnore
   private List<ImageData> imageData;


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
    public void addPicture(ImageData imageData){
        if(this.imageData == null){
            this.imageData = new ArrayList<>();
        }
        this.imageData.add(imageData);
    }


    public User(String firstName, String lastName, String email, String username, String password)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.username = username;
            this.password = password;

            this.lastLogin = LocalDateTime.now();
//        bodyWeights = new ArrayList<>();
//        imageData = new ArrayList<>();
//        profile = new Profile(this);




        }

}
