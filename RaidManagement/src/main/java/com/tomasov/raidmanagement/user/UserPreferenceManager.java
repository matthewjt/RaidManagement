package com.tomasov.raidmanagement.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserPreferenceManager {

	private static final Logger logger = LoggerFactory.getLogger(UserPreferenceManager.class);
	
	public static class UserFeedData {
		private Long bookKey;

		public Long getBookKey() {
			return bookKey;
		}

		public Date getDateAdded() {
			return dateAdded;
		}

		private Date dateAdded;

		public UserFeedData(Long bookKey) {
			this.bookKey = bookKey;
			this.dateAdded = null;
		}

		public UserFeedData(String bookKeyDataComposite) {
			if (bookKeyDataComposite!=null) {
				if (bookKeyDataComposite.contains(ID_DATE_SEPERATOR)) {
					String keyString = bookKeyDataComposite.substring(0, bookKeyDataComposite.indexOf(ID_DATE_SEPERATOR));
					String date = bookKeyDataComposite.substring(bookKeyDataComposite.indexOf(ID_DATE_SEPERATOR) + ID_DATE_SEPERATOR.length());
					
					this.bookKey = Long.valueOf(keyString);
					this.dateAdded = new Date(Long.valueOf(date));
					
				} else {
					this.bookKey = Long.valueOf(bookKeyDataComposite);
				}
			}
		}
		
		public UserFeedData(Long bookKey, Date dateAdded) {
			this.bookKey = bookKey;
			this.dateAdded = dateAdded;
		}
		
		public String preferenceValue() {
			return this.bookKey + (this.dateAdded!=null? ID_DATE_SEPERATOR + new Date().getTime(): ""); 
		}
	}
	
	public static final String ID_DATE_SEPERATOR = "::||::";
	
	public static AudiobookUser addPreference(AudiobookUser user, UserPreference preferenceType, String preferenceValue) {
		if (user != null && preferenceType != null) {
			if (preferenceValue == null) {
				user.clearPreference(preferenceType);
			} else {
				user.addPreference(preferenceType, preferenceValue);
			}
		}

		return user;
	}

	public static AudiobookUser addBook(AudiobookUser user, Long bookKey) {
		if (user != null && bookKey != null) {

			UserPreference shardUserPreference = UserPreference.BOOK_FEED;

			String existingPreference = user.getPreference(shardUserPreference);

			List<String> items = new ArrayList<String>();
			if (existingPreference != null && existingPreference.trim().length() > 0) {
				items.addAll(Arrays.asList(existingPreference.split("\\s*,\\s*")));
			}

			if (!items.contains(String.valueOf(bookKey))) {
				items.add(new UserFeedData(bookKey, new Date()).preferenceValue());
			}

			user.clearPreference(shardUserPreference);

			user.addPreference(shardUserPreference, convertToStringArray(items));
		}

		return user;
	}

	public static AudiobookUser removeBook(AudiobookUser user, Long bookKey) {
		if (user != null && bookKey != null) {

			UserPreference shardUserPreference = UserPreference.BOOK_FEED;

			String existingPreference = user.getPreference(shardUserPreference);

			List<String> items = new ArrayList<String>();
			if (existingPreference != null && existingPreference.trim().length() > 0) {
				items.addAll(Arrays.asList(existingPreference.split("\\s*,\\s*")));
			}
			
			List<String> itemsToRemove = new ArrayList<>();
			
			for (String item : items) {
				if (item.startsWith(String.valueOf(bookKey) + ID_DATE_SEPERATOR) || item.equals(String.valueOf(bookKey))) {
					itemsToRemove.add(item);
				}
			}
			
			items.removeAll(itemsToRemove);

			user.clearPreference(shardUserPreference);

			user.addPreference(shardUserPreference, convertToStringArray(items));
		}

		return user;
	}

	private static String convertToStringArray(List<String> listToConvert) {
		return StringUtils.join(listToConvert, ',');
	}

	public static boolean containsBook(AudiobookUser user, Long bookKey) {
		if (user != null && bookKey != null) {

			List<Long> items = allBookKeys(user);

			return items.contains(bookKey);
		}

		return false;
	}
	
	public static boolean isBookKeyInFeed(AudiobookUser user, Long bookKey) {
		return allBookKeys(user).contains(bookKey);
	}

	public static List<Long> allBookKeys(AudiobookUser user) {
		List<Long> bookKeys = new ArrayList<Long>();

		if (user != null) {
			List<UserFeedData> feedData = allUserFeedData(user);

			for (UserFeedData shardUserFeedData : feedData) {
				bookKeys.add(shardUserFeedData.getBookKey());
			}
		}

		return bookKeys;
	}

	public static List<UserFeedData> allUserFeedData(AudiobookUser user) {
		List<UserFeedData> bookKeys = new ArrayList<UserFeedData>();

		if (user != null) {

			UserPreference shardUserPreference = UserPreference.BOOK_FEED;

			String existingPreference = user.getPreference(shardUserPreference);

			List<String> items = new ArrayList<String>();
			if (existingPreference != null && existingPreference.trim().length() > 0) {
				items.addAll(Arrays.asList(existingPreference.split("\\s*,\\s*")));
			}

			for (String string : items) {
				bookKeys.add(new UserFeedData(string));
			}

		}

		return bookKeys;
	}

}
