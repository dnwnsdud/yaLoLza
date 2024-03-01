package com.web.project.api.controller.runeSpell;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.project.dto.runeSpell.DataEntry;
import com.web.project.dto.runeSpell.Selections;
import com.web.project.dto.runeSpell.SummonerSpellSetWinRate;


@Controller
@RequestMapping("/yalolza.gg/statistics")
public class ChampionDataApiController {

	// 참고로 perk = rune
	private String defaultFilePath;

	public ChampionDataApiController() {
		System.out.println("CALL datafile");
	    this.defaultFilePath = "src/main/resources/static/datas/RANKED_SOLO_5x5/";	    
	    System.out.println(defaultFilePath);
	}
	
    @GetMapping("/rune")
    public String getRuneData(Model model,
                              @RequestParam("tier") String tier,
                              @RequestParam("position") String position,
                              @RequestParam("championName") String championName
                              ) {
    	System.out.println("start");
    	try {
            // 요청된 티어에 따라 파일 경로를 업데이트합니다.
    		System.out.println("file update");
            String filePath = defaultFilePath + tier + "/data.json"; // 파일 경로 수정

            System.out.println(defaultFilePath + tier + "/data.json");
                        
            String rawData = Files.readString(Paths.get(filePath));
            //System.out.println(rawData);됨
            
            List<DataEntry> data = parseJson(rawData);   
            //System.out.println(data.get(0).getChampionName());됨
            
            List<DataEntry> filteredData = filterData(data, tier, position, championName);
            
            System.out.println(filteredData);
            
            String primaryStyleFirstPerk = calculatePrimaryStyleFirstPerk1(filteredData);
            List<String> primaryStylePerks234 = calculatePrimaryStylePerks234(filteredData);
            
            System.out.println("main rune : " + primaryStyleFirstPerk);
            List<String> subStylePerks12 = calculateSubStylePerks12(filteredData);

            double winrate = calculateWinrate(filteredData);
            
            double runeWinRate = calculateRuneWinRate(filteredData,primaryStyleFirstPerk);
            List<SummonerSpellSetWinRate> summonerSpellSet12 = calculateSummonerSpellSet(filteredData);
            List<Integer> ItemPreference = calculateItemPreference(filteredData);

            
            model.addAttribute("primaryPerk1", primaryStyleFirstPerk);
            model.addAttribute("primaryPerk2", primaryStylePerks234.get(1));
            model.addAttribute("primaryPerk3", primaryStylePerks234.get(2));
            model.addAttribute("primaryPerk4", primaryStylePerks234.get(3));
            model.addAttribute("secondaryPerk1", subStylePerks12.get(0));
            model.addAttribute("secondaryPerk2", subStylePerks12.get(1));
            model.addAttribute("runeWinRate", runeWinRate);
            
            model.addAttribute("summonerSpellSet1", summonerSpellSet12.get(0).getSpellSet());
            model.addAttribute("summonerSpellSet1Win", summonerSpellSet12.get(0).getWinRate());
            
            model.addAttribute("summonerSpellSet2", summonerSpellSet12.get(1).getSpellSet());
            model.addAttribute("summonerSpellSet2Win", summonerSpellSet12.get(1).getWinRate());
            model.addAttribute("item1", ItemPreference.get(0));
            
            System.out.println("like1 :"  +ItemPreference.get(0));
            System.out.println("like2 :"  +ItemPreference.get(1));
            
            model.addAttribute("item2", ItemPreference.get(1));
            model.addAttribute("item3", ItemPreference.get(2));
            model.addAttribute("WinRate", winrate);

            //모델에 rune.html에서 요구하는 데이터들 더 추가
           
           
            
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("no DATA");
        }

        return "rune";
    }


    
    private List<DataEntry> parseJson(String rawData) {
        ObjectMapper objectMapper = new ObjectMapper();//json parse하는 함수래요
        try {
            return objectMapper.readValue(rawData, new TypeReference<List<DataEntry>>() {});
 
        } catch (Exception e) {
            e.printStackTrace();   
            return Collections.emptyList(); // 빈 리스트 반환
        }
    }
    
    private List<DataEntry> filterData(List<DataEntry> data, String tier, String position, String championName) {
        return data.stream()
                .filter(entry -> entry.getIndividualPosition().equals(position)
                        && entry.getChampionName().equals(championName))
                .collect(Collectors.toList());
    }
   
    
    //메인룬 출력   
    private String calculatePrimaryStyleFirstPerk1(List<DataEntry> filteredData) {
        String primaryPerk = "";
        if (!filteredData.isEmpty()) {
            Map<Integer, Long> counter = filteredData.stream()
                    .flatMap(entry -> entry.getPerks().getStyles().stream())
                    .filter(style -> "primaryStyle".equals(style.getDescription()))
                    .flatMap(style -> style.getSelections().stream())
                    .collect(Collectors.groupingBy(Selections::getPerk, Collectors.counting()));

            if (!counter.isEmpty()) {
                Long maxCount = Collections.max(counter.values());
                primaryPerk = counter.entrySet().stream()
                        .filter(entry -> entry.getValue() == maxCount)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .map(String::valueOf)
                        .orElse("");
            }
        }
        return primaryPerk;
    }
 
    //calculatePrimaryStyleFirstPerk1 에서 검색된 메인룬, 해당 티어, 포지션, 챔피언id로 검색
    // 
    
    //calculatePrimaryStyleFirstPerk1 에서 검색된 메인룬 사용
    //해당 티어, 포지션, 챔피언id로 검색
    private List<String> calculatePrimaryStylePerks234(List<DataEntry> filteredData) {
        List<String> primaryStylePerks234 = new ArrayList<>();
        if (!filteredData.isEmpty()) {
            Map<Integer, Long> counter = filteredData.stream()
                    .flatMap(entry -> entry.getPerks().getStyles().stream())
                    .filter(style -> "primaryStyle".equals(style.getDescription()))
                    .flatMap(style -> style.getSelections().stream())
                    .collect(Collectors.groupingBy(Selections::getPerk, Collectors.counting()));

            for (int i = 0; i < 4; i++) {
                Long maxCount = Collections.max(counter.values());
                String perk = counter.entrySet().stream()
                        .filter(entry -> entry.getValue() == maxCount)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .map(String::valueOf)
                        .orElse("");
                primaryStylePerks234.add(perk);
                counter.remove(Integer.parseInt(perk)); // 최대 등장 횟수의 perk를 리스트에 추가한 후 제거합니다.
            }
        }
        return primaryStylePerks234;
    }

    private List<String> calculateSubStylePerks12(List<DataEntry> filteredData) {
        List<String> subStylePerks12 = new ArrayList<>();
        if (!filteredData.isEmpty()) {
            Map<Integer, Long> counter = filteredData.stream()
                    .flatMap(entry -> entry.getPerks().getStyles().stream())
                    .filter(style -> "subStyle".equals(style.getDescription()))
                    .flatMap(style -> style.getSelections().stream())
                    .collect(Collectors.groupingBy(Selections::getPerk, Collectors.counting()));

            for (int i = 0; i < 2; i++) {
                Long maxCount = Collections.max(counter.values());
                String perk = counter.entrySet().stream()
                        .filter(entry -> entry.getValue() == maxCount)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .map(String::valueOf)
                        .orElse("");
                subStylePerks12.add(perk);
                counter.remove(Integer.parseInt(perk)); // 최대 등장 횟수의 perk를 리스트에 추가한 후 제거합니다.
            }
        }
        return subStylePerks12;
    }
    
    
     
    private List<Integer> calculateItemPreference(List<DataEntry> filteredData) {
        List<Integer> itemPreference = new ArrayList<>();
        
        if (!filteredData.isEmpty()) {
            Map<Integer, Long> itemCounts = filteredData.stream()
                    .flatMap(entry -> Stream.of(entry.getItem0(), entry.getItem1(), entry.getItem2(),
                                                entry.getItem3(), entry.getItem4(), entry.getItem5(),
                                                entry.getItem6()))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            // 가장 많이 등장한 아이템 3개
            itemCounts.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed()) // 내림차순
                    .limit(3) // 상위 3개 아이템
                    .map(Map.Entry::getKey)
                    .forEach(itemPreference::add);
        }
        return itemPreference;
        //아이템선호? 키는 뭐냐?
    }
    
    private double calculateWinrate(List<DataEntry> filteredData) {
        double winCount = 0;
        double totalCount = filteredData.size();
        if (totalCount > 0) {
            winCount = filteredData.stream()
                    .filter(DataEntry::isWin)
                    .count();
        }
        return winCount / totalCount;
    }
    
    
    private double calculateRuneWinRate(List<DataEntry> filteredData, String primaryStyleFirstPerk1) {
        double winCount = filteredData.stream()
                .filter(entry -> entry.getPerks().getStyles().stream()
                        .anyMatch(style -> style.getDescription().equals("primaryStyle") &&
                                style.getSelections().stream()
                                        .anyMatch(selection -> selection.getPerk() == Integer.parseInt(primaryStyleFirstPerk1))))
                .filter(DataEntry::isWin)
                .count();
        double totalCount = filteredData.stream()
                .filter(entry -> entry.getPerks().getStyles().stream()
                        .anyMatch(style -> style.getDescription().equals("primaryStyle") &&
                                style.getSelections().stream()
                                        .anyMatch(selection -> selection.getPerk() == Integer.parseInt(primaryStyleFirstPerk1))))
                .count();

        if (totalCount == 0) {
            return 0; // 분모가 0이면 안돼
        }

        return winCount / totalCount;
    }

    
    private List<SummonerSpellSetWinRate> calculateSummonerSpellSet(List<DataEntry> filteredData) {
        Map<Set<Integer>, Integer> spellSetCounts = new HashMap<>();//'등장' 횟수
        Map<Set<Integer>, Integer> spellSetWins = new HashMap<>();	//'승리' 횟수

        // 각 소환사 주문 세트의 등장 횟수와 승리 횟수를 계산
        for (DataEntry entry : filteredData) {
            Set<Integer> spellSet = new HashSet<>();
            spellSet.add(entry.getSummoner1Id());
            spellSet.add(entry.getSummoner2Id());         
            spellSetCounts.put(spellSet, spellSetCounts.getOrDefault(spellSet, 0) + 1);     
            if (entry.isWin()) {
                spellSetWins.put(spellSet, spellSetWins.getOrDefault(spellSet, 0) + 1);
            }
        }//승률구함

        List<Map.Entry<Set<Integer>, Integer>> sortedSpellSets = new ArrayList<>(spellSetCounts.entrySet());
        sortedSpellSets.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));//비교해요

        List<SummonerSpellSetWinRate> result = new ArrayList<>();
        for (int i = 0; i < Math.min(sortedSpellSets.size(), 2); i++) {
            Map.Entry<Set<Integer>, Integer> entry = sortedSpellSets.get(i);
            Set<Integer> spellSet = entry.getKey();
            double winRate = (double) spellSetWins.getOrDefault(spellSet, 0) / entry.getValue();
            result.add(new SummonerSpellSetWinRate(spellSet, winRate));
        }

        return result;
    }

    
    
    
    
}

	

	
	

