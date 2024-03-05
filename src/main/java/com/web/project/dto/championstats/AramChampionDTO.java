package com.web.project.dto.championstats;

public class AramChampionDTO {
    private String championId;
    private String championName;
    private String krchampionName;
    private AramStatsDTO stats;
	public String getChampionId() {
		return championId;
	}
	public void setChampionId(String championId) {
		this.championId = championId;
	}
	public String getChampionName() {
		return championName;
	}
	public void setChampionName(String championName) {
		this.championName = championName;
	}
	public String getKrchampionName() {
		return krchampionName;
	}
	public void setKrchampionName(String krchampionName) {
		this.krchampionName = krchampionName;
	}
	public AramStatsDTO getStats() {
		return stats;
	}
	public void setStats(AramStatsDTO stats) {
		this.stats = stats;
	}

}
