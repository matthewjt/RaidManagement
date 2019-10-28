package com.tomasov.raidmanagement.host;

import org.parboiled.common.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.tomasov.raidmanagement.security.util.UserSessionHelper;
import com.tomasov.raidmanagement.user.AudiobookUser;
import com.tomasov.raidmanagement.user.UnmatchedPasswordException;
import com.tomasov.raidmanagement.user.UserRepository;

import java.util.Map;

/**
 * Entrypoing giving Users access to update operations that they can take on their own account
 *
 * Created by seth on 1/11/2018.
 */
@RestController
@RequestMapping("/rs/user")
public class UserEntrypoint {

	private static final Logger logger = LoggerFactory.getLogger(UserEntrypoint.class);

	private UserRepository userRepository;
	private UserSessionHelper userSessionHelper;

	@Autowired
	public UserEntrypoint(UserRepository userRepository, UserSessionHelper userSessionHelper) {
		this.userRepository = userRepository;
		this.userSessionHelper = userSessionHelper;
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody AudiobookUser getUser() {
		return userSessionHelper.getCurrentUser();
	}

	/**
	 * expects that the request body will contain a mapping with the old password and the new password
	 *
	 * @param request - { oldPassword: "", newPassword: ""}
	 * @return the updated user
	 * @throws UnmatchedPasswordException when the uesr's current password doesn't match the provided password
	 */
	@RequestMapping(value = "password", method = RequestMethod.PATCH,  consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AudiobookUser updatePassword(@RequestBody Map<String, String> request) throws UnmatchedPasswordException {
		String oldPassword = request.get("oldPassword");
		Preconditions.checkNotNull(oldPassword, "Old Password cannot be null.");

		String newPassword = request.get("newPassword");
		Preconditions.checkNotNull(newPassword, "New Password cannot be null.");

		Preconditions.checkNotNull(oldPassword);
		AudiobookUser currentUser = userSessionHelper.getCurrentUser();

		if (currentUser.validatePassword(oldPassword)) {
			logger.info("Updating {}'s password.", currentUser.getUserName());
			currentUser.hashAndSetPassword(newPassword);
			userRepository.addUpdateUser(currentUser);
		} else {
			logger.info("Failed to updated {}'s password. Old password is incorrect.", currentUser.getUserName());
			throw new UnmatchedPasswordException("Password provided does not match existing password.");
		}

		return currentUser;
	}
}
