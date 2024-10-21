package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.UserWeight;
import com.coms309.nutrifit.entity.WeightDTO;
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
        public List<UserWeight> getUserWeights(@PathVariable String username) {

           return bodyweightServiceHandler.getUserWeights(username);

        }

        //CREATE
        @PostMapping(path = "/{username}")
        public UserWeight addUserWeight(@PathVariable String username, @RequestBody WeightDTO weightDTO)
            {

                return bodyweightServiceHandler.addUserWeight(username, weightDTO.getWeight());

            }


        //READ reads by date in the body
        @GetMapping(path = "/{username}/date")
        public UserWeight getUserWeightByDate(@PathVariable String username, @RequestBody LocalDate date){
            return bodyweightServiceHandler.getWeightByDate(username, date);
        }

        //UPDATE
        @PutMapping(path = "/{username}/date")
        public UserWeight updateUserWeight(@PathVariable String username, @RequestBody UserWeight userWeight){
            return bodyweightServiceHandler.updateUserWeight(username, userWeight);
        }

        //DELETE
        @DeleteMapping(path = "/{username}/date")
        public String deleteUserWeight(@PathVariable String username, @RequestBody LocalDate date)
            {
                return bodyweightServiceHandler.deleteUserWeight(username, date);
            }


    }
