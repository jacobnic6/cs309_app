package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.fitness.UserMuscleProgress;
import com.coms309.nutrifit.entity.fitness.UserMuscleProgressDto;
import com.coms309.nutrifit.service.MuscleProgressService;
import com.coms309.nutrifit.service.UserServiceHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Muscle Progress Management")
@RestController
@RequestMapping("/muscle-progress")
public class MuscleProgressController {

    private final UserServiceHandler userServiceHandler;
    private final MuscleProgressService muscleProgressService;

    @Autowired
    public MuscleProgressController(UserServiceHandler userServiceHandler, MuscleProgressService muscleProgressService) {
        this.userServiceHandler = userServiceHandler;
        this.muscleProgressService = muscleProgressService;
    }

    @Operation(summary = "Creates a new Muscle Progress",
            description = "Takes input of a username and adds a muscle progress to the user. If progress already exists for that muscle, " +
                    "then progress amount is added to existing progress.")
    @PostMapping(path = "/{username}")
    public String createMuscleProgress(@RequestBody UserMuscleProgressDto progressDto , @PathVariable String username) throws IllegalAccessException {
        if(!userServiceHandler.existsByUsername(username)){
            return "User does not exist";
        }
        if(progressDto.getMuscle() == null){
            return "Muscle is null";
        }

        return muscleProgressService.createProgress(progressDto, username);
    }
    @Operation(summary = "Get all muscle progress for a user",
            description = "Takes input of a username and returns a list of all existing muscle progress for that user.")
    @GetMapping("/{username}")
    public List<UserMuscleProgress> getAllMuscleProgress(@PathVariable String username){
        if(!userServiceHandler.existsByUsername(username)){
            throw  new EntityNotFoundException("User does not exist");
        }
        return muscleProgressService.getAllUserProgress(username);

    }

    @Operation(summary = "Get a specific muscle's progress for a user",
            description = "Takes input of a username and name of a muscle and returns the progress for that muscle if it exists.")
    @GetMapping("muscle/{musclename}/{username}")
    public UserMuscleProgress getSpecificProgress(@PathVariable String musclename , @PathVariable String username){
        if(!userServiceHandler.existsByUsername(username)){
            throw  new EntityNotFoundException("User does not exist");
        }
        musclename = musclename.toUpperCase();
        return muscleProgressService.getByMuscle(musclename, username);

    }
}
