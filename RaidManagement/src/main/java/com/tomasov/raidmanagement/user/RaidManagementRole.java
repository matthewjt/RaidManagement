package com.tomasov.raidmanagement.user;

import java.util.ArrayList;
import java.util.List;

public enum RaidManagementRole {

	MEMBER("MEMBER"),
	RAIDER("RAIDER"),
	GUILDMASTER("GUILDMASTER"),
	ADMIN("ADMIN")
	;

	private String roleName;

	RaidManagementRole(String roleName) {
		this.roleName = roleName;
	}
	
	public String getRoleName() {
		return this.roleName;
	}

	public String toString() {
		return this.roleName;
	}
	
	public static List<String> getAllRoleNames(){
		List<String> allRoles = new ArrayList<>();
		for (RaidManagementRole shardRole : values()) {
			allRoles.add(shardRole.getRoleName());
		}
		return allRoles;
	}
	
}
