package com.tomasov.raidmanagement.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * this exception handler will catch exceptions that are thrown within asynchronous methods that have a "void" return
 * type
 *
 * Created by seth on 1/10/2018.
 */
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
	@Override
	public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
		Logger logger = LoggerFactory.getLogger(method.getDeclaringClass());
		logger.error("Exception thrown from {}.{}.", method.getDeclaringClass(), method.getName());
		logger.error(throwable.getMessage());
	}
}
