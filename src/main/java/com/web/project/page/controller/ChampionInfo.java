package com.web.project.page.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.project.dto.info.Perk;
import com.web.project.dto.info.Runes;
import com.web.project.dto.info.Champion.*;
import com.web.project.dto.info.item.Item;
import com.web.project.dto.info.rune.Rune;
import com.web.project.system.ChampionData;
import com.web.project.system.ItemData;
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
	@GetMapping("/itemdetailinfo/{id}")
	public String iteminfo(Model model,@PathVariable String id) {
		log.info("itemdetailinfo");	
		Item item = ItemData.item(id);
		model.addAttribute("item", item);
		return "itemdetailinfo";
	}
	@GetMapping("/championNameinfo/{id}")
	public String championNameinfo(Model model,@PathVariable String id) {
		log.info("championNameinfo");	
		Map<String, String> keysChamName = ChampionData.keysChamName();
		model.addAttribute("keysChamName", keysChamName);
		model.addAttribute("chamid", id);
		return "championName";
	}
	@GetMapping("/mapping/{championid}/mainruneid/subruneid/sum1id/sum2id/sum3id/sum4id/itemid")
	///mapping/Aatrox/8000/8400/6/21/1/7/1001
	public String mapping(Model model,
			@PathVariable String championid,
			@PathVariable Integer mainruneid,
			@PathVariable Integer subruneid,
			@PathVariable String sum1id,
			@PathVariable String sum2id,
			@PathVariable String sum3id,
			@PathVariable String sum4id,
			@PathVariable String itemid
			) {
		log.info("mapping");
		Champion champion = ChampionData.championinfo(championid);
		model.addAttribute("champion",champion);
		Runes runes = RuneData.runes(mainruneid);
		model.addAttribute("mainRune", runes);
		runes = RuneData.runes(subruneid);
		model.addAttribute("subRune", runes);
		List<Perk> perklist = RuneData.perklist();
		model.addAttribute("perklist", perklist);
		Spell spell = SummonerData.findspell(sum1id);
		model.addAttribute("summoner1", spell);
		spell = SummonerData.findspell(sum2id);
		model.addAttribute("summoner2", spell);
		spell = SummonerData.findspell(sum3id);
		model.addAttribute("summoner3", spell);
		spell = SummonerData.findspell(sum4id);
		model.addAttribute("summoner4", spell);
		Item item = ItemData.item(itemid);
		model.addAttribute("item", item);
		Map<String, String> summonerkey = SummonerData.keysSumSpell();
		model.addAttribute("summonerkey", summonerkey);
		Map<String, String> keysChamName = ChampionData.keysChamName();
		model.addAttribute("keysChamName", keysChamName);
		Map<Integer, String> keysRune = RuneData.keysRune();
		model.addAttribute("keysRune", keysRune);
		Map<Integer, String> keysPerk = RuneData.keysPerk();
		model.addAttribute("keysPerk", keysPerk);
		return "mapping";
	}
	
}
