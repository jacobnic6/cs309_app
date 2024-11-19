package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.ImageData;
import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.service.ImageService;
import com.coms309.nutrifit.service.ProfileServiceHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@Tag(name = "Image Management")
@RequestMapping("/images")
public class ImageController {


    /**
     * The Image service.
     */

    private final ProfileServiceHandler profileServiceHandler;
    /**
     * The Image service.
     */

    private final ImageService imageService;

    @Autowired
    public ImageController(ProfileServiceHandler profileServiceHandler, ImageService imageService) {
        this.profileServiceHandler = profileServiceHandler;
        this.imageService = imageService;
    }


    /**
     * Upload picture response entity.
     *
     * @param file     the file
     * @param username the username
     * @return the response entity
     * @throws IOException the io exception
     */
    @Operation(summary = "Upload an image to specific profile",
            description = "Uploads an image to the specified user's profile.")
    @PostMapping("/upload/{username}")
    public ResponseEntity<?> uploadPicture( @RequestParam(value = "image" ) MultipartFile file, @PathVariable String username) throws IOException {
        Profile profile = profileServiceHandler.getUserProfile(username);
        if (profile == null) {
          return   new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        ImageData img = imageService.saveImage(file);
        if (img.getPictureData() != null) {
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
    @Operation(summary = "Fetch image by filename.",
            description = "Fetches picture by filename.")
    @GetMapping("/file/{fileName}")
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
    @Operation(summary = "Fetch profile image.",
            description = "Fetches profile picture for specified user. If no profile pic is set, returns a default picture")
    @GetMapping("/pic/{username}")
    public ResponseEntity<?> getProfilePicture(@PathVariable String username) {

        Profile profile = profileServiceHandler.getUserProfile(username);

        if(profile.getProfileImageData() == null || profile.getProfileImageData().getName() == null){
            byte[] imgData = imageService.downloadDefaultImage();
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imgData);
        }else{
            byte[] imgData = imageService.downloadImage(profile.getProfileImageData().getName());
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imgData);
        }




    }
}
