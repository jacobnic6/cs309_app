package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.Profile;
import com.coms309.nutrifit.entity.fitness.UserMuscleProgress;
import com.coms309.nutrifit.entity.fitness.UserMuscleProgressDto;
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

	/**
	 * Instantiates a new Muscle progress service.
	 *
	 * @param profileServiceHandler        the profile service handler
	 * @param userMuscleProgressRepository the user muscle progress repository
	 * @param objectMapper                 the object mapper
	 * @param profileRepository            the profile repository
	 */
	@Autowired
	public MuscleProgressService(ProfileServiceHandler profileServiceHandler, UserMuscleProgressRepository userMuscleProgressRepository, ObjectMapper objectMapper, ProfileRepository profileRepository) {
		this.profileServiceHandler = profileServiceHandler;
		this.userMuscleProgressRepository = userMuscleProgressRepository;

		this.objectMapper = objectMapper;
		this.profileRepository = profileRepository;
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

			UserMuscleProgress progress = new UserMuscleProgress();
			String muscleName = progressDto.getMuscle().toUpperCase();
			UserMuscles muscle = UserMuscles.valueOf(muscleName);
			if (muscle != null)
			{
				progress = objectMapper.convertValue(progressDto, UserMuscleProgress.class);
				progress.setMuscle(progressDto.getMuscle().toUpperCase());
				progress.setProfile(profile);
				Map<String, UserMuscleProgress> progressMap = profile.getMuscleProgress();
				if (progressMap.containsKey(progress.getMuscle().toUpperCase()))
				{
					UserMuscleProgress existingProgress = progressMap.get(progress.getMuscle());
					existingProgress.updateProgress(progress.getPercentage(), progress.getTier());
					progress = existingProgress;
					profile.setMuscleProgress(progressMap);

				} else
				{
					progressMap.put(progress.getMuscle(), progress);
					profile.setMuscleProgress(progressMap);

				}

				userMuscleProgressRepository.save(progress);

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
