package com.tomasov.raidmanagement.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tomasov.raidmanagement.user.RaidManagementRole;
import com.tomasov.raidmanagement.user.AudiobookUser;
import com.tomasov.raidmanagement.user.UserRepository;

/**
 * removed hidden spring dependency and clearly declared it. can no longer use this as a inline singleton
 */
@Service(value="userSessionHelper")
public class UserSessionHelper {

	UserRepository userRepository;

	@Autowired
	public UserSessionHelper(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public AudiobookUser getCurrentUser() {
		AudiobookUser user = null;
		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			String principal = SecurityContextHolder.getContext().getAuthentication().getName();

			if (principal != null) {
				user = userRepository.getUser(principal);
			}
		}

		return user;
	}

	public boolean hasAuthority(RaidManagementRole shardRole) {
		AudiobookUser shardUser = getCurrentUser();

		boolean hasAuthority = false;

		if (shardUser != null) {
			if (shardUser.getRoles() != null) {
				if (shardUser.getRoles().contains(shardRole)) {
					hasAuthority = true;
				}
			}
		}

		return hasAuthority;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

}
