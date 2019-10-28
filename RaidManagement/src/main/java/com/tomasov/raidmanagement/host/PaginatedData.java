package com.tomasov.raidmanagement.host;

import java.util.ArrayList;
import java.util.List;

public class PaginatedData<PageDataType> {

	final Integer total;
	final Integer elementsPerPage;
	final ArrayList<PageDataType> pageData;
	final Integer startElement;
	final Integer endElement;
	final Integer totalPages;

	public PaginatedData(Integer total, Integer startElement, Integer endElement, Integer elementsPerPage, List<PageDataType> pageData) {
		this.total = total;
		this.elementsPerPage = elementsPerPage;
		this.pageData = new ArrayList<>(pageData);
		this.startElement = startElement;
		this.endElement = endElement;
		this.totalPages = calculateTotalPages(total, elementsPerPage);
	}

	private Integer calculateTotalPages(Integer total, Integer elementsPerPage) {
		return (total / elementsPerPage) + (total % elementsPerPage != 0 ? 1 : 0);
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public List<PageDataType> getPageData() {
		return pageData;
	}

	public Integer getStartElement() {
		if (this.getEndElement()==0) {
			return 0;
		}
		return startElement;
	}

	public Integer getEndElement() {
		return endElement;
	}

	public List<Integer> getPages(Integer currentPage) {
		if (currentPage == null) {
			currentPage = 1;
		}

		int start = 1;
		int end = 1;

		int totalPages = getTotalPages();
		if (totalPages <=20) {
			end = totalPages;
		} else {
			start = currentPage;
			end = currentPage;
			while (end-start<19) {

				if (end<totalPages) {
					end++;
				}
				if (end-start<19 && start>1) {
					start--;
				}
			}
		}

		ArrayList<Integer> pages = new ArrayList<Integer>();
		for (int i = start; i <= end; i++) {
			pages.add(i);
		}

		return pages;
	}
}
