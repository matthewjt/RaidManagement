package com.tomasov.raidmanagement.raid;

import java.util.Date;

/**
 * Raid builder helps to construct a raid in a readable fashion.
 *
 * Note: setters may need to return a new instance of RaidBuilder. currently the
 * same instance is carried forward
 */
public class RaidBuilder {

	private Long raidKey;
	private RaidType raidType;
	private Date date;

	public Raid createRaid() {
		return new Raid(raidKey, raidType, date);
	}

	public Long getRaidKey() {
		return raidKey;
	}

	public RaidBuilder setRaidKey(Long raidKey) {
		this.raidKey = raidKey;

		return this;
	}

	public RaidType getRaidType() {
		return raidType;
	}

	public RaidBuilder setRaidType(RaidType raidType) {
		this.raidType = raidType;

		return this;
	}

	public Date getDate() {
		return date;
	}

	public RaidBuilder setDate(Date date) {
		this.date = date;

		return this;
	}
}