//package com.coms309.nutrifit.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.List;
//import java.util.Map;
//
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//public class Exercises {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @ElementCollection
//    private List<String> categories;
//
//
//    @ElementCollection
//    private List<String> equipment;
//    @ElementCollection
//    private List<String> muscles;
//
//    @ElementCollection
//    private Map<String, String> muscleGroups;
//
//    @ElementCollection
//    private List<String>  exercises;
//}
