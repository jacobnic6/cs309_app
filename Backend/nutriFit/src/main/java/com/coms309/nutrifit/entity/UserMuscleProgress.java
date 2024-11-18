package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.util.UserMuscles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

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
    @Column(name = "id")
    private int id;


    @JsonProperty("muscle")
    private String muscle;

    @JsonProperty(value = "percentage")
    @Column( columnDefinition = "integer default 0")
    private int percentage;


    @JsonProperty(value = "tier")
    private int tier;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "user_id")
    @JsonIgnore
    private Profile profile;

public void updateProgress(int amount, int tier){
    this.tier += ((amount + percentage) / 100) + tier;
    percentage =((amount + percentage) % 100)  ;
}




}
