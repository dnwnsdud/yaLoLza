package com.web.project.page.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import com.web.project.dto.runeSpell.SummonerSpellSetWinRate;
import com.web.project.system.ChampionData;
import com.web.project.system.ItemData;
import com.web.project.system.RuneData;
import com.web.project.system.SummonerData;
import com.web.project.system.StatisticChampion;


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
	public String NewFile(
			Model model
//			@RequestParam(required = false) String position
			) {
		String filePath = defaultFilePath + "EMERALD" + "/data.json"; // 파일 경로 수정
		try {
			String rawData = Files.readString(Paths.get(filePath));
			List<DataEntry> data = StatisticChampion.parseJson(rawData);
			List<DataEntry> filteredData = StatisticChampion.filterData(data, "EMERALD", "TOP", "Aatrox");
			Runes runes = RuneData.runes(8000);
			model.addAttribute("mainRune", runes);
			
			//######메인룬 가장 많이 선택된거 하나 반환해요
			String primaryStyleFirstPerk = StatisticChampion.calculatePrimaryStyleFirstPerk1(filteredData).get(0);
			List<String> primaryStylePerks234 = StatisticChampion.calculatePrimaryStylePerks234(filteredData);
			model.addAttribute("primaryPerk1", primaryStyleFirstPerk);
			model.addAttribute("primaryPerk234", primaryStylePerks234);
			runes = RuneData.runes(8400);
			List<String> subStylePerks12 = StatisticChampion.calculateSubStylePerks12(filteredData);
			System.out.println(subStylePerks12);
			model.addAttribute("secondaryPerk12", subStylePerks12);
			model.addAttribute("subRune", runes);
		} catch (IOException e) {
			e.printStackTrace();
            System.out.println("no DATA");
		}
		return "NewFile";
	}
	
	@GetMapping("/{champion}/build/{position}")
	public String ChampionsDetail(
			@PathVariable("champion") String champion, 
			@PathVariable("position") String position
			) {
		return "";
	}
	@GetMapping("/{champion}/build")  //http://localhost:9998/yalolza.gg/champions/Aatrox/build?tier=EMERALD&position=TOP
	public String ChampionsDetail(
			Model model,
			@RequestParam("tier") String tier,
			@RequestParam("position") String position,
			@PathVariable("champion") String championid
			) {
		Champion champion = ChampionData.championinfo(championid);
		model.addAttribute("champion",champion);
		String filePath = defaultFilePath + tier + "/data.json"; // 파일 경로 수정
		try {
			String rawData = Files.readString(Paths.get(filePath));
			List<DataEntry> data = StatisticChampion.parseJson(rawData);
			List<DataEntry> filteredData = StatisticChampion.filterData(data, tier, position, championid);
			
			//인덱스 0 = 첫번째로 많이 등장한 메인룬
			List<String> primaryStyleFirstPerk = StatisticChampion.calculatePrimaryStyleFirstPerk1(filteredData);
			System.out.println(primaryStyleFirstPerk.get(0));
			System.out.println(primaryStyleFirstPerk.get(1));
			//#####
			Integer mainStyle = Integer.parseInt(StatisticChampion.mainStyle(filteredData, primaryStyleFirstPerk.get(0)));
			System.out.println("mainStyle : " +mainStyle);
			Runes runes = RuneData.runes(mainStyle);
			//####
			model.addAttribute("mainRune", runes);
			List<String> primaryStylePerks234 = StatisticChampion.calculatePrimaryStylePerks234(filteredData);
			model.addAttribute("primaryPerk1", primaryStyleFirstPerk);
			model.addAttribute("primaryPerk234", primaryStylePerks234);
			
			runes = RuneData.runes(8400);
			List<String> subStylePerks12 = StatisticChampion.calculateSubStylePerks12(filteredData);
			model.addAttribute("secondaryPerk12", subStylePerks12);
			model.addAttribute("subRune", runes);
			List<Perk> perklist = RuneData.perklist();
			double runeWinRate = StatisticChampion.calculateRuneWinRate(filteredData,primaryStyleFirstPerk.get(0));
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
		return "champ_detail";
	}



	

	



}
