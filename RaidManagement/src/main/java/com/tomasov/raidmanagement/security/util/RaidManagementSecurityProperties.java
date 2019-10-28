package com.tomasov.raidmanagement.security.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RaidManagementSecurityProperties {
	
	public static final String adminUserId = "admin";
	
	@Value("${com.tomasov.raidmanagement.security.adminUserPassword}")
	private String adminUserPassword;
	
	public static final String userId = "user";
	
	public static final String userPassword = "password";

	public String getAdminUserId() {
		return adminUserId;
	}

	public String getAdminUserPassword() {
		return adminUserPassword;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

}
