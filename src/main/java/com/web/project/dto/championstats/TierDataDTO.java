package com.web.project.dto.championstats;

import java.util.List;
import java.util.Map;

public class TierDataDTO {
    private Map<String, List<ChampionStatsDTO>> positions;

    public Map<String, List<ChampionStatsDTO>> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, List<ChampionStatsDTO>> positions) {
        this.positions = positions;
    }
    
}