package com.coms309.nutrifit.exercises;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class MuscleGroup {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String groupName;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "muscleGroup", cascade = CascadeType.ALL)
  private List<Muscle> muscle;

  public MuscleGroup(String groupName, List<Muscle> muscle) {
    this.groupName = groupName;
    this.muscle = muscle;
  }

}