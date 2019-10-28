package com.tomasov.raidmanagement.user;

import java.util.Set;


/**
 * Created by matt on 10/29/15.
 */
public interface UserRepository {
    
	AudiobookUser getUser(String userName);
	AudiobookUser addUser(AudiobookUser user);
	void removeUser(AudiobookUser user);
	void removeUserByName(String user);
	AudiobookUser addUpdateUser(AudiobookUser user);
	Set<AudiobookUser> getAllUsers();
}
