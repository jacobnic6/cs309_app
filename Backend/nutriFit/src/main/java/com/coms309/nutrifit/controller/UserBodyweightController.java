package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.UserWeightDto;
import com.coms309.nutrifit.service.BodyweightServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping("/bodyweights")
public class UserBodyweightController
    {
        @Autowired
       private BodyweightServiceHandler bodyweightServiceHandler;




        //LIST all of a specific user's weight
        @GetMapping(path = "/{username}")
        public List<UserWeightDto> getUserWeights(@PathVariable String username) {

           return bodyweightServiceHandler.getUserWeights(username);

        }

        //CREATE
        @PostMapping(path = "/{username}")
        public UserWeightDto addUserWeight(@PathVariable String username, @RequestBody double weight)
            {

                return bodyweightServiceHandler.addUserWeight(username, weight);

            }


        //READ reads by date in the body
        @GetMapping(path = "/{username}/date")
        public UserWeightDto getUserWeightByDate(@PathVariable String username, @RequestBody LocalDate date){
            return bodyweightServiceHandler.getWeightByDate(username, date);
        }

        //UPDATE
        @PutMapping(path = "/{username}/date")
        public UserWeightDto updateUserWeight(@PathVariable String username, @RequestBody UserWeightDto userWeightDto){
            return bodyweightServiceHandler.updateUserWeight(username, userWeightDto);
        }

        //DELETE
        @DeleteMapping(path = "/{username}/date")
        public String deleteUserWeight(@PathVariable String username, @RequestBody LocalDate date)
            {
                return bodyweightServiceHandler.deleteUserWeight(username, date);
            }


    }
