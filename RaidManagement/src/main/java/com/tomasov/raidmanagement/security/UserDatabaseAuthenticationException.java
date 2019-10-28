package com.tomasov.raidmanagement.security;

import org.springframework.security.core.AuthenticationException;

public class UserDatabaseAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public UserDatabaseAuthenticationException(String msg) {
		super(msg);
	}

}
