package com.web.project.system;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.project.dto.runeSpell.DataEntry;
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
	
    //받아온 데이터 티어, 포지션, 챔 이른으로 거름
	public static List<DataEntry> filterData(List<DataEntry> data, String tier, String position, String championName) {
        return data.stream()
                .filter(entry -> entry.getIndividualPosition().equals(position)
                        && entry.getChampionName().equals(championName))
                .collect(Collectors.toList());
    }
   
    
    //챔피언, 포지션 한정해서 가장 많이 등장한 메인룬 1,2 출력   
	public static List<String> calculatePrimaryStyleFirstPerk1(List<DataEntry> filteredData) {
	    List<String> primaryPerk = Collections.emptyList();
	    if (!filteredData.isEmpty()) {
	        primaryPerk = filteredData.stream()
	                .flatMap(entry -> entry.getPerks().getStyles().stream())
	                .filter(style -> "primaryStyle".equals(style.getDescription()))
	                .map(style -> style.getSelections().stream().findFirst().map(selection -> String.valueOf(selection.getPerk())).orElse(null)) 	                .limit(2) // 최대 두 개의 perk만 선택
	                .limit(2) // 최대 두 개의 perk만 선택
	                .filter(perk -> perk != null) // null이 아닌 perk만 필터링
	                .collect(Collectors.toList()); // 리스트로 변환
	    }
	    return primaryPerk;
	}

 
    //calculatePrimaryStyleFirstPerk1 에서 검색된 특성 종류, 메인룬, 해당 티어, 포지션, 챔피언id로 검색
  
	//정밀, 마법, 영감 등등... 키 가져와요
	//primaryPerk에는 calculatePrimaryStyleFirstPerk1의 가장 많이 등장한 룬, 두번째로 등장한 룬 넣을 예정
	public static String mainStyle(List<DataEntry> filteredData, String primaryPerk) {
		String mainStyle ="";
		if (!filteredData.isEmpty()) {
			//<perk 번호 : 등장 횟수>
            Map<Integer, Long> counter = filteredData.stream()
                    .flatMap(entry -> entry.getPerks().getStyles().stream())//"styles":열어
                    .filter(style -> "primaryStyle".equals(style.getDescription()))
                    .flatMap(selection -> selection.getSelections().stream())// "selections":열어
                    .filter(perk -> perk.getPerk() == Integer.parseInt(primaryPerk))//firstperk 가져와
                    //숫자 : 등장횟수 모아줘요
                    .collect(Collectors.groupingBy(Selections::getPerk, Collectors.counting()));

            //
            if (!counter.isEmpty()) {
                Long maxCount = Collections.max(counter.values());
                mainStyle = counter.entrySet().stream()
                        .filter(entry -> entry.getValue() == maxCount)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .map(String::valueOf)
                        .orElse("");      
            }
		}
		
		return mainStyle;
	}


	//메인 특성의 하위룬
	public static List<String> calculatePrimaryStylePerks234(List<DataEntry> filteredData) {
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

	//서브룬
	public static List<String> calculateSubStylePerks12(List<DataEntry> filteredData) {
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
