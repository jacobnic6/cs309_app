package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.util.Categories;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Exercise
 {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;



  private Categories category;

  private String name;
  @Nullable
  private List<String> aliases;
  @Nullable
  private String description;

  private List<String> instructions;
  @Nullable
  private List<String> tips;
  private List<String> primaryMuscles;
  private List<String> secondaryMuscles;
  @Nullable
  private String tempo;
  @Nullable
  private List<String> images;
  @Nullable
  private String video;
  @Nullable
  private List<String> variationOn;
  @Nullable
  private String licenseAuthor;
  @Nullable
  private Map<String, String> license;



 }
