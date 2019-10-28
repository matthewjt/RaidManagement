package com.tomasov.raidmanagement.raid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.mapdb.Atomic;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.tomasov.raidmanagement.util.PropertyUtil;

/**
 * The main repository driving the application. Retrieves raids
 *
 * Created by seth on 10/12/15.
 */
@Repository
public class MapDbRaidRepository implements RaidRepository {

	private static final Logger logger = LoggerFactory.getLogger(MapDbRaidRepository.class);

	private final DB raidDb;

	final BTreeMap<Long, Raid> raids;

	/**
	 *
	 */
	public MapDbRaidRepository() {
		this(DBMaker.fileDB(findFile()).closeOnJvmShutdown().transactionEnable().make());
	}

	private static File findFile() {
		Path path = Paths.get(PropertyUtil.getConfigPath() + "/raid.db");

		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path.getParent());
				logger.debug("Creating {}", path.toAbsolutePath());
			} catch (IOException e) {
				throw new RuntimeException("Unable to create shard database", e);
			}
		}

		return path.toFile();
	}

	@SuppressWarnings("unchecked")
	public MapDbRaidRepository(DB raidDb) {
		this.raidDb = raidDb;
		raids = raidDb.treeMap("raids").keySerializer(Serializer.RECID).valueSerializer(Serializer.ELSA).createOrOpen();
	}

	@Override
	public Raid addRaid(Raid raid) {

		if (!raids.containsKey(raid.getRaidKey())) {

			logger.debug("Raid path not found.  Adding raid as new.");

			if (raid.getRaidKey() == null) {
				raid.setRaidKey(this.getNextRaidKey());
			}

			raids.put(raid.getRaidKey(), raid);
			raidDb.commit();
		}

		return raid;
	}

	@Override
	public Raid insertUpdateRaid(Raid raid) throws RepositoryException {
		logger.debug("Inserting/Updating raid {}", raid.toStringShort());
		// if the key is null, it's probably new

		Raid raidToUpdate = null;

		logger.debug("     Trying to get raid by inverse '{}'", raid.getRaidKey());
		if (raid.getRaidKey() != null && raids.containsKey(raid.getRaidKey())) {
			raidToUpdate = raids.get(raid.getRaidKey());
		}

		if (raidToUpdate == null) {
			logger.debug("     Raid key is null.  Adding. {}", raid.toStringShort());
			raid = addRaid(raid);
		} else {

			// its an update.
			logger.debug("     Existing raid found: {}", raidToUpdate.toStringShort());

			raidToUpdate = updateRaid(raidToUpdate, raid);
			logger.debug("     Updated raid: {}", raidToUpdate.toStringShort());

			raids.put(raidToUpdate.getRaidKey(), raidToUpdate);

			raidDb.commit();

			return raidToUpdate;

		}
		return raid;
	}

	public Long getNextRaidKey() {
		Atomic.Long keys = raidDb.atomicLong("raid_keys").createOrOpen();
		return keys.incrementAndGet();
	}

	public Raid getRaidById(Long key) {
		return raids.get(key);
	}

	@Override
	public void deleteRaid(final Raid raid) {
		Long raidKey = raid.getRaidKey();

		raids.remove(raidKey);

		raidDb.commit();
	}

	@Override
	public RaidManagementSet<Raid> findRaids() {
		return new RaidManagementSet<>(raids.getValues());
	}

	@Override
	public RaidManagementSet<Raid> findRaidsByType(Long raidTypeKey) {
		return findRaids().stream().filter(raid -> (raid.getRaidType().getRaidTypeKey() != null && raid.getRaidType().getRaidTypeKey().equals(raidTypeKey))).collect(Collectors.toCollection(RaidManagementSet::new));
	}

	public Raid findRaidByKey(Long key) {
		return this.raids.get(key);
	}

	private Raid updateRaid(Raid existingBook, Raid raidWithChanges) {

		existingBook.setRaidType(raidWithChanges.getRaidType());
		existingBook.setDate(raidWithChanges.getDate());

		return existingBook;
	}

	@Override
	public RaidManagementSet<Raid> findRaidsByFilter(String filter) {
		return findRaidsByFilter(filter, "Added", "DESC");
	}

	@Override
	public RaidManagementSet<Raid> findRaidsByFilter(String filter, String sortColumn, String sortOrder) {
		final String _filter = filter != null ? filter.toLowerCase() : "";
		return findRaids().stream().filter(raid -> {
			Long raidTypeId = raid.getRaidType().getRaidTypeKey();
			Date date = raid.getDate();
			return StringUtils.containsIgnoreCase(String.valueOf(raidTypeId), _filter) || StringUtils.containsIgnoreCase(String.valueOf(date), _filter);
		}).collect(Collectors.toCollection(() -> new RaidManagementSet<>(new Raid.RaidComparator(sortColumn, sortOrder))));
	}

	@Override
	public Integer findRaidsByFilterCount(String filter) {
		return findRaidsByFilter(filter).size();
	}

}
