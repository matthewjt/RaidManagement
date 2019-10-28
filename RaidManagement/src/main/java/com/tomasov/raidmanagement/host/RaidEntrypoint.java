package com.tomasov.raidmanagement.host;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.parboiled.common.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.tomasov.raidmanagement.raid.Raid;
import com.tomasov.raidmanagement.raid.RaidManagementSet;
import com.tomasov.raidmanagement.raid.RaidRepository;
import com.tomasov.raidmanagement.security.util.UserSessionHelper;

/**
 * REST Controller that defines actions that can be taken against a raid
 *
 * Created by matt 10/28/2019.
 */
@RestController
@RequestMapping("/rs/raid")
public class RaidEntrypoint {

	private static final Logger logger = LoggerFactory.getLogger(RaidEntrypoint.class);

	private final RaidRepository raids;
	private final UserSessionHelper userSessionHelper;

	@Autowired
	public RaidEntrypoint(RaidRepository raids, UserSessionHelper userSessionHelper) {
		this.raids = raids;
		this.userSessionHelper = userSessionHelper;
	}

	@RequestMapping(value = "/raid/{raidId}", method = RequestMethod.GET)
	public @ResponseBody Raid findBook(@PathVariable Long raidId) {
		Preconditions.checkNotNull(raidId, "Raid Id cannot be null and must be a valid number.");

		Raid raid = raids.findRaidByKey(raidId);
		if (null == raid) {
			logger.info("No Raid found for {}", raidId);
			return null; // TODO changing this from httpentity to just returning the object should have some kind of ControllerAdvice to return the proper status code for null results
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return raid;
	}

	@RequestMapping(value = "/raids", method = RequestMethod.GET)
	public @ResponseBody PaginatedData<Raid> findBooksByFilter(
			@RequestParam(required = false, defaultValue="") String filter,
			@RequestParam(required = false, defaultValue="1") Integer page,
			@RequestParam(required = false, defaultValue="DESC") String sortOrder,
			@RequestParam(required = false, defaultValue="Added") String sortColumn,
			@RequestParam(required = false) Integer itemsPerPage) {


		Integer totalBooks = raids.findRaidsByFilterCount(filter);
		if (null == itemsPerPage || itemsPerPage < 1)
			itemsPerPage = totalBooks; // if no page is specified then return all raids

		Integer startElement = ((page-1) * itemsPerPage) + 1;
		Integer endElement = page * itemsPerPage;
		if (endElement > totalBooks) {
			endElement = totalBooks;
		}

		List<Raid> raidPage = raids.findRaidsByFilter(filter, sortColumn, sortOrder).stream()
				.skip(startElement - 1)
				.limit(itemsPerPage)
				.map(b -> b)
				.collect(Collectors.toList());

		return new PaginatedData<>(totalBooks, startElement, endElement, itemsPerPage, raidPage);
	}

	@NotNull
	private Map<String, List<Raid>> convertedNestedBooks(Map<String, RaidManagementSet<Raid>> toConvert) {
		Map<String, List<Raid>> toReturn = Maps.newHashMap();
		for (String key: toConvert.keySet()) {
			RaidManagementSet<Raid> raids = toConvert.get(key);
			toReturn.put(key, raids.stream().map(b -> b).collect(Collectors.toList()));
		}
		return toReturn;
	}

}
