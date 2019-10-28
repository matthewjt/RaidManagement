package com.tomasov.raidmanagement.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomasov.raidmanagement.configuration.ConfigurationException;
import com.tomasov.raidmanagement.security.util.UserSessionHelper;
import com.tomasov.raidmanagement.user.AudiobookUser;
import com.tomasov.raidmanagement.user.UserPreference;
import com.tomasov.raidmanagement.user.UserPreferenceManager;
import com.tomasov.raidmanagement.user.UserRepository;

/**
 * Created by matt on 10/30/15.
 */

@RestController
@RequestMapping("/rs/user/preference")
public class UserPreferencesEntrypoint {

	private static final Logger logger = LoggerFactory.getLogger(UserPreferencesEntrypoint.class);

	private UserRepository userRepository;
	private UserSessionHelper userSessionHelper;

	@Autowired
	public UserPreferencesEntrypoint(UserRepository userRepository, UserSessionHelper userSessionHelper) {
		this.userRepository = userRepository;
		this.userSessionHelper = userSessionHelper;
	}

	/* **********
	 * 
	 * User Endpoints
	 * 
	 * ********
	 */

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public AudiobookUser addUserPref(@RequestParam String preferenceName, @RequestParam String preferenceValue) throws ConfigurationException {

		logger.debug("Adding preference '{}', '{}'", preferenceName, preferenceValue);

		AudiobookUser user = userSessionHelper.getCurrentUser();

		UserPreference shardUserPreference = UserPreference.fromValue(preferenceName);
		if (shardUserPreference != null) {
			user = UserPreferenceManager.addPreference(user, shardUserPreference, preferenceValue);
			user = this.userRepository.addUpdateUser(user);
		} else {
			logger.error("Tried to add preference but name does not resolve: '{}', '{}'", preferenceName, preferenceValue);
		}

		return user;
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public AudiobookUser removeUserPref(@RequestParam String preferenceName) throws ConfigurationException {

		logger.debug("Removing preference '{}'", preferenceName);

		AudiobookUser user = userSessionHelper.getCurrentUser();

		UserPreference shardUserPreference = UserPreference.fromValue(preferenceName);
		if (shardUserPreference != null) {
			user.clearPreference(shardUserPreference);
			user = this.userRepository.addUpdateUser(user);
		} else {
			logger.error("Tried to add preference but name does not resolve: '{}'", preferenceName);
		}

		return user;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String listUserPref(@RequestParam String preferenceName) throws ConfigurationException {

		logger.debug("Getting preference '{}',", preferenceName);

		AudiobookUser user = userSessionHelper.getCurrentUser();

		UserPreference shardUserPreference = UserPreference.fromValue(preferenceName);
		if (shardUserPreference != null) {
			return user.getPreference(shardUserPreference);
		} else {
			logger.error("Tried to add preference but name does not resolve: '{}',", preferenceName);
		}

		return "";
	}

	@RequestMapping(value = "/addBook", method = RequestMethod.POST)
	public AudiobookUser addBookUserPref(@RequestParam Long bookKey) throws ConfigurationException {

		logger.info("Adding book to book preference '{}'", bookKey);

		AudiobookUser user = userSessionHelper.getCurrentUser();

		logger.info("     User '{}', BookKey '{}'", user, bookKey);
		
		user = UserPreferenceManager.addBook(user, bookKey);

		user = this.userRepository.addUpdateUser(user);
		
		logger.trace("    Updated user after adding preference: {}", user);

		return user;
	}

	@RequestMapping(value = "/removeBook", method = RequestMethod.POST)
	public AudiobookUser removeBookUserPref(@RequestParam Long bookKey) throws ConfigurationException {

		logger.debug("Removing book to book preference '{}'", bookKey);

		AudiobookUser user = userSessionHelper.getCurrentUser();

		user = UserPreferenceManager.removeBook(user, bookKey);

		user = this.userRepository.addUpdateUser(user);

		return user;
	}

}
