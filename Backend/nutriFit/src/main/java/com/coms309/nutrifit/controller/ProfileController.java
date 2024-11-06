package com.coms309.nutrifit.controller;

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

@RestController
@RequestMapping("/profile")
public class ProfileController
    {
        @Autowired
        ProfileServiceHandler profileServiceHandler;
        @Autowired
        ImageService imageService;

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

            @PostMapping("/image/{username}")
            public ResponseEntity<?> uploadPicture(@RequestParam("image") MultipartFile file, @PathVariable String username) throws IOException {


               String upload =  imageService.saveImage(file);
               if(upload != null) {
                   profileServiceHandler.assignImage(upload, username);
               }


               return ResponseEntity.status(HttpStatus.OK).body(upload);
            }
        @GetMapping("/image/{fileName}")
        public ResponseEntity<?> downloadPicture(@PathVariable String fileName) {
            byte[] imgData =  imageService.downloadImage(fileName);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imgData);



        }





    }
