package com.coms309.nutrifit.exercises;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
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

  @Column(nullable = false, unique = true)
  @JsonProperty("name")
  private String name;

  @Column(columnDefinition = "MEDIUMTEXT")
  @JsonProperty("description")
  private String description;


 @ManyToOne
 @JoinColumn(name = "category_name")
 @JsonProperty("category")
  private Category category;

  @Column(nullable = false)
  @ManyToMany(fetch = FetchType.EAGER)
  @JsonProperty("equipment")
  private List<Equipment> equipment;

  @Column( columnDefinition = "MEDIUMTEXT")
  @ElementCollection
  @JsonProperty("instructions")
  private List<String> instructions;

  @Column(nullable = false)
  @ManyToMany
  @JsonProperty("primary_muscles")
  private List<Muscle> primaryMuscles;

  @Column(nullable = false)
  @ManyToMany
  @JsonProperty("secondary_muscles")
  private List<Muscle> secondaryMuscles;

  @Column
  @JsonProperty("video")
  private String videoUrl;

  @Column
  @ElementCollection
  @JsonProperty("variation_on")
  private List<String> variationOn;

  @Column
  @JsonProperty("variation_id")
  private int variationId;

  @Column
  @JsonProperty("license_author")
  private String licenseAuthor;

  @Column
  @ElementCollection
  @JsonProperty("license")
  Map<String, String> license;

  public Exercise(Category category, String name, List<Equipment> equipment, List<String> instructions,
                  List<Muscle> primaryMuscles, List<Muscle> secondaryMuscles){
   this.category = category;
   this.name = name;
   this.equipment = equipment;
   this.instructions = instructions;
   this.primaryMuscles = primaryMuscles;
   this.secondaryMuscles = secondaryMuscles;

  }



 }
