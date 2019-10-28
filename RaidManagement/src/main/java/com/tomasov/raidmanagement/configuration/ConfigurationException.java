package com.tomasov.raidmanagement.configuration;

/**
 * Created by seth on 10/18/15.
 */
public class ConfigurationException extends Exception {
    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
