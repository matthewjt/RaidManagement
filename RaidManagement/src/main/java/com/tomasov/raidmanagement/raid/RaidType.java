package com.tomasov.raidmanagement.raid;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;

/**
 * basic data model for a raid type
 *
 * Created by matt on 10/28/2019.
 */
public class RaidType implements Comparable<RaidType>, Externalizable {

	private static final long serialVersionUID = 1L;

	private Long raidTypeKey;
	private String name;
	private Integer maxRaiders;

	public RaidType() {
	}

	public RaidType(Long raidTypeKey, String name, Integer maxRaiders) {
		this.raidTypeKey = raidTypeKey;
		this.name = name;
		this.maxRaiders = maxRaiders;
	}

	@Override
	public String toString() {
		// @formatter:off
		return MoreObjects.toStringHelper(this)
				.addValue(raidTypeKey)
				.addValue(name)
				.addValue(maxRaiders)
				.toString();
		// @formatter:on
	}

	public String toStringShort() {
		// @formatter:off
		return MoreObjects.toStringHelper(this)
				.addValue(name)
				.toString();
		// @formatter:on
	}

	public String displayPretty() {
		// @formatter:off
		return "\r\n[RaidType]"
				+ "\r\n\t raidTypeKey: '" + raidTypeKey + "'"
				+ "\r\n\t name: '" + name + "'"
				+ "\r\n\t maxRaiders: '" + maxRaiders + "'"
				;
		// @formatter:on
	}

	@Override
	public int hashCode() {
		return Objects.hash(raidTypeKey, name, maxRaiders);
	}

	@Override
	// a book is the same if the path, author, name, series, and series index are
	// the same
	public boolean equals(Object o) {
		if (null == o)
			return false;
		if (!(o instanceof RaidType))
			return false;

		RaidType _o = (RaidType) o;
		// @formatter:off
		return Objects.equals(this.raidTypeKey, _o.raidTypeKey) 
				&& Objects.equals(this.name, _o.name) 
				&& Objects.equals(this.maxRaiders, _o.maxRaiders) 
				;
		// @formatter:on
	}

	@Override
	public int compareTo(RaidType o) {
		// @formatter:off
		return ComparisonChain.start()
				.compare(this.raidTypeKey, o.raidTypeKey)
				.compare(this.name, o.name)
				.compare(this.maxRaiders, o.maxRaiders)
				.result();
		// @formatter:on
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// write the object
		List<Object> loc = new ArrayList<>();
		// uid
		loc.add(serialVersionUID);
		// 1L
		loc.add(this.raidTypeKey);
		loc.add(this.name);
		loc.add(this.maxRaiders);

		out.writeObject(loc);

	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// default deserialization
		@SuppressWarnings("unchecked")
		List<Object> loc = (List<Object>) in.readObject(); // Replace with real deserialization

		// 1L
		this.raidTypeKey = (Long) loc.get(1);
		this.name = (String) loc.get(2);
		this.maxRaiders = (Integer) loc.get(3);

	}

	public Long getRaidTypeKey() {
		return raidTypeKey;
	}

	public RaidType setRaidTypeKey(Long raidTypeKey) {
		this.raidTypeKey = raidTypeKey;

		return this;
	}

	public String getName() {
		return name;
	}

	public RaidType setName(String name) {
		this.name = name;

		return this;
	}

	public Integer getMaxRaiders() {
		return maxRaiders;
	}

	public RaidType setMaxRaiders(Integer maxRaiders) {
		this.maxRaiders = maxRaiders;

		return this;
	}

}
