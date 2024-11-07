package com.coms309.nutrifit.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

//not yet used
@Entity
@Table(name = "friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    User firstUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    User secondUser;
}
