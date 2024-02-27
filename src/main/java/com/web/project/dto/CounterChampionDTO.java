package com.web.project.dto;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CounterChampionDTO {

    private String championId;

    private String championName;

    private CounterStatsDTO stats;

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

    public CounterStatsDTO getStats() {
        return stats;
    }

    public void setStats(CounterStatsDTO stats) {
        this.stats = stats;
    }

}
