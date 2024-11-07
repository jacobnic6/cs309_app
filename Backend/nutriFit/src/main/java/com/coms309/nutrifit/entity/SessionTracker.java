package com.coms309.nutrifit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//not yet used
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionTracker {

    private String username;
    private String password;
    private User currentUser;

    public SessionTracker(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
