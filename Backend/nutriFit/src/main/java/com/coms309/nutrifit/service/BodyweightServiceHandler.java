package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.UserWeightDto;
import com.coms309.nutrifit.repo.BodyweightRepository;
import com.coms309.nutrifit.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class BodyweightServiceHandler
    {
        @Autowired
        private BodyweightRepository bodyweightRepository;

        @Autowired
        private UserRepository userRepository;

        public List<UserWeightDto> getUserWeights(String username)
            {
                User u = userRepository.findByUsername(username);
                if(u == null){
                    return null;
                }

               return bodyweightRepository.getAllByUserId(u.getId());


            }

        public UserWeightDto addUserWeight(String username, double weight)
            {
                User u = userRepository.findByUsername(username);

                LocalDate date = LocalDate.now();

                if(u == null || bodyweightRepository.existsByWeightDateAndUserId(date, u.getId())){
                    return null;
                }
                UserWeightDto userWeight = new UserWeightDto(weight, date, u);
                u.addBodyWeight(userWeight);
                userRepository.save(u);

              return   bodyweightRepository.getByWeightDateAndUserId(date, u.getId());
            }

        public UserWeightDto getWeightByDate(String username, LocalDate date)
            {
                User u = userRepository.findByUsername(username);
                if(u == null || date == null|| !bodyweightRepository.existsByWeightDateAndUserId (date, u.getId())){
                    return null;

                }

               return bodyweightRepository.getByWeightDateAndUserId(date, u.getId());
            }



        public UserWeightDto updateUserWeight(String username, UserWeightDto userWeightDto)
            {

                User u = userRepository.findByUsername(username);
                if(u == null || userWeightDto == null){
                    return null;
                }

                LocalDate date = userWeightDto.getWeightDate();

             UserWeightDto weight =   bodyweightRepository.getByWeightDateAndUserId(date, u.getId());
             weight.setWeight(userWeightDto.getWeight());
             bodyweightRepository.saveAndFlush(weight);
             return   bodyweightRepository.getByWeightDateAndUserId(date, u.getId());
            }

        public String deleteUserWeight(String username, LocalDate date)
            {
                User u = userRepository.findByUsername(username);
                if(u == null || date == null){
                    return "Couldn't find a weight for the username " + username +" on the date: " + date;
                }
               UserWeightDto weight =   bodyweightRepository.getByWeightDateAndUserId(date, u.getId());

                u.getBodyWeights().remove(weight);
                userRepository.saveAndFlush(u);

                bodyweightRepository.delete(weight);

                return "Weight deleted for the username " + username + " on the date: " + date;
            }
    }
