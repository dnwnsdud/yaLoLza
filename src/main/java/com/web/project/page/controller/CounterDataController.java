package com.web.project.page.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.dto.CounterChampionDTO;
import com.web.project.dto.CounterCountDTO;
import com.web.project.dto.CounterPositionDTO;
import com.web.project.system.CounterJsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CounterDataController {

	private final CounterJsonReader counterJsonReader;

	@Autowired
	public CounterDataController(CounterJsonReader counterJsonReader) {
		this.counterJsonReader = counterJsonReader;
	}

	@GetMapping("/counter_detail/{position}/champion/{champion}")
	public String getCounterData(@PathVariable String position, @PathVariable("champion") String champion,
			@RequestParam(name = "champion", required = false) String additionalChampion, Model model) {
		try {
			CounterPositionDTO counterData = counterJsonReader.readCounterJsonFile();
			List<CounterCountDTO> positionData = getPositionData(position, counterData);

			if (positionData == null) {
				return "error";
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
				    // 선택된 챔피언(darius)이 포함된 CounterChampionDTO 객체만 찾기
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

		    model.addAttribute("selectedChampionName", champion);
		    model.addAttribute("additionalChampionName", additionalChampion);
		    model.addAttribute("targetCounters", targetCounters);
		    model.addAttribute("otherChampions", otherChampions);
		    model.addAttribute("matchCounts", matchCounts);

			return "counter_detail";
		} catch (IOException e) {
			model.addAttribute("error", "Data loading error: " + e.getMessage());
			return "error";
		}
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
