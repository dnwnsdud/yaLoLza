package com.web.project.dto.championstats;

import java.util.List;

public class PositionDTO {
    private List<ChampionStatsDTO> champions;

    
    public List<ChampionStatsDTO> getChampions() {
        return champions;
    }

    public void setChampions(List<ChampionStatsDTO> champions) {
        this.champions = champions;
    }
}
