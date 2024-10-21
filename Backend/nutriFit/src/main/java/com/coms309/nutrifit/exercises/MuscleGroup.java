package com.coms309.nutrifit.exercises;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class MuscleGroup {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String groupName;

  @ManyToOne
  @JoinColumn(name = "muscle_id")
  private Muscle muscle;

}