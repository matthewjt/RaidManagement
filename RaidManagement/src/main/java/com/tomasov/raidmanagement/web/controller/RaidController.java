package com.tomasov.raidmanagement.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller used to handle Raids
 *
 */
@Controller
public class RaidController {
	private static final Logger logger = LoggerFactory.getLogger(RaidController.class);

	public RaidController() {
		
	}

	@RequestMapping("/raidDetails/{id}")
	public String book(@PathVariable(value = "id", required = true) Long bookId, Model model) {

		logger.trace("Viewing raid details for raid id {}", bookId);
		
		return "raidDetails";
	}


}
