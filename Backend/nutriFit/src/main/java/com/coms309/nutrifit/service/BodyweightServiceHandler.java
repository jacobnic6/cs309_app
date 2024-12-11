package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.User;
import com.coms309.nutrifit.entity.fitness.UserWeight;
import com.coms309.nutrifit.repo.BodyweightRepository;
import com.coms309.nutrifit.repo.ProfileRepository;
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

	private final BodyweightRepository bodyweightRepository;

	private final UserRepository userRepository;

	private final ProfileRepository profileRepository;

	private final UserServiceHandler userServiceHandler;

	private final ProfileServiceHandler profileServiceHandler;

	@Autowired
	public BodyweightServiceHandler(BodyweightRepository bodyweightRepository, UserRepository userRepository, ProfileRepository profileRepository, UserServiceHandler userServiceHandler, ProfileServiceHandler profileServiceHandler) {
		this.bodyweightRepository = bodyweightRepository;
		this.userRepository = userRepository;
		this.profileRepository = profileRepository;
		this.userServiceHandler = userServiceHandler;
		this.profileServiceHandler = profileServiceHandler;
	}

	/**
	 * Add user weight user weight.
	 *
	 * @param username   the username
	 * @param bodyWeight the body weight
	 *
	 * @return the user weight
	 */
	public UserWeight addUserWeight(String username, UserWeight bodyWeight) {
		double weight = bodyWeight.getWeight();
		if (weight <= 0)
		{
			throw new IllegalArgumentException("Weight must be greater than 0. Weight: " + weight + "");
		}
		if (!userRepository.existsByUsername(username))
		{
			throw new NullPointerException("User with username: " + username + " not found.");
		}
		User u = userRepository.findByUsername(username);
//		LocalDate date = bodyWeight.getWeightDate();
//		if (date == null)
//		{
//			date = LocalDate.now();
//			bodyWeight.setWeightDate(date);
//		}
//		if (bodyweightRepository.existsByWeightDateAndUser_Username(date, username))
//		{
//			bodyWeight = bodyweightRepository.findByWeightDateAndUser_Username(date, username);
//			bodyWeight.setWeight(weight);
//			return bodyweightRepository.saveAndFlush(bodyWeight);
//
//		}
		bodyWeight = validateBodyWeight(bodyWeight, username);

		bodyWeight.setUser(u);
		u.addUserWeight(bodyWeight);
		//userServiceHandler.addWeight(username, bodyWeight);
		bodyweightRepository.saveAndFlush(bodyWeight);
		double recent = getMostRecentWeight(username).getWeight();
		profileServiceHandler.updateWeight(username, recent);
		return bodyweightRepository.findByWeightDateAndUser_Username(bodyWeight.getWeightDate(), username);

	}

	private UserWeight validateBodyWeight(UserWeight bodyWeight, String username) {
		double weight = bodyWeight.getWeight();
		LocalDate date = bodyWeight.getWeightDate();
		if (date == null)
		{
			date = LocalDate.now();
			bodyWeight.setWeightDate(date);
		}
		if (bodyweightRepository.existsByWeightDateAndUser_Username(date, username))
		{
			bodyWeight = bodyweightRepository.findByWeightDateAndUser_Username(date, username);
			bodyWeight.setWeight(weight);

		}
		return bodyWeight;
	}

	public UserWeight getMostRecentWeight(String username) {
		List<UserWeight> weightList = getUserWeights(username);
		if (weightList == null || weightList.isEmpty())
		{
			return null;
		}
		return weightList.get(weightList.size() - 1);
	}

	/**
	 * Gets user weights.
	 *
	 * @param username the username
	 *
	 * @return the user weights
	 */
	public List<UserWeight> getUserWeights(String username) {
		User u = userRepository.findByUsername(username);
		if (u == null)
		{
			return null;
		}
		return bodyweightRepository.findByUser_UsernameOrderByWeightDateAsc(username);
	}

	/**
	 * Gets weight by date.
	 *
	 * @param username the username
	 * @param date     the date
	 *
	 * @return the weight by date
	 */
	public UserWeight getWeightByDate(String username, LocalDate date) {

		return bodyweightRepository.findByWeightDateAndUser_Username(date, username);
	}

	/**
	 * Update user weight user weight.
	 *
	 * @param username   the username
	 * @param userWeight the user weight
	 *
	 * @return the user weight
	 */
	public UserWeight updateUserWeight(String username, UserWeight userWeight) {
		User u = userRepository.findByUsername(username);
		LocalDate date = userWeight.getWeightDate();
		if (u == null || date == null || userWeight.getWeight() <= 0)
		{
			throw new IllegalArgumentException("Invalid input");
		}

		if (!bodyweightRepository.existsByWeightDateAndUser_Username(date, username))
		{
			userWeight.setUser(u);
			u.addUserWeight(userWeight);
			bodyweightRepository.saveAndFlush(userWeight);
			return userWeight;

		}

		UserWeight weight = bodyweightRepository.getByWeightDateAndUserId(date, u.getId());
		weight.setWeight(userWeight.getWeight());
		bodyweightRepository.saveAndFlush(weight);

		return bodyweightRepository.findByWeightDateAndUser_Username(date, username);
	}

	/**
	 * Delete user weight string.
	 *
	 * @param username the username
	 * @param date     the date
	 *
	 * @return the string
	 */
	public String deleteUserWeight(String username, LocalDate date) {
		User u = userRepository.findByUsername(username);
		if (u == null || date == null)
		{
			return "Couldn't find a weight for the username " + username + " on the date: " + date;
		}
		UserWeight weight = bodyweightRepository.getByWeightDateAndUserId(date, u.getId());
		u.getBodyWeights().remove(weight);
		userRepository.saveAndFlush(u);
		bodyweightRepository.delete(weight);
//        bodyweightRepository.deleteByWeightDateAndUser(date, u);
		return "Weight deleted for the username " + username + " on the date: " + date;
	}
}
