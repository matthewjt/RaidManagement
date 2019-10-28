package com.tomasov.raidmanagement.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by seth on 10/19/15.
 * 
 * matt - added overrideUrl
 */
public class Configuration {
	private Set<String> directories;
	private String overrideUrl;
	private Boolean usingDocker;
	private List<String> patterns;
	
	public Configuration() {
		this.directories = new HashSet<>();
	}

	public Set<String> getDirectories() {
		return directories;
	}
	void setDirectories(Set<String> directories) {
		this.directories = directories;
	}

	public String getOverrideUrl() {
		if (overrideUrl==null){
			return "";
		}
		return overrideUrl;
	}
	void setOverrideUrl(String overrideUrl) {
		this.overrideUrl = overrideUrl;
	}

	public Boolean getUsingDocker() {
		if (usingDocker == null) {
			usingDocker = false;
		}
		return usingDocker;
	}
	void setUsingDocker(boolean usingDocker) { this.usingDocker = usingDocker; }

	public List<String> getPatterns() {
		return null != patterns ? patterns : new ArrayList();
	}
	void setPatterns(List<String> patterns) {
		this.patterns = patterns;
	}
}
