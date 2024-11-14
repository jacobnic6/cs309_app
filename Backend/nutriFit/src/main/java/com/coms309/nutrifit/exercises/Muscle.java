package com.coms309.nutrifit.exercises;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The type Muscle.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Muscle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "muscle_group_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MuscleGroup muscleGroup;

    /**
     * Instantiates a new Muscle.
     *
     * @param name the name
     */
    public Muscle(String name) {
        this.name = name;
    }

    /**
     * Instantiates a new Muscle.
     *
     * @param name        the name
     * @param muscleGroup the muscle group
     */
    public Muscle(String name, MuscleGroup muscleGroup) {
        this.name = name;
        this.muscleGroup = muscleGroup;
    }


}
