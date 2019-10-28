package com.tomasov.raidmanagement.raid;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookPart implements Comparable<BookPart>, Externalizable {
	private static final long serialVersionUID = 3L;

	private Long bookFileKey;
	private Long bookKey;
	private String fileName;
	private String path;
	private Date dateCreated = new Date();
	private Date dateModified = new Date();
	private String contentType;
	private long length = 0;

	public BookPart() {

	}

	public BookPart(Long bookFileKey, Long bookKey, String fileName, String path, Date dateCreated, Date dateModified, String contentType, long length) {
		this.bookFileKey = bookFileKey;
		this.bookKey = bookKey;
		this.fileName = fileName;
		this.path = path;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.contentType = contentType;
		this.length = length;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).addValue(bookFileKey).addValue(bookKey).addValue(fileName).addValue(path).addValue(dateCreated).addValue(dateModified).toString();
	}

	public int compareTo(BookPart o) {
		if (null == o) {
			return -1;
		}

		return ComparisonChain.start().compare(this.fileName, o.fileName).result();
	}

	@Override
	public boolean equals(Object o) {
		if (null == o)
			return false;
		if (!(o instanceof Raid))
			return false;

		BookPart _o = (BookPart) o;
		// @formatter:off
		return ComparisonChain.start()
				.compare(this.path, _o.path, Ordering.natural().nullsFirst())
				.result() == 0;
		// @formatter:on
	}

	public static BookPartBuilder create() {
		return new BookPartBuilder();
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public String getFileName() {
		return fileName;
	}

	public Long getBookFileKey() {
		return bookFileKey;
	}

	public void setBookFileKey(Long bookFileKey) {
		this.bookFileKey = bookFileKey;
	}

	public String getPath() {
		return path;
	}

	public Long getBookKey() {
		return bookKey;
	}

	public void setBookKey(Long bookKey) {
		this.bookKey = bookKey;
	}

	public String getContentType() {
		return contentType;
	}

	public long getLength() {
		return length;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// write the object
		List<Object> loc = new ArrayList<Object>();
		// uid
		loc.add(serialVersionUID);
		// 3L
		loc.add(this.bookFileKey);
		loc.add(this.bookKey);
		loc.add(this.fileName);
		loc.add(this.path);
		loc.add(this.dateCreated);
		loc.add(this.dateModified);
		loc.add(this.contentType);
		loc.add(this.length);

		// 4L

		out.writeObject(loc);

	}

	@Override
	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// default deserialization
		List<Object> loc = (List<Object>) in.readObject(); // Replace with real deserialization
		Long uid = (Long) loc.get(0);

		// 3L
		this.bookFileKey = (Long) loc.get(1);
		this.bookKey = (Long) loc.get(2);
		this.fileName = (String) loc.get(3);
		this.path = (String) loc.get(4);
		this.dateCreated = (Date) loc.get(5);
		this.dateModified = (Date) loc.get(6);
		this.contentType = (String) loc.get(7);
		this.length = (Long) loc.get(8);
		// 4L
		if (uid >= 3) {
			// add new fields here
		}
	}

}
