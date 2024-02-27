package com.web.project.page.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.dto.ChampionStatsDTO;
import com.web.project.dto.TierDataDTO;
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
				List<ChampionStatsDTO> positionData = tierData.getPositions().get(position);
				

				// TierScore로 정렬
				positionData.sort((champ1, champ2) -> Double.compare(champ2.getStats().getTierScore(),
						champ1.getStats().getTierScore()));

				model.addAttribute("positionData", positionData);
			} else {
				model.addAttribute("error", "Invalid tier or position");
			}
		} catch (Exception e) {
			model.addAttribute("error", "ERROR: " + e.getMessage());
		}
		return "positionView";
	}

//    @GetMapping("/changeTierPosition")
//    public String changeTierPosition(@RequestParam String tier, @RequestParam String position) {
//        return "redirect:/lol?tier=" + tier + "&position=" + position;
//    }

}
