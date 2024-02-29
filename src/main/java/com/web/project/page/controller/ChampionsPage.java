package com.web.project.page.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.dto.ChampionStatsDTO;
import com.web.project.dto.TierDataDTO;
import com.web.project.dto.info.Perk;
import com.web.project.dto.info.Runes;
import com.web.project.dto.info.Champion.Champion;
import com.web.project.dto.info.Champion.Spell;
import com.web.project.dto.info.item.Item;
import com.web.project.system.ChampionData;
import com.web.project.system.ItemData;
import com.web.project.system.JsonReader;
import com.web.project.system.RuneData;
import com.web.project.system.SummonerData;

@Controller
@RequestMapping("/yalolza.gg/champions")
public class ChampionsPage {
	private final JsonReader jsonReader;

	@Autowired
	public ChampionsPage(JsonReader jsonReader) {
		this.jsonReader = jsonReader;
	}
	//챔피언 전체 페이지 - 진성+진비
	@GetMapping("")
	public String Champions(@RequestParam(required = false) String tier,
			Model model,
			@RequestParam(required = false) String position
			) {
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

	            // TierScore로 정렬
	            allPositionData.sort((champ1, champ2) -> Double.compare(champ2.getStats().getTierScore(),
	                    champ1.getStats().getTierScore()));

	            // ALL 중복 챔피언 제거
	            List<ChampionStatsDTO> uniqueChampions = new ArrayList<>();
	            Set<String> addedChampions = new HashSet<>();
	            for (ChampionStatsDTO champion : allPositionData) {
	                if (!addedChampions.contains(champion.getChampionName())) {
	                    uniqueChampions.add(champion);
	                    addedChampions.add(champion.getChampionName());
	                }
	            }
	            model.addAttribute("positionData", uniqueChampions);
	        } else {
	            model.addAttribute("error", "Invalid tier or position");
	        }
	    } catch (Exception e) {
	        model.addAttribute("error", "ERROR: " + e.getMessage());
	    }
		return "champ";
	}
	@GetMapping("/")
	public String Champions(
			@RequestParam(required = false) String position
			) {
		return "NewFile";
	}
	
	@GetMapping("/{champion}/build/{position}")
	public String ChampionsDetail(
			@PathVariable("champion") String champion, 
			@PathVariable("position") String position
			) {
		return "";
	}
	//챔피언 상세 페이지- 지수+진비
	@GetMapping("/{champion}/build")
	public String ChampionsDetail(
			Model model,
			@PathVariable("champion") String championid
			) {
		Champion champion = ChampionData.championinfo(championid);
		model.addAttribute("champion",champion);
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
