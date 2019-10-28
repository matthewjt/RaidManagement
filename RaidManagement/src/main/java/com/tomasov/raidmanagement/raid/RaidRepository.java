package com.tomasov.raidmanagement.raid;

/**
 * Created by Matt on 10/28/2019.
 */
public interface RaidRepository {

	Raid addRaid(Raid raid);

	Raid insertUpdateRaid(Raid raid) throws RepositoryException;

	void deleteRaid(Raid book);

	RaidManagementSet<Raid> findRaids();

	Integer findRaidsByFilterCount(String filter);

	RaidManagementSet<Raid> findRaidsByFilter(String filter);

	RaidManagementSet<Raid> findRaidsByFilter(String filter, String sortColumn, String sortOrder);

	RaidManagementSet<Raid> findRaidsByType(Long raidTypeId);

	Raid findRaidByKey(Long bookKey);

	Long getNextRaidKey();
}
