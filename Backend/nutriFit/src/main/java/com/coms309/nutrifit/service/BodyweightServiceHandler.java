package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.UserWeight;
import com.coms309.nutrifit.repo.BodyweightRepository;
import com.coms309.nutrifit.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Bodyweight service handler.
 */
@Service
public class BodyweightServiceHandler extends ServiceHandler {
    @Autowired
    private BodyweightRepository bodyweightRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Gets user weights.
     *
     * @param username the username
     * @return the user weights
     */
    public List<UserWeight> getUserWeights(String username) {
        User u = userRepository.findByUsername(username);
        if (u == null) {
            return null;
        }
        return bodyweightRepository.getAllByUserId(u.getId());
    }

    /**
     * Add user weight user weight.
     *
     * @param username   the username
     * @param bodyWeight the body weight
     * @return the user weight
     */
    public UserWeight addUserWeight(String username, UserWeight bodyWeight) {
        double weight = bodyWeight.getWeight();
        User u = userRepository.findByUsername(username);
        LocalDate date = bodyWeight.getWeightDate();
        if (u == null || bodyweightRepository.existsByWeightDateAndUserId(date, u.getId())) {
            return null;
        }
        UserWeight userWeight = new UserWeight(weight, date, u);
        u.addUserWeight(userWeight);
        bodyweightRepository.save(userWeight);
        userRepository.save(u);
        return bodyweightRepository.getByWeightDateAndUserId(date, u.getId());
    }

    /**
     * Gets weight by date.
     *
     * @param username the username
     * @param date     the date
     * @return the weight by date
     */
    public UserWeight getWeightByDate(String username, LocalDate date) {
        User u = userRepository.findByUsername(username);
        if (u == null || date == null || !bodyweightRepository.existsByWeightDateAndUserId(date, u.getId())) {
            return null;
        }
        return bodyweightRepository.getByWeightDateAndUserId(date, u.getId());
    }

    /**
     * Update user weight user weight.
     *
     * @param username   the username
     * @param userWeight the user weight
     * @return the user weight
     */
    public UserWeight updateUserWeight(String username, UserWeight userWeight) {
        User u = userRepository.findByUsername(username);
        if (u == null || userWeight == null) {
            return null;
        }
        LocalDate date = userWeight.getWeightDate();
        UserWeight weight = bodyweightRepository.getByWeightDateAndUserId(date, u.getId());
        weight.setWeight(userWeight.getWeight());
        bodyweightRepository.saveAndFlush(weight);
        return bodyweightRepository.getByWeightDateAndUserId(date, u.getId());
    }

    /**
     * Delete user weight string.
     *
     * @param username the username
     * @param date     the date
     * @return the string
     */
    public String deleteUserWeight(String username, LocalDate date) {
        User u = userRepository.findByUsername(username);
        if (u == null || date == null) {
            return "Couldn't find a weight for the username " + username + " on the date: " + date;
        }
        UserWeight weight = bodyweightRepository.getByWeightDateAndUserId(date, u.getId());
        u.getBodyWeights().remove(weight);
        userRepository.saveAndFlush(u);
        bodyweightRepository.delete(weight);
        return "Weight deleted for the username " + username + " on the date: " + date;
    }
}
