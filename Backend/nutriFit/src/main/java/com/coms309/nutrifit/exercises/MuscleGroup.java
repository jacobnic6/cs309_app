package com.coms309.nutrifit.exercises;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Muscle group.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@JsonTypeName("muscle_groups")
public class MuscleGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String groupName;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "muscleGroup", cascade = CascadeType.ALL)
    private List<Muscle> muscle;

    /**
     * Instantiates a new Muscle group.
     *
     * @param groupName the group name
     * @param muscle    the muscle
     */
    public MuscleGroup(String groupName, List<Muscle> muscle) {
        this.groupName = groupName;
        this.muscle = muscle;
    }

    /**
     * Instantiates a new Muscle group.
     *
     * @param groupName the group name
     */
    public MuscleGroup(String groupName) {
        this.groupName = groupName;
        muscle = new ArrayList<Muscle>();
    }

    /**
     * Add muscle.
     *
     * @param muscle the muscle
     */
    public void addMuscle(Muscle muscle) {
        this.muscle.add(muscle);
    }

}