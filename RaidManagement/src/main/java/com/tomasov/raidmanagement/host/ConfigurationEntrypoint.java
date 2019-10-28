package com.tomasov.raidmanagement.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tomasov.raidmanagement.configuration.ConfigurationException;
import com.tomasov.raidmanagement.configuration.ConfigurationManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 *
 * Created by seth on 10/18/15.
 */
@RestController
@RequestMapping("/rs/config")
public class ConfigurationEntrypoint {
	private final ConfigurationManager config;

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationEntrypoint.class);

	@Autowired
	public ConfigurationEntrypoint(ConfigurationManager config) {
		this.config = config;
	}

		/* **********
		 *
		 * Directory Endpoints
		 *
		 * *********/

	@RequestMapping(value = "/directory/add", method = RequestMethod.POST)
	public @ResponseBody Set<String> addManagedDirectory(@RequestBody String directory) throws ConfigurationException {
		logger.info("Adding directory '{}'", directory);
		return config.addManagedDirectory(directory);
	}

	@RequestMapping(value = "/directory/list", method = RequestMethod.GET)
	public @ResponseBody Set<String> listManagedDirectories() throws ConfigurationException {
		logger.info("Retrieving Managed Directories");
		return config.listManagedDirectories();
	}

	@RequestMapping(value = "/directory/remove", method = RequestMethod.POST)
	public @ResponseBody Set<String> removeManagedDirectory(@RequestBody String directory) throws ConfigurationException {
		logger.info("Removing directory '{}'", directory);
		return config.removeManagedDirectory(directory);
	}

	/**
	 * this method replaces the current managed directories with the directories supplied by the user in the
	 * bookDirectoriesCSV parameter.
	 *
	 * @param bookDirectoriesCSV - a comma-separated string that contains the directories the user wishes to become managed directories
	 * @return the end result of the operation on the current managed directories
	 * @throws ConfigurationException when operation is unable to be completed
	 */
	@RequestMapping(value = "/directory/update", method = { RequestMethod.POST })
	public @ResponseBody Set<String> updateDirectories(@RequestParam(value="directories") String bookDirectoriesCSV) throws ConfigurationException {

		Set<String> currentDirectories = listManagedDirectories();

		if (bookDirectoriesCSV == null || bookDirectoriesCSV.trim().length() == 0) {
			// nothing to do just return current list
			throw new ConfigurationException("No directories supplied.");
		}

		List<String> newDirectories = Arrays.asList(bookDirectoriesCSV.split(","));
		if (newDirectories.isEmpty()) {
			throw new ConfigurationException("No directories supplied.");
		}

		logger.info("Updating managed directories with {}", bookDirectoriesCSV);
		try {
			clearManagedDirectories(currentDirectories);
		} catch (ConfigurationException e) {
			logger.error("Unable to clear current directories.", e);
			throw new ConfigurationException("Unable to clear current directories." ,e);
		}

		try {
			addManagedDirectories(newDirectories);
		} catch (ConfigurationException e) {
			clearManagedDirectories(newDirectories);
			addManagedDirectories(currentDirectories);
			throw e;
		}

		return listManagedDirectories();
	}

	private void clearManagedDirectories(Collection<String> currentDirectories) throws ConfigurationException {
		for (String currentDirectory : currentDirectories) {
			removeManagedDirectory(currentDirectory);
		}
	}

	private void addManagedDirectories(Collection<String> directories) throws ConfigurationException {
		for (String directory: directories) {
			addManagedDirectory(directory);
		}
	}


		/* **********
		 *
		 * Override URL Endpoints
		 *
		 * *********/

	@RequestMapping(value = "/overrideurl", method = RequestMethod.PUT)
	public @ResponseBody String setOverrideUrl(@RequestParam(value="overrideUrl") String url) throws ConfigurationException {
		logger.info("Updating override url '{}'", url);
		config.setOverrideUrl(url);
		return config.getOverrideUrl();
	}

	@RequestMapping(value = "/overrideurl", method = RequestMethod.DELETE)
	public @ResponseBody String clearOverrideUrl() throws ConfigurationException {
		logger.info("Clearing override url '{}'", config.getOverrideUrl());
		config.setOverrideUrl("/");
		return config.getOverrideUrl();
	}

	@RequestMapping(value = "/overrideurl", method = RequestMethod.GET)
	public @ResponseBody String getOverrideUrl() throws ConfigurationException {
		logger.info("Retrieving override URL");
		return config.getOverrideUrl();
	}
}
