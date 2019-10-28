package com.tomasov.raidmanagement.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import com.tomasov.raidmanagement.configuration.ConfigurationException;
import com.tomasov.raidmanagement.configuration.ConfigurationManager;

@Component
public class CustomContextPathConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

	private static final Logger logger = LoggerFactory.getLogger(CustomContextPathConfig.class);

	@Autowired
	ConfigurationManager configurationManager;

	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		try {
			String contextPath = configurationManager.currentConfiguration().getOverrideUrl();

			if (contextPath != null && contextPath.trim().length() > 0 && !contextPath.equals("/")) {

				// Removed the Full URL requirement and stuck with just the context-root
				// let's talk about this some more later
//				String contextPath = extractContextPathFromUrl(overrideUrl);
//				if (contextPath != null  && contextPath.trim().length() > 0) {
				logger.info("Setting spring context path to override: '{}'", contextPath);
				factory.setContextPath(contextPath);
//				} else {
//					logger.info("override path context was /.  Not setting.");
//				}
			} else {
				logger.info("Using default spring context path.");
			}
		} catch (ConfigurationException configProblem) {
			logger.error("Problem getting configuration for setting of context path: {}", configProblem.getMessage(),
					configProblem);
		}

	}

	// assume something like "http://something/context".
	String extractContextPathFromUrl(String startUrl) {
		if (!(startUrl.toLowerCase().startsWith("http://") || startUrl.toLowerCase().startsWith("https://"))) {
			logger.info("StartUrl does not start with http or https '{}'", startUrl);
			return null;
		}

		String[] broken = startUrl.split("/");

		logger.info("Broken URL String:");
		for (String currentString : broken) {
			logger.info("     '{}'", currentString);
		}

		String extractedContext = "";
		if (broken.length >= 4) {
			for (int i = 3; i < broken.length; i++) {
				if (broken[i].length() > 0) {
					extractedContext = extractedContext + "/" + broken[i];
				}
			}
		}

		return extractedContext;
	}
}