package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.exercises.Exercise;
import com.coms309.nutrifit.exercises.Muscle;
import com.coms309.nutrifit.util.UserMuscles;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.lang.NonNull;
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



    private int progress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Profile profile;





}
