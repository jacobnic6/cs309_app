package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.ImageData;
import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.service.ImageService;
import com.coms309.nutrifit.service.ProfileServiceHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * The type Profile controller.
 */

@RestController
@Tag(name = "Profile Management")
@RequestMapping("/profile")
public class ProfileController {
    /**
     * The Profile service handler.
     */

   private final ProfileServiceHandler profileServiceHandler;
    /**
     * The Image service.
     */

    private final ImageService imageService;

    @Autowired
    public ProfileController(ProfileServiceHandler profileServiceHandler, ImageService imageService) {
        this.profileServiceHandler = profileServiceHandler;
        this.imageService = imageService;
    }


    /**
     * Add profile profile.
     *
     * @param profile the profile
     * @return the profile
     */
    @Operation(summary = "Create a user profile",
    description = "Tries to create profile. Not necessarily needed as profiles are created automatically upon user creation.")
    @PostMapping
    public Profile addProfile(@RequestBody Profile profile) {
        return profileServiceHandler.addProfile(profile);
    }

    /**
     * Add profile profile.
     *
     * @param username the username
     * @return the profile
     */
    @Operation(summary = "Create a user profile",
            description = "Tries to create profile for the given username.")
    @PostMapping("/{username}")
    public Profile addProfile(@PathVariable String username) {

        return profileServiceHandler.createProfileByName(username);
    }

    /**
     * Get all profiles list.
     *
     * @return the list
     */
    @Operation(summary = "Get all profiles",
            description = "Returns a list of all profiles")
    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileServiceHandler.getProfiles();
    }


    /**
     * Get user profile profile.
     *
     * @param username the username
     * @return the profile
     */
    @Operation(summary = "Get a specific user profile",
            description = "Finds and returns a specific profile by username")
    @GetMapping("/{username}")
    public Profile getUserProfile(@PathVariable String username) {

        Profile profile = profileServiceHandler.getUserProfile(username);
        // byte[] imgData =  imageService.downloadImage(profile.getProfileImageData().getName());


        return profile;
    }


    /**
     * Update profile string.
     *
     * @param username the username
     * @param profile  the profile
     * @return the string
     */
    @Operation(summary = "Update a specific user profile",
            description = "Finds and updates a specific profile by username")
    @PutMapping("/{username}")
    public String updateProfile(@PathVariable String username, @RequestBody Profile profile) {




        return profileServiceHandler.updateProfile(username, profile);
    }








}
