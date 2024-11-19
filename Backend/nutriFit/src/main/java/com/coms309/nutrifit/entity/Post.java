package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.entity.fitness.Workout;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * The type Post.
 */
@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @MapsId
    private User owner;

    @DateTimeFormat
    private LocalDateTime postDateTime;


    @Column(length = 500)
    private String message;

    @OneToOne
    @PrimaryKeyJoinColumn
    @MapsId
    private ImageData photo;

    @OneToOne
    @PrimaryKeyJoinColumn
    @MapsId
    private Workout workout;
}
