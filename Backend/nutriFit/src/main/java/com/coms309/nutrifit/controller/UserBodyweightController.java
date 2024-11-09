package com.coms309.nutrifit.controller;

import com.coms309.nutrifit.entity.UserWeight;
import com.coms309.nutrifit.entity.WeightDTO;
import com.coms309.nutrifit.service.BodyweightServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * The type User bodyweight controller.
 */
@RestController()
@RequestMapping("/bodyweights")
public class UserBodyweightController
    {
        @Autowired
       private BodyweightServiceHandler bodyweightServiceHandler;


        /**
         * Gets user weights.
         *
         * @param username the username
         * @return the user weights
         */
//LIST all of a specific user's weight
        @GetMapping(path = "/{username}")
        public List<UserWeight> getUserWeights(@PathVariable String username) {

           return bodyweightServiceHandler.getUserWeights(username);

        }

        /**
         * Add user weight user weight.
         *
         * @param username the username
         * @param weight   the weight
         * @return the user weight
         */
//CREATE
        @PostMapping(path = "/{username}")
        public UserWeight addUserWeight(@PathVariable String username, @RequestBody UserWeight weight)
            {

                return bodyweightServiceHandler.addUserWeight(username, weight);

            }


        /**
         * Get user weight by date user weight.
         *
         * @param username the username
         * @param weight   the weight
         * @return the user weight
         */
//READ reads by date in the body
        @GetMapping(path = "/{username}/date")
        public UserWeight getUserWeightByDate(@PathVariable String username, @RequestBody UserWeight weight){
            return bodyweightServiceHandler.getWeightByDate(username, weight.getWeightDate());
        }

        /**
         * Update user weight user weight.
         *
         * @param username   the username
         * @param userWeight the user weight
         * @return the user weight
         */
//UPDATE
        @PutMapping(path = "/{username}/date")
        public UserWeight updateUserWeight(@PathVariable String username, @RequestBody UserWeight userWeight){
            return bodyweightServiceHandler.updateUserWeight(username, userWeight);
        }

        /**
         * Delete user weight string.
         *
         * @param username the username
         * @param date     the date
         * @return the string
         */
//DELETE
        @DeleteMapping(path = "/{username}/date")
        public String deleteUserWeight(@PathVariable String username, @RequestBody LocalDate date)
            {
                return bodyweightServiceHandler.deleteUserWeight(username, date);
            }


    }
