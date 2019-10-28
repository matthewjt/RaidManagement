package com.tomasov.raidmanagement.raid;

import java.util.Date;

public class BookPartBuilder {

	private Long bookFileKey;
	private Long bookKey;
	private String fileName;
	private String path;
	private Date dateCreated = new Date();
	private Date dateModified = new Date();
	private String contentType;
	private long length = 0;

	public BookPartBuilder setBookFileKey(Long bookFileKey) {
		this.bookFileKey = bookFileKey;
		return this;
	}

	public BookPartBuilder setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public BookPartBuilder setPath(String path) {
		this.path = path;
		return this;
	}

	public BookPartBuilder setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

	public BookPartBuilder setDateModified(Date dateModified) {
		this.dateModified = dateModified;
		return this;
	}

	public BookPartBuilder setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public BookPartBuilder setLength(long length) {
		this.length = length;
		return this;
	}

	public BookPart createBookPart() {
		return new BookPart(bookFileKey, bookKey, fileName, path, dateCreated, dateModified, contentType, length);
	}
}