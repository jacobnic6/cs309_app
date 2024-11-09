package com.coms309.nutrifit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The type Friend.
 */
//not yet used
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    /**
     * The First user.
     */
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    User firstUser;

    /**
     * The Second user.
     */
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false )
    User secondUser;

    /**
     * Instantiates a new Friend.
     *
     * @param firstUser  the first user
     * @param secondUser the second user
     */
    public Friend(User firstUser, User secondUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        dateAdded = LocalDate.now();
    }
}
