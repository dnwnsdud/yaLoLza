package com.web.project.dto.championstats;

public class PatchesChampionDTO {
    private String krChampionName;
    private String championName;
    private String position;
    private PatchesStatsDTO stats;

    public String getKrChampionName() {
        return krChampionName;
    }

    public String getChampionName() {
        return championName;
    }

    public String getPosition() {
        return position;
    }

    public PatchesStatsDTO getStats() {
        return stats;
    }

    public void setKrChampionName(String krChampionName) {
        this.krChampionName = krChampionName;
    }

    public void setChampionName(String championName) {
        this.championName = championName;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setStats(PatchesStatsDTO stats) {
        this.stats = stats;
    }
}