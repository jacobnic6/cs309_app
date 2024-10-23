package com.coms309.nutrifit.exercises;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

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

  public MuscleGroup(String groupName, List<Muscle> muscle) {
    this.groupName = groupName;
    this.muscle = muscle;
  }
  public MuscleGroup(String groupName) {
    this.groupName = groupName;
    muscle = new ArrayList<Muscle>();
  }

  public void addMuscle(Muscle muscle) {
    this.muscle.add(muscle);
  }

}