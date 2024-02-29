package com.web.project.page.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.dto.championstats.ChampionStatsDTO;
import com.web.project.dto.championstats.CounterChampionDTO;
import com.web.project.dto.championstats.CounterCountDTO;
import com.web.project.dto.championstats.CounterPositionDTO;
import com.web.project.dto.championstats.TierDataDTO;
import com.web.project.dto.info.Perk;
import com.web.project.dto.info.Runes;
import com.web.project.dto.info.Champion.Champion;
import com.web.project.dto.info.Champion.Spell;
import com.web.project.dto.info.item.Item;
import com.web.project.system.ChampionData;
import com.web.project.system.CounterJsonReader;
import com.web.project.system.ItemData;
import com.web.project.system.JsonReader;
import com.web.project.system.RuneData;
import com.web.project.system.SummonerData;

@Controller
@RequestMapping("/yalolza.gg/champions")
public class ChampionsPage {
	private final JsonReader jsonReader;
	private final CounterJsonReader counterJsonReader;

	@Autowired
	public ChampionsPage(JsonReader jsonReader, CounterJsonReader counterJsonReader) {
		this.jsonReader = jsonReader;
		this.counterJsonReader = counterJsonReader;
	}
	//챔피언 전체 페이지 - 진성+진비
	@GetMapping("")
	public String getChampionsData(@RequestParam(required = false, defaultValue = "EMERALD") String tier,
			@RequestParam(required = false, defaultValue = "TOP") String position, Model model) {
		List<Champion> data = ChampionData.imagedata();
		model.addAttribute("data", data);
		try {
	        if (tier != null && position != null) {
	            TierDataDTO tierData = jsonReader.readJsonFile(tier);
	            for(String key : tierData.getPositions().keySet()) {
	            	for(ChampionStatsDTO dto : tierData.getPositions().get(key)) {
	            		dto.setPosition(key);
	            	}
	            }
	            List<ChampionStatsDTO> allPositionData = new ArrayList<>();
	            
	            // 포지션 전체 ALL 추가
	            if (position.equals("ALL")) {	                
	                for (List<ChampionStatsDTO> positionData : tierData.getPositions().values()) {
	                    allPositionData.addAll(positionData);
	                }
	            } else {
	                allPositionData = tierData.getPositions().get(position);
	            }

				// 선택한 포지션의 모든 챔피언 데이터를 가져옴
				if (position.equals("ALL")) {
					for (List<ChampionStatsDTO> positionData : tierData.getPositions().values()) {
						allPositionData.addAll(positionData);
					}
				} else {
					allPositionData = tierData.getPositions().get(position);
				}

				// 챔피언 데이터를 티어 점수를 기준으로 내림차순으로 정렬
				allPositionData.sort((champ1, champ2) -> Double.compare(champ2.getStats().getTierScore(),
						champ1.getStats().getTierScore()));

				// 중복된 챔피언을 제거
				List<ChampionStatsDTO> uniqueChampions = new ArrayList<>();
				Set<String> addedChampions = new HashSet<>();
				for (ChampionStatsDTO champion : allPositionData) {
					if (!addedChampions.contains(champion.getChampionName())) {
						uniqueChampions.add(champion);
						addedChampions.add(champion.getChampionName());
					}
				}
				model.addAttribute("selectedTier", tier);
				model.addAttribute("positionData", uniqueChampions);
			} else {
				model.addAttribute("error", "Invalid tier or position");
			}
		} catch (Exception e) {
			model.addAttribute("error", "ERROR: " + e.getMessage());
		}
		return "champ";
	}

	@GetMapping("/{champion}/counter/{position}")
	public String getCounterData(@PathVariable String position, @PathVariable("champion") String champion,
			@RequestParam(name = "champion", required = false) String additionalChampion, Model model) {
		try {
			// 카운터 데이터를 읽어옴
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
					// 선택된 챔피언이 포함된 CounterChampionDTO 객체만 찾기
					if (countDTO1.getCounter().stream()
							.anyMatch(championDTO -> championDTO.getChampionName().equalsIgnoreCase(champion))) {
						for (CounterChampionDTO championDTO : countDTO1.getCounter()) {
							if (!championDTO.getChampionName().equalsIgnoreCase(champion)
									&& !otherChampions.contains(championDTO)) {
								otherChampions.add(championDTO);
								matchCounts.put(championDTO.getChampionName(), countDTO1.getCount());
							}
						}
					}
				}
			}

			// 다른 챔피언의 리스트를 승률을 기준으로 내림차순으로 정렬
			otherChampions.sort((champion1, champion2) -> Double.compare(champion2.getStats().getWinRate(),
					champion1.getStats().getWinRate()));

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

	// 포지션 데이터를 가져오는 메서드
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

	@GetMapping("/")
	public String Champions(@RequestParam(required = false) String position) {
		return "NewFile";
	}

	@GetMapping("/{champion}/build/{position}")
	public String ChampionsDetail(@PathVariable("champion") String champion,
			@PathVariable("position") String position) {
		return "";
	}
	//챔피언 상세 페이지- 지수+진비
	@GetMapping("/{champion}/build")
	public String ChampionsDetail(Model model, @PathVariable("champion") String championid) {
		Champion champion = ChampionData.championinfo(championid);
		model.addAttribute("champion", champion);
		Runes runes = RuneData.runes(8000);
		model.addAttribute("mainRune", runes);
		runes = RuneData.runes(8400);
		model.addAttribute("subRune", runes);
		List<Perk> perklist = RuneData.perklist();
		model.addAttribute("perklist", perklist);
		Spell spell = SummonerData.findspell("6");
		model.addAttribute("summoner1", spell);
		spell = SummonerData.findspell("21");
		model.addAttribute("summoner2", spell);
		spell = SummonerData.findspell("1");
		model.addAttribute("summoner3", spell);
		spell = SummonerData.findspell("7");
		model.addAttribute("summoner4", spell);
		Item item = ItemData.item("1001");
		model.addAttribute("item", item);
		Map<String, String> summonerkey = SummonerData.keysSumSpell();
		model.addAttribute("summonerkey", summonerkey);
		return "champ_detail";
	}

}
