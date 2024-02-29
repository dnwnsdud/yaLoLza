package com.web.project.page.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.dto.info.Perk;
import com.web.project.dto.info.Runes;
import com.web.project.dto.info.Champion.Champion;
import com.web.project.dto.info.Champion.Spell;
import com.web.project.dto.info.item.Item;
import com.web.project.dto.runeSpell.DataEntry;
import com.web.project.system.ChampionData;
import com.web.project.system.ItemData;
import com.web.project.system.RuneData;
import com.web.project.system.SummonerData;
import com.web.project.system.ChampionStatistic;


@Controller
@RequestMapping("/yalolza.gg/champions")
public class ChampionsPage {
	// 참고로 perk = rune
	private String defaultFilePath;

	public ChampionsPage() {
		System.out.println("CALL datafile");
	    this.defaultFilePath = "src/main/resources/static/datas/RANKED_SOLO_5x5/";	    
	    System.out.println(defaultFilePath);
	}

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
			@RequestParam("tier") String tier,
			@RequestParam("position") String position,
			@PathVariable("champion") String championid
			) {
		Champion champion = ChampionData.championinfo(championid);
		model.addAttribute("champion",champion);
//		String filePath = defaultFilePath + tier + "/data.json"; // 파일 경로 수정
//		String rawData = Files.readString(Paths.get(filePath));
//		List<DataEntry> data = parseJson(rawData);
//		List<DataEntry> filteredData = filterData(data, tier, position, championid);
		Runes runes = RuneData.runes(8000);
//		String primaryStyleFirstPerk = calculatePrimaryStyleFirstPerk1(filteredData);
//        List<String> primaryStylePerks234 = calculatePrimaryStylePerks234(filteredData);
		model.addAttribute("mainRune", runes);
		runes = RuneData.runes(8400);
//		List<String> subStylePerks12 = calculateSubStylePerks12(filteredData);
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
		model.addAttribute("item1", item);
		item = ItemData.item("1001");
		model.addAttribute("item2", item);
		item = ItemData.item("3364");
		model.addAttribute("item3", item);
		Map<String, String> summonerkey = SummonerData.keysSumSpell();
		model.addAttribute("summonerkey", summonerkey);
		return "champ_detail";
	}

	



}
