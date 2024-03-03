package com.web.project.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.project.dto.runeSpell.DataEntry;
import com.web.project.dto.runeSpell.RuneWinRate;
import com.web.project.dto.runeSpell.Selections;
import com.web.project.dto.runeSpell.Styles;
import com.web.project.dto.runeSpell.SummonerSpellSetWinRate;

public class StatisticChampion {
	
	public static List<DataEntry> parseJson(String rawData) {//json parse하는 함수
        ObjectMapper objectMapper = new ObjectMapper();
        try {											//selections같은 타입 유지
            return objectMapper.readValue(rawData, new TypeReference<List<DataEntry>>() {});
 
        } catch (Exception e) {
            e.printStackTrace();   
            return Collections.emptyList(); // 빈 리스트 반환
        }
    }
	
    //받아온 데이터 티어, 포지션, 챔 이름으로 거름
	public static List<DataEntry> filterData(List<DataEntry> data, String tier, String position, String championName) {
        return data.stream()
                .filter(entry -> entry.getIndividualPosition().equals(position)
                        && entry.getChampionName().equals(championName))
                .collect(Collectors.toList());
    }
   
    
	public static List<RuneWinRate> calculatePrimaryStyleFirstPerk1(List<DataEntry> filteredData) {
	    Map<Integer, Integer> mainRuneCounts = new HashMap<>();
	    Map<Integer, Integer> mainRuneWins = new HashMap<>();
	    int totalChampionCount = filteredData.size();

	    // 각 챔피언의 메인 룬 등장 횟수와 승리 횟수를 계산
	    for (DataEntry entry : filteredData) {
	        Optional<Integer> mainRune = entry.getPerks().getStyles().stream()
	                .filter(style -> "primaryStyle".equals(style.getDescription()))
	                .flatMap(style -> style.getSelections().stream().map(Selections::getPerk))
	                .findFirst();

	        if (mainRune.isPresent()) {
	            int runeId = mainRune.get();
	            mainRuneCounts.put(runeId, mainRuneCounts.getOrDefault(runeId, 0) + 1);
	            if (entry.isWin()) {
	                mainRuneWins.put(runeId, mainRuneWins.getOrDefault(runeId, 0) + 1);
	            }
	        }
	    }

	    List<RuneWinRate> result = mainRuneCounts.entrySet().stream()
	            .map(entry -> {
	                int runeId = entry.getKey();
	                int runeCount = entry.getValue();
	                double appearanceRate = (double) runeCount / totalChampionCount;
	                int wins = mainRuneWins.getOrDefault(runeId, 0);
	                double winRate = (double) wins / runeCount;

	                return new RuneWinRate(String.valueOf(runeId), winRate, runeCount, appearanceRate);
	            })
	            .collect(Collectors.toList());

	    // 결과를 등장 횟수가 많은 순서로 정렬하고 상위 두 개의 메인 룬만 선택
	    result.sort(Comparator.comparingInt(RuneWinRate::getSetCount).reversed());
	    if (result.size() > 2) {
	        result = result.subList(0, 2);
	    }

	    return result;
	}



 
    //calculatePrimaryStyleFirstPerk1 에서 검색된 특성 종류, 메인룬, 해당 티어, 포지션, 챔피언id로 검색
  
	
	//정밀, 마법, 영감 등등... style 키 가져와요
	//primaryPerk에는 calculatePrimaryStyleFirstPerk1의 가장 많이 등장한 룬, 두번째로 등장한 룬 넣을 예정
	public static String mainStyle(List<DataEntry> filteredData, String primaryPerk) {
		String mainStyle ="";
		if (!filteredData.isEmpty()) {
			//<perk 번호 : 등장 횟수>
            Map<Integer, Long> counter = filteredData.stream()
                    .flatMap(entry -> entry.getPerks().getStyles().stream())//"styles":열어
                    .filter(style -> "primaryStyle".equals(style.getDescription()))//primary 검색
                    .map(Styles::getStyle) // 각 "styles에서 "style"만 선택해서 map에 넣어
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));//그냥 모아서 숫자만 셈

           
            if (!counter.isEmpty()) {
                Long maxCount = Collections.max(counter.values()); //값
                mainStyle = counter.entrySet().stream()
                        .filter(entry -> entry.getValue() == maxCount)//하나씩 세
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .map(String::valueOf)
                        .orElse("");      
            }else {
            	return "8000";//정밀 반환
            }
		}
		
		return mainStyle;
	}


	public static List<Integer> calculatePrimaryStylePerks234(List<DataEntry> filteredData, String primaryPerk) {
	    List<Integer> primaryStylePerks234 = new ArrayList<>();

	    if (!filteredData.isEmpty()) {
	        Map<List<Integer>, Long> counter = filteredData.stream()
	                .flatMap(entry -> entry.getPerks().getStyles().stream())
	                .filter(styles -> "primaryStyle".equals(styles.getDescription()))
	                .flatMap(styles -> styles.getSelections().stream())
	                .filter(mainRune -> primaryPerk.equals(mainRune.getPerk(0)))
	                .map(selections -> {
	                    List<Integer> perks = new ArrayList<>();
	                    for (int i = 0; i < 4; i++) {
	                        perks.add(selections.getPerk(i));
	                    }
	                    return perks;
	                })
	                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	                		
	                		
	               // 		.stream().map(Selections::getPerk).sorted().collect(Collectors.toList())
	               // .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

	        // 가장 많이 등장한 값을 찾음
	                List<Integer> maxEntry = Collections.max(counter.entrySet(), Map.Entry.comparingByValue()).getKey();

	    }
	    return maxEntry;
	}
	
		//서브 스타일
		public static String subStyle(List<DataEntry> filteredData, String primaryPerk) {
			String subStyle ="";
			if (!filteredData.isEmpty()) {
				//<perk 번호 : 등장 횟수>
	            Map<Integer, Long> counter = filteredData.stream()
	                    .flatMap(entry -> entry.getPerks().getStyles().stream())//"styles":열어
	                    .filter(style -> "subStyle".equals(style.getDescription()))//primary 검색
	                    .map(Styles::getStyle) // 각 "styles에서 "style"만 선택해서 map에 넣어
	                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

	           
	            if (!counter.isEmpty()) {
	                Long maxCount = Collections.max(counter.values()); //값
	                subStyle = counter.entrySet().stream()
	                        .filter(entry -> entry.getValue() == maxCount)//하나씩 세
	                        .map(Map.Entry::getKey)
	                        .findFirst()
	                        .map(String::valueOf)
	                        .orElse("");      
	            }
			}
			
			return subStyle;
		}
	
	
	//서브 특성의 하위룬
	public static List<String> calculateSubStylePerks12(List<DataEntry> filteredData, String primaryPerk) {
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
    
    
     
	public static List<Integer> calculateItemPreference(List<DataEntry> filteredData) {
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
    
	public static double calculateWinrate(List<DataEntry> filteredData) {
        double winCount = 0;
        double totalCount = filteredData.size();
        if (totalCount > 0) {
            winCount = filteredData.stream()
                    .filter(DataEntry::isWin)
                    .count();
        }
        return winCount / totalCount;
    }
    
	public static int calculateRuneGameCount(List<DataEntry> filteredData, String primaryStyleFirstPerk1) {
	    return (int) filteredData.stream()
	            .filter(entry -> entry.getPerks().getStyles().stream()
	                    .anyMatch(style -> style.getDescription().equals("primaryStyle") &&
	                            style.getSelections().stream()
	                                    .anyMatch(selection -> selection.getPerk() == Integer.parseInt(primaryStyleFirstPerk1))))
	            .count();
	}
	
	public static double calculateRunePickRate(List<DataEntry> filteredData, String primaryStyleFirstPerk1) {
	    double pickCount = filteredData.stream()
	            .filter(entry -> entry.getPerks().getStyles().stream()
	                    .anyMatch(style -> style.getDescription().equals("primaryStyle") &&
	                            style.getSelections().stream()
	                                    .anyMatch(selection -> selection.getPerk() == Integer.parseInt(primaryStyleFirstPerk1))))
	            .count();
	    double totalCount = filteredData.size();

	    if (totalCount == 0) {
	        return 0; // 분모가 0이면 안돼
	    }

	    return pickCount / totalCount;
	}

	
    
	public static double calculateRuneWinRate(List<DataEntry> filteredData, String primaryStyleFirstPerk1) {
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

    
	public static List<SummonerSpellSetWinRate> calculateSummonerSpellSet(List<DataEntry> filteredData) {
        Map<Set<Integer>, Integer> spellSetCounts = new HashMap<>();//스펠의 '등장' 횟수
        Map<Set<Integer>, Integer> spellSetWins = new HashMap<>();	//스펠의 '승리' 횟수
        int totalChampionCount = 0;									//해당 챔피언 '총' 등장 횟수

        // 각 소환사 주문 세트의 등장 횟수와 승리 횟수를 계산
        for (DataEntry entry : filteredData) {
            Set<Integer> spellSet = new HashSet<>();
            spellSet.add(entry.getSummoner1Id());
            spellSet.add(entry.getSummoner2Id());         
            spellSetCounts.put(spellSet, spellSetCounts.getOrDefault(spellSet, 0) + 1);     
            if (entry.isWin()) {
                spellSetWins.put(spellSet, spellSetWins.getOrDefault(spellSet, 0) + 1);
            }
            totalChampionCount++;
        }//승률, 챔피언 총 등장 횟수 구함

//        List<Map.Entry<Set<Integer>, Integer>> sortedSpellSets = new ArrayList<>(spellSetCounts.entrySet());
//        sortedSpellSets.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));//1등 비교해요

        List<SummonerSpellSetWinRate> result = new ArrayList<>();
        
        for (Map.Entry<Set<Integer> , Integer> entry : spellSetCounts.entrySet()) {
            Set<Integer> spellSet = entry.getKey();
            int setCount = entry.getValue();
            double appearanceRate = (double) setCount / totalChampionCount;
            int wins = spellSetWins.getOrDefault(spellSet, 0);
            double winRate = (double) wins / setCount;
            result.add(new SummonerSpellSetWinRate(spellSet, winRate, setCount, appearanceRate));
        }
        result.sort(Comparator.comparingInt(SummonerSpellSetWinRate::getSetCount).reversed());

        // 2개의 소환사 주문 세트만 선택
        
        if (!result.isEmpty()) {
            result = result.subList(0, Math.min(result.size(), 2));
        } else {
            // 빈 리스트면 점멸, 점멸 반환함
            Set<Integer> defaultSpellSet = new HashSet<>();
            defaultSpellSet.add(4); // 4 = 점멸
            result.add(new SummonerSpellSetWinRate(defaultSpellSet, 0.0, 0, 0.0));
        }
        return result; 
        

    }
}
