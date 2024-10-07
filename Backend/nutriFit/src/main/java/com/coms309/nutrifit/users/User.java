package com.coms309.nutrifit.users;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.type.descriptor.java.LocalDateTimeJavaType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;


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
