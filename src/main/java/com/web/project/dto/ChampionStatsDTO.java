package com.web.project.dto;

public class ChampionStatsDTO {
	
    private String championId;
    private String championName;
    private String krchampionName;
	private StatsDTO stats;
    
    
    public String getChampionId() {
        return championId;
    }

    public StatsDTO getStats() {
        return stats;
    }

    public String getChampionName() {
		return championName;
	}
   
    public String getKrchampionName() {
		return krchampionName;
	}

	public void setChampionName(String championName) {
		this.championName = championName;
	}

	public void setChampionId(String championId) {
        this.championId = championId;
    }
	
	public void setKrchampionName(String krchampionName) {
		this.krchampionName = krchampionName;
	}
	
    public void setStats(StatsDTO stats) {
        this.stats = stats;
    }
}