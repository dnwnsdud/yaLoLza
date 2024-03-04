package com.web.project.page.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import com.web.project.dto.runeSpell.DataEntry;
import com.web.project.dto.runeSpell.SummonerSpellSetWinRate;
import com.web.project.metrics.count.Connect;
import com.web.project.system.ChampionData;
import com.web.project.system.CounterJsonReader;
import com.web.project.system.ItemData;
import com.web.project.system.JsonReader;
import com.web.project.system.RuneData;
import com.web.project.system.SummonerData;

import lombok.NoArgsConstructor;

import com.web.project.system.StatisticChampion;


@Controller
@RequestMapping("/yalolza.gg/champions")
public class ChampionsPage {
	private final JsonReader jsonReader;
	private final CounterJsonReader counterJsonReader;
	// 참고로 perk = rune
	private String defaultFilePath;

//	public ChampionsPage() {
//		System.out.println("CALL datafile");
//	    this.defaultFilePath = "src/main/resources/static/datas/RANKED_SOLO_5x5/";	    
//	    System.out.println(defaultFilePath);
//	}


	public ChampionsPage(JsonReader jsonReader, CounterJsonReader counterJsonReader) {
		this.jsonReader = jsonReader;
		this.counterJsonReader = counterJsonReader;
		this.defaultFilePath = "src/main/resources/static/datas/RANKED_SOLO_5x5/";	  
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
    	new Connect("total","yalolza.gg", "champions");
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

	@GetMapping("/res")
	public String Champions(@RequestParam(required = false) String position) {
		return "NewFile";
	}
	@GetMapping("/runeview")
	public String runeview(Model model) {
		model.addAttribute("num1", 8000);
		model.addAttribute("num2", 8100);
		List<Runes> runeslist = RuneData.runeslist();
		model.addAttribute("runeslist", runeslist);
		Map<Integer, Integer> keysRunes=RuneData.keysRunes();
		model.addAttribute("keysRunes", keysRunes);
		Runes runes = RuneData.runes(8100);
		model.addAttribute("mainRune", runes);
		model.addAttribute("subRune", runes);
		return "runeview";
	}
 
	@GetMapping("/{champion}/build/{position}")
	public String ChampionsDetail(@PathVariable("champion") String champion,
			@PathVariable("position") String position) {
		return "";
	}
	@GetMapping("/{champion}/build")  //http://localhost:9998/yalolza.gg/champions/Aatrox/build?tier=EMERALD&position=TOP
	public String ChampionsDetail(
			Model model,
			@RequestParam(name="tier",required = false, defaultValue = "EMERALD") String tier,
			@RequestParam(name="position",required = false, defaultValue = "TOP") String position,
			@PathVariable("champion") String championid
			) {
		Champion champion = ChampionData.championinfo(championid);
		model.addAttribute("champion",champion);
		String filePath = defaultFilePath + tier + "/data.json"; // 파일 경로 수정
		try {
			String rawData = Files.readString(Paths.get(filePath));
			List<DataEntry> data = StatisticChampion.parseJson(rawData);
			List<DataEntry> filteredData = StatisticChampion.filterData(data, tier, position, championid);
			
			Integer mainStyle = Integer.parseInt(StatisticChampion.mainStyle(filteredData));
			System.out.println(mainStyle);
			Runes runes = RuneData.runes(mainStyle);
			model.addAttribute("mainRune", runes);
			String primaryStyleFirstPerk = StatisticChampion.calculatePrimaryStyleFirstPerk1(filteredData);
			List<String> primaryStylePerks234 = StatisticChampion.calculatePrimaryStylePerks234(filteredData);
			model.addAttribute("primaryPerk1", primaryStyleFirstPerk);
			model.addAttribute("primaryPerk234", primaryStylePerks234);
			runes = RuneData.runes(8400);
			List<String> subStylePerks12 = StatisticChampion.calculateSubStylePerks12(filteredData);
			model.addAttribute("secondaryPerk12", subStylePerks12);
			model.addAttribute("subRune", runes);
			List<Perk> perklist = RuneData.perklist();
			double runeWinRate = StatisticChampion.calculateRuneWinRate(filteredData,primaryStyleFirstPerk);
            List<SummonerSpellSetWinRate> summonerSpellSet12 = StatisticChampion.calculateSummonerSpellSet(filteredData);
            List<Integer> Spelllist1 = new ArrayList<Integer>(summonerSpellSet12.get(0).getSpellSet());
            List<Integer> Spelllist2 = new ArrayList<Integer>(summonerSpellSet12.get(1).getSpellSet());
            출처: https://hianna.tistory.com/555 [어제 오늘 내일:티스토리]
			model.addAttribute("perklist", perklist);
			Spell spell = SummonerData.findspell(Spelllist1.get(0).toString());
			model.addAttribute("summoner1", spell);
			spell = SummonerData.findspell(Spelllist1.get(1).toString());
			model.addAttribute("summoner2", spell);
			model.addAttribute("summonerSpellSet1Win", ((double)Math.round(summonerSpellSet12.get(0).getWinRate()*10000)/100));
			spell = SummonerData.findspell(Spelllist2.get(0).toString());
			model.addAttribute("summoner3", spell);
			spell = SummonerData.findspell(Spelllist2.get(1).toString());
			model.addAttribute("summoner4", spell);
			model.addAttribute("summonerSpellSet2Win", ((double)Math.round(summonerSpellSet12.get(1).getWinRate()*10000)/100));
		} catch (IOException e) {
			e.printStackTrace();
            System.out.println("no DATA");
		}
		Item item = ItemData.item("1001");
		model.addAttribute("item1", item);
		item = ItemData.item("1001");
		model.addAttribute("item2", item);
		item = ItemData.item("3364");
		model.addAttribute("item3", item);
		Map<String, String> summonerkey = SummonerData.keysSumSpell();
		model.addAttribute("summonerkey", summonerkey);
    	new Connect("total","yalolza.gg", "champions","detail");
		return "champ_detail";
	}
 

}
