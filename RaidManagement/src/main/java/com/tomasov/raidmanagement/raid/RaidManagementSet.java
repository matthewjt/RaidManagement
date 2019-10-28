package com.tomasov.raidmanagement.raid;

import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

public class RaidManagementSet<E> extends TreeSet<E> {

	private static final long serialVersionUID = 1L;

	public E get(int count) throws IndexOutOfBoundsException {
		return Iterables.get(this, count);
	}

	public RaidManagementSet() {
		super();
	}
	public RaidManagementSet(Comparator<E> comparator) {
		super(comparator);
	}

	public RaidManagementSet(Collection<? extends E> c) {
		this();
		addAll(c);
	}

}
