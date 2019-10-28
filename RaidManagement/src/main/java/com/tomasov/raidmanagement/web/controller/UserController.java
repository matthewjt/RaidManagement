package com.tomasov.raidmanagement.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.ImmutableMap;
import com.tomasov.raidmanagement.host.RaidEntrypoint;
import com.tomasov.raidmanagement.host.UserEntrypoint;
import com.tomasov.raidmanagement.raid.Raid;
import com.tomasov.raidmanagement.user.AudiobookUser;
import com.tomasov.raidmanagement.user.UnmatchedPasswordException;
import com.tomasov.raidmanagement.user.UserPreferenceManager;

@Controller
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private UserEntrypoint users;
	private RaidEntrypoint books;

	@Autowired
	public UserController(UserEntrypoint users, RaidEntrypoint books) {
		this.users = users;
		this.books = books;
	}

	@RequestMapping(value = "/secure/user", method = { RequestMethod.GET })
	public ModelAndView getUser(Model model) {

		AudiobookUser currentUser = users.getUser();

		model.addAttribute("user", currentUser);
		model.addAttribute("booksInFeed", getBookPreferenceListForUser(currentUser));

		return new ModelAndView("secure/user");
	}

	@RequestMapping(value = "/secure/user", method = { RequestMethod.POST })
	public ModelAndView updateUser(
			@RequestParam(required = false) String oldUserPassword,
			@RequestParam(required = false) String userPassword,
			Model model) throws UnmatchedPasswordException {

		ImmutableMap<String, String> requestBody = ImmutableMap.<String, String>builder().put("oldPassword", oldUserPassword).put("newPassword", userPassword).build();
		users.updatePassword(requestBody);

		return getUser(model);
	}

	// FIXME this should be part of the User object returned from the User entrypoint. The Controller shouldn't build this up.
	private ArrayList<Raid> getBookPreferenceListForUser(AudiobookUser user) {

		List<Long> bookKeys = UserPreferenceManager.allBookKeys(user);
		
		ArrayList<Raid> foundBooks = new ArrayList<>();

		for (Long bookKey : bookKeys) {
			// get the book and add it to the list
			if (bookKey != null) {
				Raid foundBook = books.findBook(bookKey);
				if (foundBook != null) {
					foundBooks.add(foundBook);
				}
			}
		}

		return foundBooks;
	}

}
