package com.tomasov.raidmanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Created by matt on 10/29/2019.
 */
@SpringBootApplication
@EnableWebSecurity
public class Application {

	public static class Modules {
		public final static String SCAN = "MODULE_SCAN";
	}

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
