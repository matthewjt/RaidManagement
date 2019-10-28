package com.tomasov.raidmanagement.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tomasov.raidmanagement.configuration.ConfigurationException;
import com.tomasov.raidmanagement.user.UnmatchedPasswordException;

import javax.servlet.http.HttpServletRequest;

/**
 * centralized location to handle exceptions thrown from the server and give a meaningful response to the client.
 *
 * it is also possible to have these exception handlers within the controllers themselves, that may make more sense
 * for some use cases.
 *
 * TODO determine what the response body for these errors should look like
 *
 * Created by seth on 10/18/15.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ConfigurationException.class)
    public String handleConfigurationException(HttpServletRequest req, ConfigurationException e) {
        logger.warn("Configuration Exception Occurred", e);
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UnmatchedPasswordException.class)
    public String handleUnmatchedPasswordException(HttpServletRequest req, UnmatchedPasswordException e) {
        return e.getMessage();
    }


}
