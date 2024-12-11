package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.fitness.UserMuscleProgress;
import com.coms309.nutrifit.entity.fitness.UserMuscleProgressDto;
import com.coms309.nutrifit.exercises.Muscle;
import com.coms309.nutrifit.repo.MuscleGroupRepository;
import com.coms309.nutrifit.repo.MuscleRepository;
import com.coms309.nutrifit.repo.ProfileRepository;
import com.coms309.nutrifit.repo.UserMuscleProgressRepository;
import com.coms309.nutrifit.util.UserMuscles;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * The type Muscle progress service.
 */
@Service
public class MuscleProgressService {

	private final ProfileServiceHandler profileServiceHandler;

	private final UserMuscleProgressRepository userMuscleProgressRepository;

	private final ObjectMapper objectMapper;

	private final ProfileRepository profileRepository;

	private final MuscleRepository muscleRepository;

	/**
	 * Instantiates a new Muscle progress service.
	 *
	 * @param profileServiceHandler        the profile service handler
	 * @param userMuscleProgressRepository the user muscle progress repository
	 * @param objectMapper                 the object mapper
	 * @param profileRepository            the profile repository
	 */
	@Autowired
	public MuscleProgressService(ProfileServiceHandler profileServiceHandler, UserMuscleProgressRepository userMuscleProgressRepository, MuscleGroupRepository muscleGroupRepository, ObjectMapper objectMapper, ProfileRepository profileRepository, MuscleRepository muscleRepository) {
		this.profileServiceHandler = profileServiceHandler;
		this.userMuscleProgressRepository = userMuscleProgressRepository;

		this.objectMapper = objectMapper;
		this.profileRepository = profileRepository;
		this.muscleRepository = muscleRepository;
	}

	/**
	 * Create progress string.
	 *
	 * @param progressDto the progress dto
	 * @param username    the username
	 *
	 * @return the string
	 *
	 * @throws IllegalAccessException the illegal access exception
	 */
	public String createProgress(UserMuscleProgressDto progressDto, String username) throws IllegalAccessException {

		Profile profile = profileServiceHandler.getUserProfile(username);
		if (profile == null)
		{
			profile = profileServiceHandler.createProfileByName(username);
		}
		try
		{

			UserMuscleProgress progress = null;
			String muscleName = progressDto.getMuscle().toUpperCase();
			Muscle muscle = muscleRepository.findByName(muscleName);

			if (muscle != null)
			{
				progress = objectMapper.convertValue(progressDto, UserMuscleProgress.class);
				progress.setMuscle(muscle.getName());
				progress.setProfile(profile);
				Map<String, UserMuscleProgress> progressMap = profile.getMuscleProgress();
				if (progressMap.containsKey(muscle.getName()))
				{
					UserMuscleProgress existingProgress = progressMap.get(progress.getMuscle());

					updateProgress(progress.getTotalProgress(), existingProgress);
					// existingProgress.updateProgress(progress.getPercentage());
					progress = existingProgress;
					profile.setMuscleProgress(progressMap);

				} else
				{
					UserMuscleProgress newProgress = checkValues(progress);
					progressMap.put(newProgress.getMuscle(), newProgress);
					profile.setMuscleProgress(progressMap);

				}

				profileRepository.saveAndFlush(profile);

				return "Muscle " + progress.getMuscle() + " progress created";
			}

			return "muscle progress not created";
		}
		catch (IllegalArgumentException e)
		{
			String msg = "invalid muscle name. Muscle must be one of: ";
			for (UserMuscles muscle : UserMuscles.values())
			{
				msg += muscle.toString() + ", ";
			}
			return msg;
		}

	}

	public UserMuscleProgress updateProgress(double progressAmount, UserMuscleProgress muscleProgress) {
		//muscleProgress = checkValues(muscleProgress);
		int currentTier = muscleProgress.getTier();
		double currentTotal = muscleProgress.getTotalProgress();

		double nextTierAt = muscleProgress.getAmountToNextTier();
//		double prevTierAt = calcPrevTierAmount(currentTier);

		currentTotal += progressAmount;
		muscleProgress.setTotalProgress(currentTotal);

		if (currentTotal >= nextTierAt)
		{
			currentTier += 1;
//			prevTierAt = nextTierAt;
//			nextTierAt = calcNextTierAmount(currentTier);

			muscleProgress.setTier(currentTier);

		}
		muscleProgress.setAmountToNextTier(calcNextTierAmount(currentTier) - currentTotal);
		muscleProgress.setPercentage(calcTierPercentage(currentTotal, currentTier));

		//tier = tier * 1.2 * 100
		return muscleProgress;
	}

	public UserMuscleProgress checkValues(UserMuscleProgress muscleProgress) {
		if (muscleProgress.getTotalProgress() >= muscleProgress.getAmountToNextTier())
		{
			muscleProgress.setTier(muscleProgress.getTier() + 1);
			muscleProgress.setAmountToNextTier(calcNextTierAmount(muscleProgress.getTier()) - muscleProgress.getTotalProgress());
			muscleProgress.setPercentage(calcTierPercentage(muscleProgress.getTotalProgress(), muscleProgress.getTier()));
		}
		return muscleProgress;
	}

	private double calcNextTierAmount(int tier) {

		if (tier == 0)
		{
			return 100;
		}
		return (tier * 1.2 * 100) + 100;
	}

	private double calcTierPercentage(double currentTotal, int currentTier) {

		if (currentTier == 0)
		{
			return currentTotal;
		}
		double tierProgress = currentTotal - calcPrevTierAmount(currentTier);
		double tierDifference = findTierAmountDifference(currentTier);
		double tierPercentage = (tierProgress / tierDifference) * 100;

		return tierPercentage;
	}

	private double calcPrevTierAmount(int tier) {

		if (tier == 0)
		{
			return 0;
		}
		return ((double) (tier - 1) * 1.2 * 100) + 100;
	}

	private double findTierAmountDifference(int tier) {
		if (tier == 0)
		{
			return 100;
		}
		return calcNextTierAmount(tier) - calcPrevTierAmount(tier);

	}

	/**
	 * Gets all user progress.
	 *
	 * @param username the username
	 *
	 * @return the all user progress
	 */
	public List<UserMuscleProgress> getAllUserProgress(String username) {
		return userMuscleProgressRepository.findAllByProfile_Name(username);
	}

	/**
	 * Gets by muscle.
	 *
	 * @param musclename the musclename
	 * @param username   the username
	 *
	 * @return the by muscle
	 */
	public UserMuscleProgress getByMuscle(String musclename, String username) {
		return userMuscleProgressRepository.findUserMuscleProgressByProfile_NameAndAndMuscle(username, musclename);
	}
}
