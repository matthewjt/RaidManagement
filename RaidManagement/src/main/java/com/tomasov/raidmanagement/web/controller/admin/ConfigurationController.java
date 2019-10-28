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

import com.tomasov.raidmanagement.configuration.ConfigurationException;
import com.tomasov.raidmanagement.host.ConfigurationEntrypoint;

@Controller
public class ConfigurationController {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);

	private ConfigurationEntrypoint configurationEntrypoint;

	@Autowired
	public ConfigurationController(ConfigurationEntrypoint configurationEntrypoint) {
		this.configurationEntrypoint = configurationEntrypoint;
	}

	@RequestMapping(value = "/secure/admin/configuration", method = { RequestMethod.GET })
	public ModelAndView viewConfiguration(Model model) {
		return new ModelAndView("secure/admin/configuration");
	}

	@RequestMapping(value = "/secure/admin/configuration/overrideURL", method = { RequestMethod.POST })
	public ModelAndView updateOverrideUrl(@RequestParam(value = "override-url") String overrideUrl, Model model) throws ConfigurationException {
		configurationEntrypoint.setOverrideUrl(overrideUrl);
		return new ModelAndView("secure/admin/configuration");
	}

	@RequestMapping(value = "/secure/admin/configuration/managedDirectories", method = { RequestMethod.POST })
	public ModelAndView updateDirectories(@RequestParam(value="directories") String bookDirectoriesCSV, Model model) throws ConfigurationException {
		configurationEntrypoint.updateDirectories(bookDirectoriesCSV);
		return new ModelAndView("secure/admin/configuration");
	}
}
