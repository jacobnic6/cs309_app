package com.coms309.nutrifit.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.stereotype.Component;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class UserSettings {

        @Id
        @GeneratedValue (strategy = GenerationType.IDENTITY)
        private int id;


        @OneToOne(mappedBy = "settings", optional = false, orphanRemoval = true)
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
