package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.util.UserMuscles;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

/**
 * The type User muscle progress.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMuscleProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @JsonProperty("muscle")
    private UserMuscles muscle;

    @JsonProperty(value = "progress", defaultValue = "0")
    private int percentage;


    @JsonProperty(value = "tier", defaultValue = "0")
    private int tier;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;







}
