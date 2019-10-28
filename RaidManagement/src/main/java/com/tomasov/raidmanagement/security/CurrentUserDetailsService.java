package com.tomasov.raidmanagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tomasov.raidmanagement.user.AudiobookUser;
import com.tomasov.raidmanagement.user.UserRepository;

@Service
public class CurrentUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public CurrentUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		AudiobookUser user = userRepository.getUser(userName);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("User with name {} was not found", userName));

		}
		return new CurrentUser(user);
	}
}