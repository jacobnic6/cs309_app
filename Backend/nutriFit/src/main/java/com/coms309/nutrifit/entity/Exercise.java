package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.util.Categories;
import com.coms309.nutrifit.util.Equipment;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Exercise
 {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;



  private Categories category;


  @Column(nullable = false, unique = true)
  private String name;


  @ElementCollection
  private List<String>  aliases;


  @Column
  private String description;

  @Column
  @ElementCollection
  private List<Equipment> equipment;

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
