package com.web.project.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.project.dto.championstats.ChampionStatsDTO;
import com.web.project.dto.championstats.CounterChampionDTO;
import com.web.project.dto.championstats.CounterCountDTO;
import com.web.project.dto.championstats.CounterPositionDTO;
import com.web.project.dto.championstats.TierDataDTO;
import com.web.project.system.CounterJsonReader;
import com.web.project.system.JsonReader;


@Service
public class TierPositionService {

    private final JsonReader jsonReader;
    private final CounterJsonReader counterJsonReader;

	@Autowired
	public TierPositionService(JsonReader jsonReader, CounterJsonReader counterJsonReader) {
        this.jsonReader = jsonReader;
        this.counterJsonReader = counterJsonReader;
	}

	public List<ChampionStatsDTO> getChampionsData(String tier, String position) {
		try {

			TierDataDTO tierData = jsonReader.readJsonFile(tier);
			List<ChampionStatsDTO> allPositionData = new ArrayList<>();

			if (position.equals("ALL")) {
				for (List<ChampionStatsDTO> positionData : tierData.getPositions().values()) {
					allPositionData.addAll(positionData);
				}
			} else {
				allPositionData = tierData.getPositions().get(position);
			}

			allPositionData.sort((champ1, champ2) -> Double.compare(champ2.getStats().getTierScore(),
					champ1.getStats().getTierScore()));

			// 중복된 챔피언을 제거하여 유일한 챔피언 데이터만을 유지
			List<ChampionStatsDTO> uniqueChampions = new ArrayList<>();
			Set<String> addedChampions = new HashSet<>();
			for (ChampionStatsDTO champion : allPositionData) {
				if (!addedChampions.contains(champion.getChampionName())) {
					uniqueChampions.add(champion);
					addedChampions.add(champion.getChampionName());
				}
			}
			return uniqueChampions;

		} catch (Exception e) {
			System.out.println("ERROR : " + e.getMessage());
			return null;
		}
	}
	public ChampionStatsDTO getChampionDataByName(String tier, String position, String championName) {
	    try {
	        TierDataDTO tierData = jsonReader.readJsonFile(tier);
	        List<ChampionStatsDTO> allPositionData;

	        if (position.equals("ALL")) {
	            allPositionData = tierData.getPositions().values().stream()
	                                    .flatMap(List::stream)
	                                    .collect(Collectors.toList());
	        } else {
	            allPositionData = new ArrayList<>(tierData.getPositions().get(position));
	        }

	        return allPositionData.stream()
	                .filter(champion -> champion.getChampionName().equalsIgnoreCase(championName))
	                .findFirst()
	                .orElse(null);
	    } catch (Exception e) {
	        System.out.println("ERROR: " + e.getMessage());
	        return null;
	    }
	}
	
}