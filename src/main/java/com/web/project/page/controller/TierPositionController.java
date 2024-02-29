package com.web.project.page.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.dto.championstats.ChampionStatsDTO;
import com.web.project.dto.championstats.TierDataDTO;
import com.web.project.system.JsonReader;

import java.util.List;

@Controller
public class TierPositionController {

	private final JsonReader jsonReader;

	@Autowired
	public TierPositionController(JsonReader jsonReader) {
		this.jsonReader = jsonReader;
	}

	@GetMapping("/lol") // http://localhost:9998/lol?tier=IRON&position=TOP
	public String getChampionsData(@RequestParam(required = false) String tier,
	        @RequestParam(required = false) String position, Model model) {
	    try {
	        if (tier != null && position != null) {
	            TierDataDTO tierData = jsonReader.readJsonFile(tier);
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
	    return "positionView";
	}

}
