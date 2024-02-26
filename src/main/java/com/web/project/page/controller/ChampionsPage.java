package com.web.project.page.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.dto.Perk;
import com.web.project.dto.Runes;
import com.web.project.dto.Champion.Champion;
import com.web.project.dto.Champion.Spell;
import com.web.project.system.ChampionData;
import com.web.project.system.RuneData;
import com.web.project.system.SummonerData;

@Controller
@RequestMapping("/yalolza.gg/champions")
public class ChampionsPage {

	@GetMapping("")
	public String Champions(Model model,
			@RequestParam(required = false) String position
			) {
		List<Champion> data = ChampionData.imagedata();
		model.addAttribute("data", data);
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
		Map<String, String> summonerkey = SummonerData.keysSumSpell();
		model.addAttribute("summonerkey", summonerkey);
		
		return "champ_detail";
	}

	



}
