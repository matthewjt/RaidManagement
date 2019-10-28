package com.tomasov.raidmanagement.raid;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

/**
 * basic data model for a raid
 *
 * Created by matt on 10/28/2019.
 */
public class Raid implements Comparable<Raid>, Externalizable {

	private static final long serialVersionUID = 1L;

	private Long raidKey;
	private RaidType raidType;
	private Date date = new Date();

	public Raid() {
	}

	public Raid(Long raidKey, RaidType raidType, Date date) {
		this.raidKey = raidKey;
		this.raidType = raidType;
		this.date = date;
	}

	public static RaidBuilder create() {
		return new RaidBuilder();
	}

	@Override
	public String toString() {
		// @formatter:off
		return MoreObjects.toStringHelper(this)
				.addValue(raidKey)
				.addValue(raidType)
				.addValue(date)
				.toString();
		// @formatter:on
	}

	public String toStringShort() {
		// @formatter:off
		return MoreObjects.toStringHelper(this)
				.addValue(raidType)
				.addValue(date)
				.toString();
		// @formatter:on
	}

	public String displayPretty() {
		// @formatter:off
		return "\r\n[Raid]"
				+ "\r\n\t raidKey: '" + raidKey + "'"
				+ "\r\n\t date: '" + date + "'"
				+ "\r\n\t raidType: '" + raidType + "'"
				;
		// @formatter:on
	}

	@Override
	public int hashCode() {
		return Objects.hash(raidKey, date, raidType);
	}

	@Override
	// a book is the same if the path, author, name, series, and series index are
	// the same
	public boolean equals(Object o) {
		if (null == o)
			return false;
		if (!(o instanceof Raid))
			return false;

		Raid _o = (Raid) o;
		// @formatter:off
		return Objects.equals(this.raidKey, _o.raidKey) 
				&& Objects.equals(this.date, _o.date) 
				&& Objects.equals(this.raidType, _o.raidType) 
				;
		// @formatter:on
	}

	@Override
	public int compareTo(Raid o) {
		// @formatter:off
		return ComparisonChain.start()
				.compare(this.date, o.date, Ordering.natural().nullsFirst())
				.compare(this.raidKey, o.raidKey)
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
		loc.add(this.raidKey);
		loc.add(this.date);
		loc.add(this.raidType);

		out.writeObject(loc);

	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// default deserialization
		@SuppressWarnings("unchecked")
		List<Object> loc = (List<Object>) in.readObject(); // Replace with real deserialization

		// 1L
		this.raidKey = (Long) loc.get(1);
		this.date = (Date) loc.get(2);
		this.raidType = (RaidType) loc.get(3);

	}

	public static class RaidComparator implements Comparator<Raid> {

		public static final String NAME = "Name", DATE = "Date";
		public static final String ASCENDING = "ASC", DESCENDING = "DESC";

		@SuppressWarnings("rawtypes")
		private static Ordering<Comparable> DEFAULT = Ordering.natural().onResultOf(new Function<Comparable, Comparable>() {
			@Nullable
			@Override
			public Comparable apply(@Nullable Comparable object) {
				if (object != null) {
					if (!(object instanceof String))
						return object;

					String toClean = (String) object;

					return toClean.trim().toUpperCase();
				} else {
					return null;
				}
			}
		}).nullsFirst();

		private String field;
		@SuppressWarnings("rawtypes")
		private Ordering<Comparable> ordering;

		RaidComparator(String field, String order) {
			this.field = field;

			this.ordering = ASCENDING.equals(order) ? DEFAULT : DEFAULT.reverse();
		}

		@Override
		public int compare(Raid v, Raid w) {

			switch (field) {
			case DATE:
				return ComparisonChain.start().compare(v.date, w.date, ordering).compare(v.raidType.getName(), w.raidType.getName(), DEFAULT).result();
			case NAME:
			default:
				return ComparisonChain.start().compare(v.raidType.getName(), v.raidType.getName(), DEFAULT).compare(v.date, w.date, DEFAULT).result();
			}
		}
	}

	public Long getRaidKey() {
		return raidKey;
	}

	public Raid setRaidKey(Long raidKey) {
		this.raidKey = raidKey;

		return this;
	}

	public RaidType getRaidType() {
		return raidType;
	}

	public Raid setRaidType(RaidType raidType) {
		this.raidType = raidType;

		return this;
	}

	public Date getDate() {
		return date;
	}

	public Raid setDate(Date date) {
		this.date = date;

		return this;
	}

}
