package com.tomasov.raidmanagement.web.controller;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tomasov.raidmanagement.host.PaginatedData;
import com.tomasov.raidmanagement.host.RaidEntrypoint;
import com.tomasov.raidmanagement.raid.Raid;

/**
 * Controller used to handle routing for the Raid views
 *
 * Created by seth on 10/30/15.
 */
@Controller
public class RaidController {
	private static final Logger logger = LoggerFactory.getLogger(RaidController.class);
	private static final Integer DEFAULT = 10;

	private final RaidEntrypoint books;
	private final Integer itemsPerPage;

	public RaidController(RaidEntrypoint books, Integer itemsPerPage) {
		this.books = books;
		this.itemsPerPage = itemsPerPage;
	}

	@Autowired
	public RaidController(RaidEntrypoint books) {
		this(books, DEFAULT);
	}

	@RequestMapping("/secure/browse")
	public ModelAndView browse(
			@RequestParam(required = false, defaultValue="") String filter,
			@RequestParam(required = false, defaultValue="1") Integer pageStart,
			@RequestParam(required = false, defaultValue="DESC") String sortDirection,
			@RequestParam(required = false, defaultValue="Added") String sortColumn,
			Model model) {

		PaginatedData<Raid> paginatedData = books.findBooksByFilter(filter, pageStart, sortDirection, sortColumn, itemsPerPage);

		logger.trace("Page data: " + paginatedData.getPageData());
		
		model.addAttribute("paginatedData", paginatedData);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("sortColumn", sortColumn);
		model.addAttribute("filter", filter);

		return new ModelAndView("secure/browse", "bookControllerModel", model);
	}

	@RequestMapping("/secure/book/{id}")
	public String book(@PathVariable(value = "id") Long bookId, Model model) {

		Raid book = books.findBook(bookId);

		if (book==null) {
			// TODO forward to error page?
			book = emptyBookResource();
		}
		model.addAttribute("book", book);
		
		logger.trace("Set book: " + book);

		return "secure/book";
	}

	@NotNull
	private Raid emptyBookResource() {
		return Raid.create().createRaid();
	}

}
