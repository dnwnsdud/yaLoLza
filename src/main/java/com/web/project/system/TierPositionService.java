package com.web.project.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

	public List<ChampionStatsDTO> getChampionsData(String tier, String position, Model model) {
		try {
			TierDataDTO tierData = jsonReader.readJsonFile(tier);
			List<ChampionStatsDTO> allChampions = new ArrayList<>();
			Map<String, String> championOriginalPositions = new HashMap<>();
			Map<String, ChampionStatsDTO> bestChampions = new HashMap<>();

			if (position.equals("ALL")) {
				for (Map.Entry<String, List<ChampionStatsDTO>> entry : tierData.getPositions().entrySet()) {
					for (ChampionStatsDTO champion : entry.getValue()) {
						ChampionStatsDTO bestChampion = bestChampions.get(champion.getChampionName());
						if (bestChampion == null
								|| champion.getStats().getTierScore() > bestChampion.getStats().getTierScore()) {
							bestChampions.put(champion.getChampionName(), champion);
							championOriginalPositions.put(champion.getChampionName(), entry.getKey());
						}
					}
				}
				allChampions.addAll(bestChampions.values());
				model.addAttribute("championOriginalPositions", championOriginalPositions);
			} else {
				allChampions.addAll(tierData.getPositions().getOrDefault(position, Collections.emptyList()));
			}

			allChampions.sort((champ1, champ2) -> Double.compare(champ2.getStats().getTierScore(),
					champ1.getStats().getTierScore()));
			return allChampions;
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
				allPositionData = tierData.getPositions().values().stream().flatMap(List::stream)
						.collect(Collectors.toList());
			} else {
				allPositionData = new ArrayList<>(tierData.getPositions().get(position));
			}

			return allPositionData.stream()
					.filter(champion -> champion.getChampionName().equalsIgnoreCase(championName)).findFirst()
					.orElse(null);
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return null;
		}
	}

	public Map<String, String> getChampionPositions(String tier, String position) {
		try {
			TierDataDTO tierData = jsonReader.readJsonFile(tier);
			Map<String, String> championPositions = new HashMap<>();
			if (position.equals("ALL")) {
				for (Map.Entry<String, List<ChampionStatsDTO>> entry : tierData.getPositions().entrySet()) {
					for (ChampionStatsDTO champion : entry.getValue()) {
						championPositions.put(champion.getChampionName(), entry.getKey());
					}
				}
			}
			return championPositions;
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return Collections.emptyMap();
		}
	}

}