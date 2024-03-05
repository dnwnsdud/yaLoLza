package com.web.project.system;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.project.dto.championstats.CounterChampionDTO;
import com.web.project.dto.championstats.CounterCountDTO;
import com.web.project.dto.championstats.CounterPositionDTO;

@Service
public class CounterDataService {

	private final CounterJsonReader counterJsonReader;

	@Autowired
	public CounterDataService(CounterJsonReader counterJsonReader) {
		this.counterJsonReader = counterJsonReader;
	}

	public Map<String, Object> getCounterData(String position, String champion, String additionalChampion) throws IOException {
	    Map<String, Object> modelData = new HashMap<>();
	    CounterPositionDTO counterData = counterJsonReader.readCounterJsonFile();
	    List<CounterCountDTO> positionData = getPositionData(position, counterData);

	    if (positionData == null) {
	        modelData.put("error", "Data not found");
	        return modelData;
	    }

	    List<CounterChampionDTO> targetCounters = new ArrayList<>();
	    List<CounterChampionDTO> otherChampions = new ArrayList<>();
	    Map<String, Integer> matchCounts = new HashMap<>();

	    for (CounterCountDTO countDTO : positionData) {
	        if (additionalChampion == null) {
	            if (countDTO.getCounter().stream()
	                    .anyMatch(championDTO -> championDTO.getChampionName().equalsIgnoreCase(champion))) {
	                targetCounters.addAll(countDTO.getCounter());
	            }
	        } else {
	            List<CounterChampionDTO> matchedCounters = countDTO.getCounter().stream()
	                    .filter(championDTO -> championDTO.getChampionName().equalsIgnoreCase(champion)
	                            || championDTO.getChampionName().equalsIgnoreCase(additionalChampion))
	                    .collect(Collectors.toList());

	            boolean containsBothChampions = matchedCounters.stream()
	                    .anyMatch(championDTO -> championDTO.getChampionName().equalsIgnoreCase(champion))
	                    && matchedCounters.stream().anyMatch(
	                            championDTO -> championDTO.getChampionName().equalsIgnoreCase(additionalChampion));

	            if (containsBothChampions) {
	                targetCounters.addAll(matchedCounters);
	                Collections.swap(targetCounters, 0,
	                        targetCounters.indexOf(matchedCounters.stream()
	                                .filter(championDTO -> championDTO.getChampionName().equalsIgnoreCase(champion))
	                                .findFirst().orElse(null)));
	            }
	        }

	        if (!targetCounters.isEmpty()) {
	            CounterChampionDTO selectedChampion = targetCounters.stream()
	                    .filter(championDTO -> championDTO.getChampionName().equalsIgnoreCase(champion)).findFirst()
	                    .orElse(null);

	            if (selectedChampion != null) {
	                targetCounters.remove(selectedChampion);
	                targetCounters.add(0, selectedChampion);
	            }
	        }

	        for (CounterCountDTO countDTO1 : positionData) {
	            if (countDTO1.getCounter().stream().anyMatch(championDTO -> championDTO.getChampionName().equalsIgnoreCase(champion))) {
	                for (CounterChampionDTO championDTO : countDTO1.getCounter()) {
	                    if (!championDTO.getChampionName().equalsIgnoreCase(champion) && !otherChampions.contains(championDTO)) {
	                        otherChampions.add(championDTO);
	                        matchCounts.put(championDTO.getChampionName(), countDTO1.getCount());
	                    }
	                }
	            }
	        }
	    }

	    otherChampions.sort((champion1, champion2) -> Double.compare(champion2.getStats().getWinRate(), champion1.getStats().getWinRate()));
	    
	    // 상위 5개 챔피언 추출
	    List<CounterChampionDTO> topChampions = otherChampions.stream().limit(5).collect(Collectors.toList());

	    // 하위 5개 챔피언 추출
	    List<CounterChampionDTO> bottomChampions = otherChampions.stream().sorted((champion1, champion2) -> Double.compare(champion1.getStats().getWinRate(), champion2.getStats().getWinRate())).limit(5).collect(Collectors.toList());

	    modelData.put("topChampions", topChampions);
	    modelData.put("bottomChampions", bottomChampions);

	    modelData.put("selectedChampionName", champion);
	    modelData.put("targetCounters", targetCounters);
	    modelData.put("otherChampions", otherChampions);
	    modelData.put("matchCounts", matchCounts);
	    modelData.put("selectedChampionName", champion);
	    
	    return modelData;
	}

	private List<CounterCountDTO> getPositionData(String position, CounterPositionDTO counterData) {
	    switch (position.toUpperCase()) {
	        case "TOP":
	            return counterData.getTop();
	        case "JUNGLE":
	            return counterData.getJungle();
	        case "MIDDLE":
	            return counterData.getMiddle();
	        case "BOTTOM":
	            return counterData.getBottom();
	        case "UTILITY":
	            return counterData.getUtility();
	        default:
	            return null;
	    }
	}
}
