package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.util.Categories;
import com.coms309.nutrifit.util.Equipment;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

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

  @Column(nullable = false)
  @ElementCollection
  private Set<String> category;


  @Column(nullable = false, unique = true)
  private String name;

  @Column
  @ElementCollection
  private List<String>  aliases;


  @Column
  private String description;

  @Column
  @ElementCollection
  private List<String> equipment;

  @Column
  @ElementCollection
  private List<String> instructions;

  @Column
  @ElementCollection
  private List<String> tips;

  @Column
  @ElementCollection
  private List<String> primaryMuscles;

  @Column
  @ElementCollection
  private List<String> secondaryMuscles;

  @Column
  private String tempo;

  @Column
  @ElementCollection
  private List<String> images;

  @Column
  private String video;

  @Column
  @ElementCollection
  private List<String> variationOn;

  @Column
  private String licenseAuthor;

  @Column
  @ElementCollection
  private Map<String, String> license;



 }
