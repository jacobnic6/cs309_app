package com.coms309.nutrifit.exercises;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Exercise
 {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;
  private String description;

  @ManyToOne
  private Category category;

  @ManyToMany
  private List<Equipment> equipment;

  @ElementCollection
  private List<String> instructions;

  @ManyToMany
  private List<Muscle> primaryMuscles;

  @ManyToMany
  private List<Muscle> secondaryMuscles;

  private String videoUrl;


 }
