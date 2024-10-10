package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.util.MeasurementUnits;
import com.coms309.nutrifit.util.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class UserSettings {

        @Id
        @GeneratedValue (strategy = GenerationType.IDENTITY)
        private int id;


        @OneToOne(mappedBy = "settings", optional = false)
        @JsonIgnore
        private User user;


        @Enumerated(EnumType.STRING)
        private Visibility profileVisibility;


        @Enumerated(EnumType.STRING)
        private Visibility biometricVisibility;


        @Enumerated(EnumType.STRING)
        private MeasurementUnits measurementUnits;


        @Column(columnDefinition = "BIT")
        private boolean messageNotifications;//0 for false 1 for true


        @Column(columnDefinition = "BIT")
        private boolean friendRequestNotifications;


        @Column(columnDefinition = "BIT")
        private boolean workoutRemindersEnabled;



        public UserSettings(){
            profileVisibility = Visibility.PRIVATE;
            biometricVisibility = Visibility.PRIVATE;
            measurementUnits = MeasurementUnits.IMPERIAL;
            messageNotifications = true;
            friendRequestNotifications = true;
            workoutRemindersEnabled = true;

        }
    }
