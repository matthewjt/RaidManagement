package com.tomasov.raidmanagement.user;

/**
 * intended to be used when a password is attempted to be validated against a shard user and fails
 *
 * Created by seth on 1/11/2018.
 */
public class UnmatchedPasswordException extends Exception {
	public UnmatchedPasswordException(String message) {
		super(message);
	}

	public UnmatchedPasswordException(String message, Throwable cause) {
		super(message, cause);
	}
}
