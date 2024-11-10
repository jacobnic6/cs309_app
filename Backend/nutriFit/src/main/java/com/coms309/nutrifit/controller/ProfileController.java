package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.ImageData;
import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.service.ImageService;
import com.coms309.nutrifit.service.ProfileServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * The type Profile controller.
 */
@RestController
@RequestMapping("/profile")
public class ProfileController {
    /**
     * The Profile service handler.
     */
    @Autowired
    ProfileServiceHandler profileServiceHandler;
    /**
     * The Image service.
     */
    @Autowired
    ImageService imageService;


    /**
     * Add profile profile.
     *
     * @param profile the profile
     * @return the profile
     */
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
    @PostMapping("/{username}")
    public Profile addProfile(@PathVariable String username) {

        return profileServiceHandler.createProfileByName(username);
    }

    /**
     * Get all profiles list.
     *
     * @return the list
     */
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
    @PutMapping("/{username}")
    public String updateProfile(@PathVariable String username, @RequestBody Profile profile) {
        return profileServiceHandler.updateProfile(username, profile);
    }

    /**
     * Upload picture response entity.
     *
     * @param file     the file
     * @param username the username
     * @return the response entity
     * @throws IOException the io exception
     */
    @PostMapping("/image/{username}")
    public ResponseEntity<?> uploadPicture(@RequestParam("image") MultipartFile file, @PathVariable String username) throws IOException {


        ImageData img = imageService.saveImage(file);
        if (img != null) {
            profileServiceHandler.assignImage(img, username);
        }


        return ResponseEntity.status(HttpStatus.OK).body(img);
    }

    /**
     * Download picture response entity.
     *
     * @param fileName the file name
     * @return the response entity
     */
//Get a specific image
    @GetMapping("/image/{fileName}")
    public ResponseEntity<?> downloadPicture(@PathVariable String fileName) {
        byte[] imgData = imageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imgData);


    }

    /**
     * Get profile picture response entity.
     *
     * @param username the username
     * @return the response entity
     */
    @GetMapping("/{username}/pic")
    public ResponseEntity<?> getProfilePicture(@PathVariable String username) {

        Profile profile = profileServiceHandler.getUserProfile(username);
        byte[] imgData = imageService.downloadImage(profile.getProfileImageData().getName());


        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imgData);
    }


}
