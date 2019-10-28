package com.tomasov.raidmanagement.util;

public class PropertyUtil {

	public static final String getConfigPath() {
		String configDir = System.getProperty("library_shard.config.dir");
		if (configDir == null || configDir.trim().length() == 0) {
			configDir = System.getProperty("user.home") + "/.raidmanagement";
		}

		return configDir;
	}
}
