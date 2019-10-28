package com.tomasov.raidmanagement.user;

public enum UserPreference {

	DEFAULT_SORT, BOOK_FEED, ;

	public static UserPreference fromValue(String value) {
		if (value == null) {
			return null;
		}
		for (UserPreference shardUserPreference : UserPreference.values()) {
			if (shardUserPreference.name().equalsIgnoreCase(value)) {
				return shardUserPreference;
			}
		}

		return null;
	}
}
