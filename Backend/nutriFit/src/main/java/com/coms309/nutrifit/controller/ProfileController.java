package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.Image;
import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.service.ProfileServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController
    {
        @Autowired
        ProfileServiceHandler profileServiceHandler;

        @PostMapping
        public Profile addProfile(@RequestBody Profile profile)
            {
                return profileServiceHandler.addProfile(profile);
            }

        @PostMapping("/{username}")
        public Profile addProfile(@PathVariable String username)
            {

                return profileServiceHandler.addProfileByName(username);
            }

        @GetMapping
        public List<Profile> getAllProfiles(){
            return profileServiceHandler.getProfiles();
        }

        @GetMapping("/{username}")
        public Profile getUserProfile(@PathVariable String username){
            return profileServiceHandler.getUserProfile(username);
        }

        @PutMapping("/{username}")
        public String updateProfile(@PathVariable String username, @RequestBody Profile profile)
            {
                return profileServiceHandler.updateProfile(username, profile);
            }



    }
