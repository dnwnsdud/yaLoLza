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
import com.web.project.system.CounterDataService;
import com.web.project.system.CounterJsonReader;
import com.web.project.system.ItemData;
import com.web.project.system.JsonReader;
import com.web.project.system.RuneData;
import com.web.project.system.StatisticChampion;
import com.web.project.system.SummonerData;
import com.web.project.system.TierPositionService;

import lombok.NoArgsConstructor;

@Controller
@RequestMapping("/yalolza.gg/champions")
public class ChampionsPage {
	private final JsonReader jsonReader;
	private final CounterJsonReader counterJsonReader;
	private final CounterDataService counterDataService;
	private final TierPositionService tierPositionService;
	// 참고로 perk = rune
	private String defaultFilePath;

//	public ChampionsPage() {
//		System.out.println("CALL datafile");
//	    this.defaultFilePath = "src/main/resources/static/datas/RANKED_SOLO_5x5/";	    
//	    System.out.println(defaultFilePath);
//	}

	public ChampionsPage(JsonReader jsonReader, CounterJsonReader counterJsonReader,
			CounterDataService counterDataService, TierPositionService tierPositionService) {
		this.jsonReader = jsonReader;
		this.counterJsonReader = counterJsonReader;
		this.counterDataService = counterDataService;
		this.tierPositionService = tierPositionService;
		this.defaultFilePath = "src/main/resources/static/datas/RANKED_SOLO_5x5/";
	}
	//챔피언 전체 페이지 - 진성+진비
	@GetMapping("")
	public String getChampionsData(@RequestParam(required = false, defaultValue = "EMERALD") String tier,
			@RequestParam(required = false, defaultValue = "TOP") String position, Model model) {
//		List<Champion> data = ChampionData.imagedata();
//		model.addAttribute("data", data);
		List<ChampionStatsDTO> uniqueChampions = tierPositionService.getChampionsData(tier, position);
		model.addAttribute("selectedTier", tier);
		model.addAttribute("positionData", uniqueChampions);
		model.addAttribute("position", position);

		return "champ";
	}
	
	@GetMapping("/{champion}/counter/{position}") // http://localhost:9998/yalolza.gg/champions/jax/counter/top
	public String getCounterData(@PathVariable String position, @PathVariable("champion") String champion,
			@RequestParam(name = "champion", required = false) String additionalChampion, Model model) {
		try {
			Map<String, Object> modelData = counterDataService.getCounterData(position, champion, additionalChampion);
			model.addAllAttributes(modelData);
			return "counter_detail";
		} catch (IOException e) {
			model.addAttribute("error", "error: " + e.getMessage());
			return "error";
		}
	}
	

	@GetMapping("/res")
	public String Champions(@RequestParam(required = false) String position) {
		return "NewFile";
	}

	@GetMapping("/{champion}/build/{position}")
	public String ChampionsDetail(@PathVariable("champion") String champion,
			@PathVariable("position") String position) {
		return "";
	}
	
	
	@GetMapping("/{champion}/build")  //http://localhost:9998/yalolza.gg/champions/Aatrox/build?tier=EMERALD&position=TOP
	public String ChampionsDetail(
			Model model,
			@PathVariable String champion,
			@RequestParam(name="tier",required = false, defaultValue = "EMERALD") String tier,
			@RequestParam(name="position",required = false, defaultValue = "TOP") String position,
			@RequestParam(name = "champion", required = false) String additionalChampion,
			@PathVariable("champion") String championid
			) {
		
	    ChampionStatsDTO championData = tierPositionService.getChampionDataByName(tier, position, champion);
	    
	    if (championData != null) {
	        model.addAttribute("champion", championData);
	        model.addAttribute("winrate", championData.getStats().getWinrate());
	        model.addAttribute("pickrate", championData.getStats().getPickrate());
	        model.addAttribute("banrate", championData.getStats().getBanrate());
	        championData.getStats().getWinrate();
	        championData.getStats().getPickrate();
	        championData.getStats().getBanrate();
	    } else {
	        model.addAttribute("error", "챔피언 데이터를 찾을 수 없습니다: " + champion);
	        return "error";
	    }

	    try {
	        Map<String, Object> counterData = counterDataService.getCounterData(position, champion, additionalChampion);
	        model.addAllAttributes(counterData);
	    } catch (IOException e) {
	        model.addAttribute("error", "Data loading error: " + e.getMessage());
	        return "error";
	    }

	    List<ChampionStatsDTO> uniqueChampions = tierPositionService.getChampionsData(tier, position);
	    model.addAttribute("selectedTier", tier);
	    model.addAttribute("positionData", uniqueChampions);
	    model.addAttribute("position", position);
		
		Champion champion1 = ChampionData.championinfo(championid);
		model.addAttribute("champion",champion1);
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
