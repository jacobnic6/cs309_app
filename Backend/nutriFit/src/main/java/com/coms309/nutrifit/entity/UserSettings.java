package com.coms309.nutrifit.entity;

import com.coms309.nutrifit.util.MeasurementUnits;
import com.coms309.nutrifit.util.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
        @JsonProperty("profile_visibility")
        private Visibility profileVisibility;


        @Enumerated(EnumType.STRING)
        @JsonProperty("biometric_visibility")
        private Visibility biometricVisibility;


        @Enumerated(EnumType.STRING)
        @JsonProperty("measurement_units")
        private MeasurementUnits measurementUnits;


        @Column(columnDefinition = "BIT")
        @JsonProperty("message_notifications")
        private boolean messageNotifications;//0 for false 1 for true


        @Column(columnDefinition = "BIT")
        @JsonProperty("friend_request_notifications")
        private boolean friendRequestNotifications;


        @Column(columnDefinition = "BIT")
        @JsonProperty("workout_reminders_enabled")
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
