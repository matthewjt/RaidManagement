package com.tomasov.raidmanagement.security;

import org.springframework.security.core.authority.AuthorityUtils;

import com.tomasov.raidmanagement.user.RaidManagementRole;
import com.tomasov.raidmanagement.user.AudiobookUser;

import java.util.ArrayList;
import java.util.List;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

	private AudiobookUser user;

	public CurrentUser(AudiobookUser user) {

		super(user.getUserName(), user.getHashedPassword(), AuthorityUtils.createAuthorityList(getRoleList(user.getRoles())));
		this.user = user;
		
	}

	public String getUser() {
		return user.getUserName();
	}
	
	public static String[] getRoleList(List<RaidManagementRole> roles){
		
		List<String> roleNames = new ArrayList<String>();
		
		if (roles!=null){
			for (RaidManagementRole role : roles) {
				roleNames.add(role.getRoleName());
			}
		}
		
		return roleNames.toArray(new String[roleNames.size()]);
	}

}