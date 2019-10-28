package com.tomasov.raidmanagement.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tomasov.raidmanagement.security.util.RaidManagementSecurityProperties;
import com.tomasov.raidmanagement.user.RaidManagementRole;
import com.tomasov.raidmanagement.user.AudiobookUser;
import com.tomasov.raidmanagement.user.UserRepository;

import java.util.Set;

/**
 * TODO review admin update logic. as well as admin initial creation
 * <p>
 * Created by matt on 10/30/15.
 */

@RestController
@RequestMapping("/rs/admin/user")
public class UserAdministrationEntrypoint {

	private static final Logger logger = LoggerFactory.getLogger(UserAdministrationEntrypoint.class);
	public static final String ADMIN_USER_ID = RaidManagementSecurityProperties.adminUserId;

	private UserRepository userRepository;

	@Autowired
	public UserAdministrationEntrypoint(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/* **********
	 *
	 * User Endpoints
	 *
	 * *********/

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody
	AudiobookUser addUser(@RequestBody AudiobookUser user) {
		logger.info("Adding user '{}'", user);
		if (ADMIN_USER_ID.equalsIgnoreCase(user.getUserName())) {
			logger.warn("Ignoring attempt to remove ADMIN role from {}.", ADMIN_USER_ID);
			if (!user.isMember(RaidManagementRole.ADMIN)) {
				user.addRole(RaidManagementRole.ADMIN);
			}
		}

		userRepository.addUpdateUser(user);
		return userRepository.getUser(user.getUserName());
	}

	@RequestMapping(value = "update", method = RequestMethod.PUT)
	public @ResponseBody
	AudiobookUser updateUser(@RequestBody AudiobookUser user) {
		logger.info("Updating user '{}'", user);
		return addUser(user);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	Set<AudiobookUser> listUsers() {
		logger.trace("Getting all users");
		return userRepository.getAllUsers();
	}

	@RequestMapping(value = "/{userName}", method = RequestMethod.GET)
	public @ResponseBody
	AudiobookUser getUser(@PathVariable String userName) {
		logger.trace("Getting user {}", userName);
		return userRepository.getUser(userName);
	}

	@RequestMapping(value = "/{userName}", method = RequestMethod.DELETE)
	public @ResponseBody
	String removeUser(@PathVariable String userName) {
		if (userName.equalsIgnoreCase(ADMIN_USER_ID)) {
			logger.warn("Ignoring attempt to remove {}.", ADMIN_USER_ID);
			return "Ignored attempt";
		} else {
			logger.trace("Removing user '{}'", userName);
			userRepository.removeUserByName(userName);
			return String.format("User %s removed.", userName);
		}
	}

}
