package com.web.project.page.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.project.dto.Perk;
import com.web.project.dto.Champion.*;
import com.web.project.dto.rune.Rune;
import com.web.project.system.ChampionData;
import com.web.project.system.RuneData;
import com.web.project.system.SummonerData;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/info")
@Slf4j
public class ChampionInfo {
	@GetMapping("/passive/{id}")
	public String championpassive (Model model, @PathVariable String id) {  
		log.info("passive");
		Champion champion = ChampionData.championinfo(id);
		Passive passive = champion.getPassive();
		model.addAttribute("passive",passive);
		return "championpassive";
	}
	@GetMapping("/spell/{championid}/{key}")
	public String championspell (Model model, 
							@PathVariable String championid,
							@PathVariable String key) {  
		log.info("spell");
		Champion champion = ChampionData.championinfo(championid);
		List<Spell> spells = champion.getSpells();
		int index = 0;
		if(key.equalsIgnoreCase("Q")) index = 0;
		else if(key.equalsIgnoreCase("W")) index = 1;
		else if(key.equalsIgnoreCase("E")) index = 2;
		else if(key.equalsIgnoreCase("R")) index = 3;
		Spell spell = spells.get(index);
		model.addAttribute("spell",spell);
		return "championspell";
	}
	@GetMapping("/runedetailinfo/{id}")
    public String runedetailinfo(Model model,@PathVariable Integer id) {
	    log.info("runedetailinfo");		  
	    Rune rune = RuneData.runedetail(id);
        model.addAttribute("rune", rune);
      
        return "runedetailinfo";
    }
	@GetMapping("/perkdetailinfo/{id}")
	public String perkjson(Model model,@PathVariable Integer id) {
		log.info("perkdetailinfo");	
		Perk perk = RuneData.perk(id);
		model.addAttribute("perk", perk);
		return "perkdetailinfo";
	}
	@GetMapping("/summonerinfo/{id}")
	public String summonerspellinfo (Model model, @PathVariable("id") String id) {
		log.info("summonerspellinfo");	
		Spell spell = SummonerData.findspell(id);
		model.addAttribute("spell", spell);
		Map<String, String> summonerkey = SummonerData.keysSumSpell();
		model.addAttribute("summonerkey", summonerkey);
		return "summonerspellinfo";
	}
}
