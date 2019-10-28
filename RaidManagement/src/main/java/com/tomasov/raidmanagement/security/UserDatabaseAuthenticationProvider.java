package com.tomasov.raidmanagement.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.tomasov.raidmanagement.security.util.RaidManagementSecurityProperties;
import com.tomasov.raidmanagement.user.RaidManagementRole;
import com.tomasov.raidmanagement.user.AudiobookUser;
import com.tomasov.raidmanagement.user.UserRepository;

@Component
public class UserDatabaseAuthenticationProvider implements AuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(UserDatabaseAuthenticationProvider.class);

	private UserRepository userRepository;

	@Autowired
	public UserDatabaseAuthenticationProvider(UserRepository repo, RaidManagementSecurityProperties shardSecurityProperties) {

		userRepository = repo;

		String adminUserId = shardSecurityProperties.getAdminUserId();
		String adminUserPassword = shardSecurityProperties.getAdminUserPassword();
		//String userId = shardSecurityProperties.getUserId();
		//String userPassword = shardSecurityProperties.getUserPassword();

		logger.info("Loading user database.");
		logger.info("     Detault Admin ID: " + adminUserId);
		//logger.info("     Detault User ID: " + userId);

		AudiobookUser adminUser = null;
		if ((adminUser = this.userRepository.getUser(adminUserId)) == null) {
			logger.info("     Admin user does not exist.  Creating with default password '{}'", adminUserPassword);
			adminUser = new AudiobookUser(adminUserId, RaidManagementRole.values()).hashAndSetPassword(adminUserPassword);
			userRepository.addUpdateUser(adminUser);
		}
		/*
		AudiobookUser user = null;
		if ((user = this.userRepository.getUser(userId)) == null) {
			logger.info("     User does not exist.  Creating with default password '{}'", userPassword);
			user = new AudiobookUser(userId, RaidManagementRole.USER).hashAndSetPassword(adminUserPassword);
			userRepository.addUpdateUser(user);
		}
		*/

	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();

		AudiobookUser user = userRepository.getUser(name);

		// use the credentials to try to authenticate against the third party
		// system
		
		if (user == null || !checkLoginCredentials(user, password)) {
			logger.info("Login invalid for user '{}' and password '<hidden>'.", name, password);
			throw new UserDatabaseAuthenticationException("User " + name + " not authenticated");
		}

		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		for (RaidManagementRole shardRole : user.getRoles()) {
			grantedAuths.add(new SimpleGrantedAuthority(shardRole.getRoleName()));
		}

		logger.info("{}: logged in with user authorities {}", user.getUserName(), grantedAuths);
		
		return new UsernamePasswordAuthenticationToken(name, password, grantedAuths);

	}

	@Override
	public boolean supports(Class<?> authentication) {
		//return authentication.equals(UsernamePasswordAuthenticationToken.class);
		return true;
	}

	public boolean checkLoginCredentials(AudiobookUser user, String password) {

		if (user == null) {
			logger.info("User does not exist.");
			return false;
		}

		return user.validatePassword(password);

	}

}
