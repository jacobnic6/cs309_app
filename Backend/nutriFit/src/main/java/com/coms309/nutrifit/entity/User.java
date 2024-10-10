package com.coms309.nutrifit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {


    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_settings_id")
    private UserSettings settings;

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




    public User(String firstName, String lastName, String email, String username, String password)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.username = username;
            this.password = password;

            this.lastLogin = LocalDateTime.now();





        }

}
