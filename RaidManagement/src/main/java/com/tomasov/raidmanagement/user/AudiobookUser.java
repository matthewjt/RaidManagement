package com.tomasov.raidmanagement.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

/**
 * Data Object used to represent the User data within the audiobook rss Domain
 *
 * TODO roles should be switched to a Set from a List
 * TODO AudiobookUser should accept the clear text password and then hash it internally in either the setter or constructor
 */
public class AudiobookUser implements Comparable<AudiobookUser>, Externalizable {

	private static final long serialVersionUID = 2L;

	private static final Logger logger = LoggerFactory.getLogger(AudiobookUser.class);

	private String userName;
	private String hashedPassword;
	private List<RaidManagementRole> roles;
	private HashMap<String, String> preferences;

	public AudiobookUser() {
		// needed for Externlizable deserializing
		roles = new ArrayList<>();
		preferences = new HashMap<>();
	}

	public AudiobookUser(String userName) {
		this(userName, hashPassword(new Date().getTime() + ""));
	}

	public AudiobookUser(String userName, String hashedPassword) {
		this();
		// TODO should userName allowed to be null?
//		if (null == userName) throw new IllegalArgumentException("User name must be populated.");
		this.userName = userName != null ? userName.toLowerCase() : userName;
		this.hashedPassword = hashedPassword;
	}

	public AudiobookUser(@JsonProperty("userName") String userName, @JsonProperty("hashedPassword") String hashedPassword, @JsonProperty("roles") List<RaidManagementRole> roles) {
		this(userName, hashedPassword);
		this.roles = roles;
	}

	public AudiobookUser(String userName, List<RaidManagementRole> roles) {
		this(userName);
		this.roles = roles;
	}

	// FIXME Array.asList() creates an unmodifiable collection so methods like add and remove are unsupported operations
	public AudiobookUser(String user, String hashedPassword, RaidManagementRole[] roles) {
		this(user, hashedPassword, (roles != null ? Arrays.asList(roles) : null));
	}

	// FIXME Array.asList() creates an unmodifiable collection so methods like add and remove are unsupported operations
	public AudiobookUser(String user, String hashedPassword, RaidManagementRole singleRole) {
		this(user, hashedPassword, (singleRole != null ? Arrays.asList(singleRole) : null));
	}

	// FIXME Array.asList() creates an unmodifiable collection so methods like add and remove are unsupported operations
	public AudiobookUser(String user, RaidManagementRole[] roles) {
		this(user, (roles != null ? Arrays.asList(roles) : null));
	}

	/**
	 * FIXME Array.asList() creates an unmodifiable collection so methods like add and remove are unsupported operations
	 * @param user
	 * @param singleRole
	 */
	public AudiobookUser(String user, RaidManagementRole singleRole) {
		this(user, (singleRole != null ? Arrays.asList(singleRole) : null));
	}

	public String getPreference(UserPreference preferenceName) {
		return getPreference(preferenceName.name());
	}

	private String getPreference(String preferenceName) {
		if (this.preferences != null && this.preferences.containsKey(preferenceName)) {
			return this.preferences.get(preferenceName);
		} else {
			return null;
		}
	}

	public void addPreference(UserPreference preferenceName, String preferenceValue) {
		setPreference(preferenceName.name(), preferenceValue);
	}

	private void setPreference(String preferenceName, String preferenceValue) {
		this.preferences.put(preferenceName, preferenceValue);
	}

	public void clearPreference(UserPreference preferenceName) {
		clearPreference(preferenceName.name());
	}

	private void clearPreference(String preferenceName) {
		if (this.preferences != null && this.preferences.containsKey(preferenceName)) {
			this.preferences.remove(preferenceName);
		}
	}

	public boolean validatePassword(String plainTextPassword) {
		try {
			return PasswordHash.validatePassword(plainTextPassword, this.hashedPassword);
		} catch (Exception someOddProblem) {
			logger.error("Problem validating password: {}", someOddProblem.getMessage(), someOddProblem);
		}

		return false;
	}

	public static String hashPassword(String plainTextPassword) {
		if (plainTextPassword != null) {
			try {
				return PasswordHash.createHash(plainTextPassword);
			} catch (Exception someOddProblem) {
				logger.error("Problem validating password: {}", someOddProblem.getMessage(), someOddProblem);
			}
		}

		return null;
	}

	public AudiobookUser hashAndSetPassword(String plainTextPassword) {
		this.hashedPassword = hashPassword(plainTextPassword);
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public List<RaidManagementRole> addRole(RaidManagementRole role) {
		if (role != null && !this.roles.contains(role)) {
			this.roles.add(role);
		}

		return this.roles;
	}

	public List<RaidManagementRole> removeRole(RaidManagementRole role) {
		if (role != null && this.roles.contains(role)) {
			this.roles.remove(role);
		}

		return this.roles;
	}

	public boolean isMember(RaidManagementRole role) {
		return role != null && this.roles.contains(role);
	}

	@Override
	public boolean equals(Object o) {
		if (null == o)
			return false;
		if (!(o instanceof AudiobookUser))
			return false;

		AudiobookUser _o = (AudiobookUser) o;
		return ComparisonChain.start()
						.compare(this.userName, _o.userName, Ordering.natural().nullsFirst()).result() == 0;
	}

	@Override
	public int compareTo(AudiobookUser o) {
		return ComparisonChain.start()
						.compare(this.userName, o.userName, Ordering.natural().nullsFirst()).result();
	}

	@Override
	public String toString() {
		//@formatter:off
		return MoreObjects.toStringHelper(this)
				.addValue(serialVersionUID)
				.addValue(userName)
				.addValue(hashedPassword)
				.addValue(roles)
				.addValue(preferences).toString();
		//@formatter:on
	}

	public boolean validate() {
		return this.userName != null && this.userName.length() > 0 && this.hashedPassword != null && this.hashedPassword.length() > 0;
	}

	public AudiobookUser clone() {
		return new AudiobookUser(this.userName, this.hashedPassword, this.roles);
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public List<RaidManagementRole> getRoles() {
		return roles;
	}

	public void setRoles(List<RaidManagementRole> roles) {
		this.roles = roles;
		if (this.roles == null) {
			this.roles = new ArrayList<>();
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		logger.trace("Serializing object: {}", this);
		// write the object
		List<Object> loc = new ArrayList<>();
		// uid
		loc.add(serialVersionUID);
		// 1L
		loc.add(this.userName);
		loc.add(this.hashedPassword);
		loc.add(this.getRoles());

		// 2L
		loc.add(this.preferences);
		out.writeObject(loc);

	}

	@Override
	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// default deserialization
		List<Object> loc = (List<Object>) in.readObject(); // Replace with real deserialization
		Long uid = (Long) loc.get(0);

		// 1L
		this.userName = (String) loc.get(1);
		this.hashedPassword = (String) loc.get(2);
		this.roles = (List<RaidManagementRole>) loc.get(3);
		// 2L
		if (uid >= 2) {
			this.preferences = (HashMap<String, String>) (loc.get(4));
		}

		logger.trace("Deserialized object: {}", this);
	}

	public HashMap<String, String> getPreferences() {
		return preferences;
	}
}
