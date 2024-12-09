package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.ImageData;
import com.coms309.nutrifit.service.ImageService;
import com.coms309.nutrifit.service.ProfileServiceHandler;
import com.coms309.nutrifit.service.UserServiceHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * The type Image controller.
 */
@RestController
@Tag(name = "Image Management")
@RequestMapping("/images")
public class ImageController {

	/**
	 * The Image service.
	 */

	private final ProfileServiceHandler profileServiceHandler;

	private final UserServiceHandler userServiceHandler;

	/**
	 * The Image service.
	 */

	private final ImageService imageService;

	/**
	 * Instantiates a new Image controller.
	 *
	 * @param profileServiceHandler the profile service handler
	 * @param imageService          the image service
	 */
	@Autowired
	public ImageController(ProfileServiceHandler profileServiceHandler, UserServiceHandler userServiceHandler, ImageService imageService) {
		this.profileServiceHandler = profileServiceHandler;
		this.userServiceHandler = userServiceHandler;
		this.imageService = imageService;
	}

	/**
	 * Upload picture response entity.
	 *
	 * @param file     the file
	 * @param username the username
	 *
	 * @return the response entity
	 *
	 * @throws IOException the io exception
	 */
	@Operation(summary = "Upload an image to specific profile",
			description = "Uploads an image to the specified user's profile.")
	@PostMapping("/upload/{username}")
	public ResponseEntity<?> uploadPicture(@RequestParam(value = "image") MultipartFile file, @PathVariable String username) throws IOException {

		if (!userServiceHandler.existsByUsername(username))
		{
			throw new NullPointerException("User does not exist");
		}

		ImageData img = imageService.saveImage(file);
		if (img.getName() != null)
		{

			profileServiceHandler.assignImage(img, username);

			//profileServiceHandler.assignImage(img, username);
		}

		return ResponseEntity.status(HttpStatus.OK).body(img);
	}

	@Operation(summary = "Upload an image to specific profile",
			description = "Uploads an image to the specified user's profile.")
	@PostMapping("/default")
	public ResponseEntity<?> uploadDefaultPicture(@RequestParam(value = "image") MultipartFile file
	) throws IOException
	{

		ImageData img = imageService.saveImage(file);
		return ResponseEntity.status(HttpStatus.OK).body(img);
	}

	/**
	 * Download picture response entity.
	 *
	 * @param fileName the file name
	 *
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
	 *
	 * @return the response entity
	 */
	@Operation(summary = "Fetch profile image.",
			description = "Fetches profile picture for specified user. If no profile pic is set, returns a default picture")
	@GetMapping("/pic/{username}")
	public ResponseEntity<?> getProfilePicture(@PathVariable String username) {
		if (!userServiceHandler.existsByUsername(username))
		{
			throw new NullPointerException("User does not exist");
		}

		ImageData data = imageService.getProfilePicture(username);
		if (data != null)
		{
			byte[] imgData = imageService.downloadImage(data.getName());
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(data.getType())).body(imgData);
		} else
		{
			byte[] imgData = imageService.downloadDefaultImage();
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imgData);
		}

	}
}
