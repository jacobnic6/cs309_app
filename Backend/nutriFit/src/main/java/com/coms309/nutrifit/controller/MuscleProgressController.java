package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.UserMuscleProgress;
import com.coms309.nutrifit.entity.UserMuscleProgressDto;
import com.coms309.nutrifit.service.MuscleProgressService;
import com.coms309.nutrifit.service.UserServiceHandler;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    @GetMapping("/{username}")
    public List<UserMuscleProgress> getAllMuscleProgress(@PathVariable String username){
        if(!userServiceHandler.existsByUsername(username)){
            throw  new EntityNotFoundException("User does not exist");
        }
        return muscleProgressService.getAllUserProgress(username);

    }
}
