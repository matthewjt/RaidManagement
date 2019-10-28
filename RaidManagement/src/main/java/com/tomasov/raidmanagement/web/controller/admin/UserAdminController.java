package com.tomasov.raidmanagement.web.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tomasov.raidmanagement.host.UserAdministrationEntrypoint;
import com.tomasov.raidmanagement.user.RaidManagementRole;
import com.tomasov.raidmanagement.user.AudiobookUser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * The User Admin Controller communicates with the User Entrypoint to retrieve and update user preferences and configuration.
 */
@Controller
public class UserAdminController {

	private static final Logger logger = LoggerFactory.getLogger(UserAdminController.class);

	private final UserAdministrationEntrypoint entrypoint;

	@Autowired
	public UserAdminController(UserAdministrationEntrypoint entrypoint) {
		this.entrypoint = entrypoint;
	}

	@RequestMapping(value = "/secure/admin", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView secureIndex() {
		return new ModelAndView("secure/admin/index");
	}

	@RequestMapping(value = "/secure/admin/users", method = { RequestMethod.GET })
	public ModelAndView users(Model model) {

		Set<AudiobookUser> shardUsers = entrypoint.listUsers();

		model.addAttribute("allUsers", shardUsers);
		return new ModelAndView("secure/admin/users");
	}

	@RequestMapping(value = "/secure/admin/user", method = { RequestMethod.GET })
	public ModelAndView getUser(@RequestParam(required=false) String userId, Model model) {
		AudiobookUser shardUser;

		if (null != userId) {
			shardUser = entrypoint.getUser(userId);
		} else {
			shardUser = new AudiobookUser(null);
		}

		model.addAttribute("allRoles", Arrays.asList(RaidManagementRole.values()));
		model.addAttribute("selectedRoles", shardUser.getRoles());
		model.addAttribute("user", shardUser);

		return new ModelAndView("secure/admin/user");
	}

	/**
	 * FIXME there is some logic here that needs to move to the server side entrypoint.
	 * @param userId - user to add/update
	 * @param userPassword - new user password
	 * @param deleteUser - delete user flag
	 * @param selectedRoles - roles to apply to the user
	 * @param model - view model
	 * @return the view model
	 */
	@RequestMapping(value = "/secure/admin/user", method = { RequestMethod.POST })
	public ModelAndView updateUser(
					@RequestParam(required=false) String userId,
					@RequestParam(required=false) String userPassword,
					@RequestParam(required = false) String deleteUser,
					@RequestParam(required = false) List<RaidManagementRole> selectedRoles,
					Model model) {

		userId = userId.toLowerCase();

		if (deleteUser != null && deleteUser.length() > 0) {
			logger.debug("Deleting user: {}", userId);
			entrypoint.removeUser(userId);
		} else {
			AudiobookUser shardUser = entrypoint.getUser(userId);

			if (shardUser == null) {
				logger.debug("Creating new user: {}", userId);
				shardUser = new AudiobookUser(userId);
			}

			logger.debug("Selected roles {} - {}", (selectedRoles != null ? selectedRoles.size() : -1), selectedRoles);
			shardUser.setRoles(selectedRoles);
			if (userPassword != null && userPassword.trim().length() > 0) {
				shardUser.hashAndSetPassword(userPassword.trim());
			}

			entrypoint.addUser(shardUser);
		}

		return users(model);
	}
}
