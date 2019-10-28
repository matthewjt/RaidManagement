package com.tomasov.raidmanagement.user;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomasov.raidmanagement.security.util.RaidManagementSecurityProperties;
import com.tomasov.raidmanagement.util.PropertyUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

/**
 * The user repository which stores authorized users within the application
 */
@Repository
public class MapDbUserRepository implements UserRepository {

	private static final Logger logger = LoggerFactory.getLogger(MapDbUserRepository.class);
	public static final String ADMIN_USER_ID = RaidManagementSecurityProperties.adminUserId;

	private final DB userDb;

	final BTreeMap<String, AudiobookUser> users;

	@Autowired
	public MapDbUserRepository() {
		this(DBMaker.fileDB(findFile()).closeOnJvmShutdown().transactionEnable().make());
	}

	private static File findFile() {
		Path path = Paths.get(PropertyUtil.getConfigPath() + "/users.db");

		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path.getParent());
				logger.debug("Creating {}", path.toAbsolutePath());
			} catch (IOException e) {
				throw new RuntimeException("Unable to create shard database", e);
			}
		}

		return path.toFile();
	}

	public MapDbUserRepository(DB bookDb) {
		this.userDb = bookDb;
		users = userDb.treeMap("books").keySerializer(Serializer.STRING).valueSerializer(Serializer.ELSA).createOrOpen();
	}

	@Override
	public AudiobookUser getUser(String userName) {
		if (userName != null && users.containsKey(userName)) {
			return users.get(userName);
		}

		return null;
	}

	@Override
	public AudiobookUser addUser(AudiobookUser user) {
		if (user != null && user.validate()) {
			users.put(user.getUserName(), user);
			userDb.commit();

			return user;
		}

		return null;
	}

	@Override
	public void removeUser(AudiobookUser user) {
		logger.debug("Removing user {}", user);

		if (user != null && user.getUserName() != null && !user.getUserName().equalsIgnoreCase(ADMIN_USER_ID) && users.containsKey(user.getUserName())) {
			users.remove(user.getUserName());
			userDb.commit();
		} else {
			logger.debug("Not removing user: {}", user);
		}
	}

	@Override
	public void removeUserByName(String userName) {
		if (userName != null && users.containsKey(userName)) {
			users.remove(userName);

			userDb.commit();
		}
	}

	@Override
	public AudiobookUser addUpdateUser(AudiobookUser user) {
		if (user != null && user.validate()) {

			users.put(user.getUserName(), user);
			userDb.commit();
			return user;
		} else {
			logger.debug("Not adding user '{}' as they are null or not valid.", user);
		}

		return null;
	}

	@Override
	public Set<AudiobookUser> getAllUsers() {
		Set<AudiobookUser> sortedUsers = new TreeSet<>();
		sortedUsers.addAll(users.getValues());

		return sortedUsers;
	}

}
