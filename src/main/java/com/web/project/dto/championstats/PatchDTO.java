package com.web.project.dto.championstats;

import java.util.Map;

public class PatchDTO {
    private Map<String, PatchesChampionDTO> champions;

    public Map<String, PatchesChampionDTO> getChampions() {
        return champions;
    }

    public void setChampions(Map<String, PatchesChampionDTO> champions) {
        this.champions = champions;
    }
}
