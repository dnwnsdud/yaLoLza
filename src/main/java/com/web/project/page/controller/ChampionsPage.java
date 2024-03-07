package com.web.project.page.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.api.controller.YoutubeService;
import com.web.project.dto.championstats.ChampionStatsDTO;
import com.web.project.dto.info.Perk;
import com.web.project.dto.info.Runes;
import com.web.project.dto.info.Champion.Champion;
import com.web.project.dto.info.Champion.Spell;
import com.web.project.dto.info.item.Item;
import com.web.project.dto.runeSpell.DataEntry;
import com.web.project.dto.runeSpell.ItemListStatistics;
import com.web.project.dto.runeSpell.ItemStatistics;
import com.web.project.dto.runeSpell.ItemWinRate;
import com.web.project.dto.runeSpell.RuneWinRate;
import com.web.project.dto.runeSpell.SkillStatistics;
import com.web.project.dto.runeSpell.StatPerks;
import com.web.project.dto.runeSpell.SummonerSpellSetWinRate;
import com.web.project.metrics.count.Connect;
import com.web.project.system.ChampionData;
import com.web.project.system.CounterDataService;
import com.web.project.system.CounterJsonReader;
import com.web.project.system.ItemData;
import com.web.project.system.JsonReader;
import com.web.project.system.RuneData;
import com.web.project.system.StatisticChampion;
import com.web.project.system.StatisticItemSkill;
import com.web.project.system.SummonerData;
import com.web.project.system.TierPositionService;
 

@Controller
@RequestMapping("/yalolza.gg/champions")
public class ChampionsPage {
	
	 @Autowired
	    private YoutubeService youtubeService;
	
	
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
		List<Champion> data = ChampionData.imagedata();
		model.addAttribute("data", data);
	    List<ChampionStatsDTO> uniqueChampions = tierPositionService.getChampionsData(tier, position, model);
	    Map<String, String> championPositions = tierPositionService.getChampionPositions(tier, position);
	    model.addAttribute("selectedTier", tier);
	    model.addAttribute("positionData", uniqueChampions);
	    model.addAttribute("position", position);
	    model.addAttribute("championPositions", championPositions);
	    return "champ";
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
	        model.addAttribute("champtier", championData.getStats().getChamptier());
	        championData.getStats().getWinrate();
	        championData.getStats().getPickrate();
	        championData.getStats().getBanrate();
	        championData.getStats().getChamptier();
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

	    List<ChampionStatsDTO> uniqueChampions = tierPositionService.getChampionsData(tier, position, model);
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
			
			
			//인덱스 0 = 첫번째로 많이 등장한 메인룬
			List<RuneWinRate> primaryStyleFirstPerk = StatisticChampion.calculatePrimaryStyleFirstPerk1(filteredData);
			
			
			model.addAttribute("runePickRate1",(double)Math.round(primaryStyleFirstPerk.get(0).getPickRate()*10000)/100);
			model.addAttribute("runeCount1",primaryStyleFirstPerk.get(0).getSetCount());
			model.addAttribute("runeWinRate1",(double)Math.round(primaryStyleFirstPerk.get(0).getWinRate()*10000)/100);
			
			model.addAttribute("runePickRate2",(double)Math.round(primaryStyleFirstPerk.get(1).getPickRate()*10000)/100);
			model.addAttribute("runeCount2",primaryStyleFirstPerk.get(1).getSetCount());
			model.addAttribute("runeWinRate2",(double)Math.round(primaryStyleFirstPerk.get(1).getWinRate()*10000)/100);
	

			
			//룬 등장 횟수
			int RuneGameCount = StatisticChampion.calculateRuneGameCount(filteredData, primaryStyleFirstPerk.get(0).getMainRune());
			System.out.println("playedrunecount :" +RuneGameCount);
			//#####
			Integer mainStyle = Integer.parseInt(StatisticChampion.mainStyle(filteredData, primaryStyleFirstPerk.get(0).getMainRune()));
			System.out.println("mainStyle : " +mainStyle);
			Runes runes = RuneData.runes(mainStyle);
			//####
			model.addAttribute("mainRune", runes);
			List<Integer> primaryStylePerks234 = StatisticChampion.calculatePrimaryStylePerks234(filteredData,primaryStyleFirstPerk.get(0).getMainRune());
			model.addAttribute("primaryPerk1", primaryStyleFirstPerk);
			model.addAttribute("primaryPerk234", primaryStylePerks234);
			System.out.println(primaryStylePerks234);
			System.out.println("메인룬 1 : " + primaryStylePerks234.get(0));
			System.out.println("룬 2 : " + primaryStylePerks234.get(1));
			System.out.println("룬 3 : " + primaryStylePerks234.get(2));
			System.out.println("룬 4 : " + primaryStylePerks234.get(3));
			

			
			Integer subStyle = Integer.parseInt(StatisticChampion.subStyle(filteredData, primaryStyleFirstPerk.get(0).getMainRune()));			
			System.out.println("서브룬 : " +subStyle);
			runes = RuneData.runes(subStyle);
			
			List<Integer> subStylePerks12 = StatisticChampion.calculateSubStylePerks12(filteredData,primaryStyleFirstPerk.get(0).getMainRune());
			System.out.println(subStylePerks12);
			
			model.addAttribute("secondaryPerk12", subStylePerks12);
			model.addAttribute("subRune", runes);
			List<Perk> perklist = RuneData.perklist();
			double runeWinRate = StatisticChampion.calculateRuneWinRate(filteredData,primaryStyleFirstPerk.get(0).getMainRune());
            List<SummonerSpellSetWinRate> summonerSpellSet12 = StatisticChampion.calculateSummonerSpellSet(filteredData);
            List<Integer> Spelllist1 = new ArrayList<Integer>(summonerSpellSet12.get(0).getSpellSet());
            List<Integer> Spelllist2 = new ArrayList<Integer>(summonerSpellSet12.get(1).getSpellSet());
            //출처: https://hianna.tistory.com/555 [어제 오늘 내일:티스토리]
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
			
			model.addAttribute("summonerSpellSetCountRate1", ((double)Math.round(summonerSpellSet12.get(0).getCountRate()*10000)/100));
			model.addAttribute("summonerSpellSet1Count1", summonerSpellSet12.get(0).getSetCount());
			model.addAttribute("summonerSpellSetCountRate2", ((double)Math.round(summonerSpellSet12.get(1).getCountRate()*10000)/100));
			model.addAttribute("summonerSpellSet1Count2", summonerSpellSet12.get(1).getSetCount());
			
			
			
			Map<Long, String> keysRuneImage=RuneData.keysRuneImage();							

			Long Runeimage = Long.valueOf(mainStyle);				
			keysRuneImage.get(Runeimage);			
			model.addAttribute("keysRuneImage", keysRuneImage.get(Runeimage));
			
			Long perkImage = Long.valueOf(primaryStylePerks234.get(0));				
			keysRuneImage.get(perkImage);			
			model.addAttribute("keyPerk", keysRuneImage.get(perkImage));
			
			Long SubRuneimage = Long.valueOf(subStyle);	
			keysRuneImage.get(SubRuneimage);
			model.addAttribute("keysSubRuneImage", keysRuneImage.get(SubRuneimage));
			
			
									   System.out.println("2빠 메인룬 : " + primaryStyleFirstPerk.get(1).getMainRune());			
			String mainStyle2 = StatisticChampion.mainStyle(filteredData, primaryStyleFirstPerk.get(1).getMainRune());
			String subStyle2 = StatisticChampion.subStyle(filteredData, primaryStyleFirstPerk.get(1).getMainRune());
			
			System.out.println(mainStyle2);
			
			
			Long Runeimage2 = Long.valueOf(mainStyle2);				
			keysRuneImage.get(Runeimage2);			
			model.addAttribute("keysRuneImage2", keysRuneImage.get(Runeimage2));
			
			Long perkImage2 = Long.valueOf(primaryStyleFirstPerk.get(1).getMainRune());				
			keysRuneImage.get(perkImage2);			
			model.addAttribute("keyPerk2", keysRuneImage.get(perkImage2));
			
			Long SubRuneimage2 = Long.valueOf(subStyle);	
			keysRuneImage.get(SubRuneimage2);
			model.addAttribute("keysSubRuneImage2", keysRuneImage.get(SubRuneimage2));

			
			List<StatPerks> statperks = StatisticChampion.caculateStatPerks(filteredData);
			
			model.addAttribute("statperks1",statperks.get(0).getDefense());		
			model.addAttribute("statperks2",statperks.get(0).getFlex());		
			model.addAttribute("statperks3",statperks.get(0).getOffense());	
			System.out.println("이거뭐임" + statperks.get(0).getDefense());
			
			
			
			//String rawData = Files.readString(Paths.get(filePath));
			//List<DataEntry> data = StatisticChampion.parseJson(rawData);
			
			
			String link = "";
			link = "/firstItem";
			String StatisticfilePath = defaultFilePath + tier + link + "Statistics.json"; // 파일 경로 수정
			//System.out.println("경로" + StatisticfilePath);
			
			String rawStatistic = Files.readString(Paths.get(StatisticfilePath));		
			List<ItemStatistics> parsedData =  StatisticItemSkill.parseJson(rawStatistic);
			List<ItemStatistics> firstItemData = StatisticItemSkill.filterStatistic(parsedData, tier, position, championid);
			


			
			//2개만 반환함
			List<ItemStatistics> firstItem =StatisticItemSkill.caculateFirstItems(firstItemData);

			model.addAttribute("firstItem", firstItem.get(0).getItem());
			model.addAttribute("firstItemPickCount", firstItem.get(0).getPickCount());
			model.addAttribute("firstItemPickRate", 
					((double)Math.round((double)firstItem.get(0).getPickCount()/(double)firstItem.get(0).getTotalCount()*10000)/100));		
			model.addAttribute("firstItemWinRate", ((double)Math.round(firstItem.get(0).getWinRate()*10000)/100));
			
			model.addAttribute("secondItem", firstItem.get(1).getItem());
			model.addAttribute("secondItemPickCount", firstItem.get(1).getPickCount());
			model.addAttribute("secondItemPickRate", 
					((double)Math.round((double)firstItem.get(1).getPickCount()/(double)firstItem.get(1).getTotalCount()*10000)/100));
			model.addAttribute("secondItemWinRate",  ((double)Math.round(firstItem.get(1).getWinRate()*10000)/100));
			
			
			link = "/item";
			String StatisticfilePath2 = defaultFilePath + tier + link + "Statistics.json"; // 파일 경로 수정
			String rawStatistic2 = Files.readString(Paths.get(StatisticfilePath2));		
			List<ItemListStatistics> parsedDatas =  StatisticItemSkill.parseJson3(rawStatistic2);
			List<ItemListStatistics> ItemStatistic = StatisticItemSkill.filterStatistic3(parsedDatas, tier, position, championid);
			List<ItemListStatistics> itemlist = StatisticItemSkill.caculateItems(ItemStatistic);
			

			model.addAttribute("ItemStatistic", itemlist);

			model.addAttribute("listWin1", ((double)Math.round(itemlist.get(0).getWinRate()*10000)/100));
			model.addAttribute("listpick1", itemlist.get(0).getPickCount());			
			model.addAttribute("listPickRate1", 
					(double)Math.round((double)itemlist.get(0).getPickCount()/(double)itemlist.get(0).getTotalCount()*10000)/100);
			
			model.addAttribute("listWin2", ((double)Math.round(itemlist.get(1).getWinRate()*10000)/100));
			model.addAttribute("listpick2", itemlist.get(1).getPickCount());			
			model.addAttribute("listPickRate2", 
					(double)Math.round((double)itemlist.get(1).getPickCount()/(double)itemlist.get(1).getTotalCount()*10000)/100);
			
			model.addAttribute("listWin3", ((double)Math.round(itemlist.get(2).getWinRate()*10000)/100));
			model.addAttribute("listpick3", itemlist.get(2).getPickCount());			
			model.addAttribute("listPickRate3", 
					(double)Math.round((double)itemlist.get(2).getPickCount()/(double)itemlist.get(2).getTotalCount()*10000)/100);
			
			model.addAttribute("listWin4", ((double)Math.round(itemlist.get(3).getWinRate()*10000)/100));
			model.addAttribute("listpick4", itemlist.get(3).getPickCount());			
			model.addAttribute("listPickRate4", 
					(double)Math.round((double)itemlist.get(3).getPickCount()/(double)itemlist.get(3).getTotalCount()*10000)/100);
			
			model.addAttribute("listWin5", ((double)Math.round(itemlist.get(4).getWinRate()*10000)/100));
			model.addAttribute("listpick5", itemlist.get(4).getPickCount());			
			model.addAttribute("listPickRate5", 
					(double)Math.round((double)itemlist.get(4).getPickCount()/(double)itemlist.get(4).getTotalCount()*10000)/100);
			
			
			link = "/firstSkill";
			String StatisticfilePath3 = defaultFilePath + tier + link + "Statistics.json"; // 파일 경로 수정
			String rawStatistic3 = Files.readString(Paths.get(StatisticfilePath3));		
			List<SkillStatistics> parsedFirstSkill =  StatisticItemSkill.parseJson2(rawStatistic3);
			List<SkillStatistics> firstSkillStatistic = StatisticItemSkill.filterStatistic2(parsedFirstSkill, tier, position, championid);
			List<SkillStatistics> firstSkill = StatisticItemSkill.caculateFirstSkills(firstSkillStatistic);
			
			model.addAttribute("firstSkill", firstSkill);
			System.out.println(" 안아줘요 !!! : " + firstSkill.get(0).getSkillList()[0]);
			
			model.addAttribute("skillWinRate", ((double)Math.round(firstSkill.get(0).getWinRate()*10000)/100));		
			model.addAttribute("skillPickCount", firstSkill.get(0).getPickCount());			
			model.addAttribute("skillPickRate", 
					(double)Math.round((double)firstSkill.get(0).getPickCount()/(double)firstSkill.get(0).getTotalCount()*10000)/100);
			
			
			
			
			
			link = "/skill";
			String StatisticfilePath4 = defaultFilePath + tier + link + "Statistics.json"; // 파일 경로 수정
			String rawStatistic4 = Files.readString(Paths.get(StatisticfilePath4));		
			List<SkillStatistics> parsedSkill =  StatisticItemSkill.parseJson2(rawStatistic4);
			List<SkillStatistics> skillStatistic = StatisticItemSkill.filterStatistic2(parsedSkill, tier, position, championid);
			List<SkillStatistics> skill = StatisticItemSkill.caculateFirstSkills(skillStatistic);
			
			model.addAttribute("skill", skill);
			
			model.addAttribute("skillListWinRate", ((double)Math.round(skill.get(0).getWinRate()*10000)/100));		
			model.addAttribute("skillListPickCount", skill.get(0).getPickCount());			
			model.addAttribute("skillListPickRate", 
					(double)Math.round((double)skill.get(0).getPickCount()/(double)skill.get(0).getTotalCount()*10000)/100);
			
			
				
			List<ItemWinRate> items = StatisticChampion.calculateItemPreference(filteredData);
			String item1 =  String.valueOf(items.get(0).getItemId());
			String itemCount1 =  String.valueOf(items.get(0).getItemCount());
			String itemPickRate1 =  String.valueOf(items.get(0).getpickRate());
			String itemWinRate1 =  String.valueOf(items.get(0).getWinRate());
			
			String item2 =  String.valueOf(items.get(1).getItemId());
			String item3 =  String.valueOf(items.get(2).getItemId());
			
			
			
			Item item = ItemData.item(item1);
			model.addAttribute("item1", item);
			model.addAttribute("itemCount1", itemCount1);			
			model.addAttribute("itemPickRate1", ((double)Math.round(Double.parseDouble(itemPickRate1)*10000)/100));
			model.addAttribute("itemWinRate1", ((double)Math.round(Double.parseDouble(itemWinRate1)*10000)/100));
			
			
			item = ItemData.item(item2);
			model.addAttribute("item2", item);
			item = ItemData.item(item3);
			model.addAttribute("item3", item);
			
			
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
            System.out.println("no DATA");
		}
		
		
		
		Map<String, String> summonerkey = SummonerData.keysSumSpell();
		model.addAttribute("summonerkey", summonerkey);
    	new Connect("total","yalolza.gg", "champions","detail");
    	
		return "champ_detail";
	}
	
//	@GetMapping("/{champion}/counter/{position}")
//	public String getCounterData(@PathVariable String position, @PathVariable("champion") String champion,
//			@RequestParam(name = "champion", required = false) String additionalChampion, Model model) {
//		try {
//			Map<String, Object> modelData = counterDataService.getCounterData(position, champion, additionalChampion);
//			model.addAllAttributes(modelData);
//			return "counter_detail";
//		} catch (IOException e) {
//			model.addAttribute("error", "error: " + e.getMessage());
//			return "error";
//		}
//	}
 
	@GetMapping("/{champion}/counter/{position}")	
	public String getCounterData(
			Model model,
			@RequestParam(name = "champion", required = false) String additionalChampion,
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
			Map<String, Object> modelData = counterDataService.getCounterData(position, championid, additionalChampion);
			model.addAllAttributes(modelData);
			
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("no DATA");
		}

		Map<String, String> summonerkey = SummonerData.keysSumSpell();
		model.addAttribute("summonerkey", summonerkey);
		
		new Connect("total","yalolza.gg", "champions","detail");
		return "counter_detail";
	}
	@GetMapping("/{champion}/rune")  //http://localhost:9998/yalolza.gg/champions/Aatrox/build?tier=EMERALD&position=TOP
	public String ChampionsRune(
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
			
//			Integer mainStyle = Integer.parseInt(StatisticChampion.mainStyle(filteredData));
//			System.out.println(mainStyle);
//			Runes runes = RuneData.runes(mainStyle);
//			model.addAttribute("mainRune", runes);
//			String primaryStyleFirstPerk = StatisticChampion.calculatePrimaryStyleFirstPerk1(filteredData);
//			List<String> primaryStylePerks234 = StatisticChampion.calculatePrimaryStylePerks234(filteredData);
//			model.addAttribute("primaryPerk1", primaryStyleFirstPerk);
//			model.addAttribute("primaryPerk234", primaryStylePerks234);
//			runes = RuneData.runes(8400);
//			List<String> subStylePerks12 = StatisticChampion.calculateSubStylePerks12(filteredData);
//			model.addAttribute("secondaryPerk12", subStylePerks12);
//			model.addAttribute("subRune", runes);
//			List<Perk> perklist = RuneData.perklist();
//			double runeWinRate = StatisticChampion.calculateRuneWinRate(filteredData,primaryStyleFirstPerk);
//			List<SummonerSpellSetWinRate> summonerSpellSet12 = StatisticChampion.calculateSummonerSpellSet(filteredData);
//			List<Integer> Spelllist1 = new ArrayList<Integer>(summonerSpellSet12.get(0).getSpellSet());
//			List<Integer> Spelllist2 = new ArrayList<Integer>(summonerSpellSet12.get(1).getSpellSet());
//			출처: https://hianna.tistory.com/555 [어제 오늘 내일:티스토리]
//				model.addAttribute("perklist", perklist);
//			Spell spell = SummonerData.findspell(Spelllist1.get(0).toString());
//			model.addAttribute("summoner1", spell);
//			spell = SummonerData.findspell(Spelllist1.get(1).toString());
//			model.addAttribute("summoner2", spell);
//			model.addAttribute("summonerSpellSet1Win", ((double)Math.round(summonerSpellSet12.get(0).getWinRate()*10000)/100));
//			spell = SummonerData.findspell(Spelllist2.get(0).toString());
//			model.addAttribute("summoner3", spell);
//			spell = SummonerData.findspell(Spelllist2.get(1).toString());
//			model.addAttribute("summoner4", spell);
//			model.addAttribute("summonerSpellSet2Win", ((double)Math.round(summonerSpellSet12.get(1).getWinRate()*10000)/100));
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
		return "champ_rune";
	}
	@GetMapping("/{champion}/item")  //http://localhost:9998/yalolza.gg/champions/Aatrox/build?tier=EMERALD&position=TOP
	public String ChampionsItem(
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
			
//			Integer mainStyle = Integer.parseInt(StatisticChampion.mainStyle(filteredData));
//			System.out.println(mainStyle);
//			Runes runes = RuneData.runes(mainStyle);
//			model.addAttribute("mainRune", runes);
//			String primaryStyleFirstPerk = StatisticChampion.calculatePrimaryStyleFirstPerk1(filteredData);
//			List<String> primaryStylePerks234 = StatisticChampion.calculatePrimaryStylePerks234(filteredData);
//			model.addAttribute("primaryPerk1", primaryStyleFirstPerk);
//			model.addAttribute("primaryPerk234", primaryStylePerks234);
//			runes = RuneData.runes(8400);
//			List<String> subStylePerks12 = StatisticChampion.calculateSubStylePerks12(filteredData);
//			model.addAttribute("secondaryPerk12", subStylePerks12);
//			model.addAttribute("subRune", runes);
//			List<Perk> perklist = RuneData.perklist();
//			double runeWinRate = StatisticChampion.calculateRuneWinRate(filteredData,primaryStyleFirstPerk);
//			List<SummonerSpellSetWinRate> summonerSpellSet12 = StatisticChampion.calculateSummonerSpellSet(filteredData);
//			List<Integer> Spelllist1 = new ArrayList<Integer>(summonerSpellSet12.get(0).getSpellSet());
//			List<Integer> Spelllist2 = new ArrayList<Integer>(summonerSpellSet12.get(1).getSpellSet());
//			출처: https://hianna.tistory.com/555 [어제 오늘 내일:티스토리]
//				model.addAttribute("perklist", perklist);
//			Spell spell = SummonerData.findspell(Spelllist1.get(0).toString());
//			model.addAttribute("summoner1", spell);
//			spell = SummonerData.findspell(Spelllist1.get(1).toString());
//			model.addAttribute("summoner2", spell);
//			model.addAttribute("summonerSpellSet1Win", ((double)Math.round(summonerSpellSet12.get(0).getWinRate()*10000)/100));
//			spell = SummonerData.findspell(Spelllist2.get(0).toString());
//			model.addAttribute("summoner3", spell);
//			spell = SummonerData.findspell(Spelllist2.get(1).toString());
//			model.addAttribute("summoner4", spell);
//			model.addAttribute("summonerSpellSet2Win", ((double)Math.round(summonerSpellSet12.get(1).getWinRate()*10000)/100));
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
		return "champ_item";
	}
	@GetMapping("/{champion}/skill")  //http://localhost:9998/yalolza.gg/champions/Aatrox/build?tier=EMERALD&position=TOP
	public String ChampionsSkill(
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
			
//			Integer mainStyle = Integer.parseInt(StatisticChampion.mainStyle(filteredData));
//			System.out.println(mainStyle);
//			Runes runes = RuneData.runes(mainStyle);
//			model.addAttribute("mainRune", runes);
//			String primaryStyleFirstPerk = StatisticChampion.calculatePrimaryStyleFirstPerk1(filteredData);
//			List<String> primaryStylePerks234 = StatisticChampion.calculatePrimaryStylePerks234(filteredData);
//			model.addAttribute("primaryPerk1", primaryStyleFirstPerk);
//			model.addAttribute("primaryPerk234", primaryStylePerks234);
//			runes = RuneData.runes(8400);
//			List<String> subStylePerks12 = StatisticChampion.calculateSubStylePerks12(filteredData);
//			model.addAttribute("secondaryPerk12", subStylePerks12);
//			model.addAttribute("subRune", runes);
//			List<Perk> perklist = RuneData.perklist();
//			double runeWinRate = StatisticChampion.calculateRuneWinRate(filteredData,primaryStyleFirstPerk);
//			List<SummonerSpellSetWinRate> summonerSpellSet12 = StatisticChampion.calculateSummonerSpellSet(filteredData);
//			List<Integer> Spelllist1 = new ArrayList<Integer>(summonerSpellSet12.get(0).getSpellSet());
//			List<Integer> Spelllist2 = new ArrayList<Integer>(summonerSpellSet12.get(1).getSpellSet());
//			출처: https://hianna.tistory.com/555 [어제 오늘 내일:티스토리]
//				model.addAttribute("perklist", perklist);
//			Spell spell = SummonerData.findspell(Spelllist1.get(0).toString());
//			model.addAttribute("summoner1", spell);
//			spell = SummonerData.findspell(Spelllist1.get(1).toString());
//			model.addAttribute("summoner2", spell);
//			model.addAttribute("summonerSpellSet1Win", ((double)Math.round(summonerSpellSet12.get(0).getWinRate()*10000)/100));
//			spell = SummonerData.findspell(Spelllist2.get(0).toString());
//			model.addAttribute("summoner3", spell);
//			spell = SummonerData.findspell(Spelllist2.get(1).toString());
//			model.addAttribute("summoner4", spell);
//			model.addAttribute("summonerSpellSet2Win", ((double)Math.round(summonerSpellSet12.get(1).getWinRate()*10000)/100));
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
		return "champ_skill";
	}
	@GetMapping("/{champion}/tip")  //http://localhost:9998/yalolza.gg/champions/Aatrox/build?tier=EMERALD&position=TOP
	public String ChampionsTip(
			Model model,
			@RequestParam String query,
			@RequestParam(name="tier",required = false, defaultValue = "EMERALD") String tier,
			@RequestParam(name="position",required = false, defaultValue = "TOP") String position,
			@PathVariable("champion") String championid
			) {
		Champion champion = ChampionData.championinfo(championid);
		model.addAttribute("champion",champion);
        model.addAttribute("youtubeVideos", youtubeService.youtubeGenerator(query));

		String filePath = defaultFilePath + tier + "/data.json"; // 파일 경로 수정
		try {
			String rawData = Files.readString(Paths.get(filePath));
			List<DataEntry> data = StatisticChampion.parseJson(rawData);
			List<DataEntry> filteredData = StatisticChampion.filterData(data, tier, position, championid);
			
//			Integer mainStyle = Integer.parseInt(StatisticChampion.mainStyle(filteredData));
//			System.out.println(mainStyle);
//			Runes runes = RuneData.runes(mainStyle);
//			model.addAttribute("mainRune", runes);
//			String primaryStyleFirstPerk = StatisticChampion.calculatePrimaryStyleFirstPerk1(filteredData);
//			List<String> primaryStylePerks234 = StatisticChampion.calculatePrimaryStylePerks234(filteredData);
//			model.addAttribute("primaryPerk1", primaryStyleFirstPerk);
//			model.addAttribute("primaryPerk234", primaryStylePerks234);
//			runes = RuneData.runes(8400);
//			List<String> subStylePerks12 = StatisticChampion.calculateSubStylePerks12(filteredData);
//			model.addAttribute("secondaryPerk12", subStylePerks12);
//			model.addAttribute("subRune", runes);
//			List<Perk> perklist = RuneData.perklist();
//			double runeWinRate = StatisticChampion.calculateRuneWinRate(filteredData,primaryStyleFirstPerk);
//			List<SummonerSpellSetWinRate> summonerSpellSet12 = StatisticChampion.calculateSummonerSpellSet(filteredData);
//			List<Integer> Spelllist1 = new ArrayList<Integer>(summonerSpellSet12.get(0).getSpellSet());
//			List<Integer> Spelllist2 = new ArrayList<Integer>(summonerSpellSet12.get(1).getSpellSet());
//			출처: https://hianna.tistory.com/555 [어제 오늘 내일:티스토리]
//				model.addAttribute("perklist", perklist);
//			Spell spell = SummonerData.findspell(Spelllist1.get(0).toString());
//			model.addAttribute("summoner1", spell);
//			spell = SummonerData.findspell(Spelllist1.get(1).toString());
//			model.addAttribute("summoner2", spell);
//			model.addAttribute("summonerSpellSet1Win", ((double)Math.round(summonerSpellSet12.get(0).getWinRate()*10000)/100));
//			spell = SummonerData.findspell(Spelllist2.get(0).toString());
//			model.addAttribute("summoner3", spell);
//			spell = SummonerData.findspell(Spelllist2.get(1).toString());
//			model.addAttribute("summoner4", spell);
//			model.addAttribute("summonerSpellSet2Win", ((double)Math.round(summonerSpellSet12.get(1).getWinRate()*10000)/100));
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
		return "champ_tip";
	}
	
	 

}
