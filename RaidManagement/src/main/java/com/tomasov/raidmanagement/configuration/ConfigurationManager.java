package com.tomasov.raidmanagement.configuration;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.tomasov.raidmanagement.util.PropertyUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

/**
 * Created by seth on 10/18/15.
 */
@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConfigurationManager {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);

	public Configuration currentConfiguration() throws ConfigurationException {
		try {
			File configFile = safeOpenFile();
			return safeGetConfiguration(configFile);
		} catch (IOException e) {
			throw new ConfigurationException("Unable to open configuration file.", e);
		}
	}

	private Configuration safeGetConfiguration(File configFile) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(configFile, Configuration.class);
		} catch (JsonMappingException e) {
			logger.warn("Unable to map config file. Possibly a new configuration. Return empty configuration instead.");
			return new Configuration();
		}
	}

	public void saveConfiguration(Configuration config) throws ConfigurationException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(safeOpenFile(), config);
		} catch (IOException e) {
			throw new ConfigurationException("Unable to save configuration file", e);
		}
	}

	// TODO this is really similar to the code that is in the BookRespository, should consolidate
	private static File safeOpenFile() throws IOException {
		Path path = Paths.get(PropertyUtil.getConfigPath() + "/config");

		if (!Files.exists(path)) {
			Files.createDirectories(path.getParent());
			Files.createFile(path);
			logger.debug("Creating {}", path.toAbsolutePath());
		}

		return path.toFile();
	}

	/**
	 * adds the new managed directory to the configuration ignores duplicates
	 * <p>
	 * if the string cannot be converted to a valid Path then an exception will be thrown.
	 *
	 * @param managedDirectory - a directory that could contain books that should be added to the library
	 * @return - the new set of managed directories
	 */
	public Set<String> addManagedDirectory(String managedDirectory) throws ConfigurationException {
		Configuration config = this.currentConfiguration();

		if (!Files.exists(Paths.get(managedDirectory))) {
			throw new ConfigurationException("Path " + managedDirectory + " does not exist on the server.");
		}

		Set<String> directories = Sets.newHashSet(config.getDirectories());
		directories.add(managedDirectory);
		config.setDirectories(directories);

		saveConfiguration(config);
		return directories;
	}

	/**
	 * removes the existing managed directory to the configuration ignores duplicates
	 * <p>
	 * if the string cannot be converted to a valid Path then an exception will be thrown.
	 *
	 * @param managedDirectory - the directory that should be removed from the library
	 * @return - the new set of managed directories
	 */
	public Set<String> removeManagedDirectory(String managedDirectory) throws ConfigurationException {
		Configuration config = this.currentConfiguration();

		Set<String> directories = Sets.newHashSet(config.getDirectories());
		directories.remove(managedDirectory);
		config.setDirectories(directories);

		saveConfiguration(config);
		return directories;
	}

	/**
	 * Lists all the current existing managed directories
	 *
	 * @return - the set of managed directories
	 */
	public Set<String> listManagedDirectories() throws ConfigurationException {
		Configuration config = this.currentConfiguration();
		return Sets.newHashSet(config.getDirectories());
	}

	public String listManagedDirectoriesCSV() throws ConfigurationException {
		return StringUtils.join(listManagedDirectories(), ",");
	}

	/**
	 * adds the new managed directory to the configuration ignores duplicates
	 * <p>
	 * if the string cannot be converted to a valid Path then an exception will be thrown.
	 *
	 * @param overrideUrl - the new url that the webapp will be accessible through
	 * @return - the new set of managed directories
	 */
	public void setOverrideUrl(String overrideUrl) throws ConfigurationException {
		if (!overrideUrl.trim().startsWith("/"))
			throw new ConfigurationException("Override URL must start with a '/': '" + overrideUrl + "'");

		Configuration config = this.currentConfiguration();

		config.setOverrideUrl(overrideUrl);

		saveConfiguration(config);
	}

	/**
	 * adds the new managed directory to the configuration ignores duplicates
	 * <p>
	 * if the string cannot be converted to a valid Path then an exception will be thrown.
	 *
	 * @return - the new set of managed directories
	 */
	public String getOverrideUrl() throws ConfigurationException {
		Configuration config = this.currentConfiguration();
		return config.getOverrideUrl();
	}
	
	public void setUsingDocker(Boolean usingDocker) throws ConfigurationException {
		Configuration config = this.currentConfiguration();
		config.setUsingDocker(usingDocker != null ? usingDocker : false);
		saveConfiguration(config);
	}

	public Boolean getUsingDocker() throws ConfigurationException {
		Configuration config = this.currentConfiguration();
		Boolean usingDocker = config.getUsingDocker();

		return (usingDocker != null ? usingDocker : false);
	}

	public List<String> addDirectoryPattern(String pattern) throws ConfigurationException {

		Configuration config = this.currentConfiguration();

		List<String> patterns = config.getPatterns();
		patterns.add(pattern);
		config.setPatterns(patterns);

		saveConfiguration(config);
		return patterns;

	}

	public List<String> removeDirectoryPattern(String pattern) throws ConfigurationException {
		Configuration config = this.currentConfiguration();

		List<String> patterns = config.getPatterns();
		patterns.remove(pattern);
		config.setPatterns(patterns);

		saveConfiguration(config);
		return patterns;
	}
}
