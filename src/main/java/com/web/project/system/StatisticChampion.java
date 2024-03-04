package com.web.project.system;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.web.project.dto.runeSpell.ItemWinRate;
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
	        Map<String, Long> counter = filteredData.stream()
	                .flatMap(entry -> entry.getPerks().getStyles().stream())
	                .filter(styles -> "primaryStyle".equals(styles.getDescription()))
	                .flatMap(styles -> styles.getSelections().stream()
	                        .map(selection -> String.valueOf(selection.getPerk())))
	                .collect(Collectors.groupingBy(perks -> perks, Collectors.counting()));

	        
	        Optional<Map.Entry<String, Long>> maxEntry = counter.entrySet().stream()
	        		.filter(entry -> entry.getKey().contains(primaryPerk))
	                .max(Map.Entry.comparingByValue());

	        if (maxEntry.isPresent()) {
	            String mostFrequentPerks = maxEntry.get().getKey();
	            // 가장 많이 등장한 조합을 List<Integer>로 변환하여 반환
	            primaryStylePerks234 = Arrays.stream(mostFrequentPerks.split(","))
	                    .map(Integer::parseInt)
	                    .collect(Collectors.toList());
	        }
	    }

	    return primaryStylePerks234;
	}
	
		//서브 스타일
		public static String subStyle(List<DataEntry> filteredData, String primaryPerk) {
			String subStyle ="";
			if (!filteredData.isEmpty()) {
				//<perk 번호 : 등장 횟수>
	            Map<Integer, Long> counter = filteredData.stream()
	                    .flatMap(entry -> entry.getPerks().getStyles().stream())//"styles":열어
	                    .filter(style -> style.getSelections().stream()//Selections에서 메인 룬 가진애만 찾아
	                    		.anyMatch(selection -> primaryPerk.equals(String.valueOf(selection.getPerk()))))
	                    .filter(style -> "subStyle".equals(style.getDescription()))
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
    
    
     
	public static List<ItemWinRate> calculateItemPreference(List<DataEntry> filteredData) {
		 	List<Integer> legendaryItemIds = Arrays.asList(
		 		    2065, 2502, 2504, 3001, 3002, 3003, 3004, 3011, 3026, 3033, 3036, 3039, 3046, 3050, 3053, 3065, 3068, 3071, 3072, 3073, 3074, 3075, 3078, 3083, 3084, 3085, 3087, 3091, 3094, 3095, 3100, 3102, 3107, 3109, 
		 		    3110, 3115, 3116, 3118, 3119, 3124, 3128, 3131, 3135, 3137, 3139, 3142, 3143, 3152, 3153, 3156, 3157, 3161, 3165, 3179, 3181, 3190, 3193, 3222, 3302, 3504, 3508, 3742, 3748, 3814, 4003, 4005, 4401, 4402, 
		 		    4403, 4628, 4629, 4633, 4636, 4637, 4644, 4645, 4646, 6035, 6333, 6609, 6610, 6616, 6617, 6620, 6621, 6630, 6631, 6632, 6653, 6655, 6656, 6657, 6662, 6664, 6665, 6667, 6671, 6672, 6673, 6675, 6676, 6691, 
		 		    6692, 6693, 6694, 6695, 6696, 6697, 6698, 6699, 6700, 6701, 7003, 7031, 8001, 8020
		 		   ,3042,3121,3040);//여눈 3상위
		 	
			List<ItemWinRate> result = new ArrayList<>();
		 	Map<Integer, Integer> itemCounts = new HashMap<>();	//템 '등장' 횟수
	        Map<Integer, Integer> itemWins = new HashMap<>();	//템 '승리' 횟수
	        int totalChampionCount = 0;							//해당 챔피언 '총' 등장 횟수

	        // 아이템 등장 횟수와 승리 횟수를 계산
	        for (DataEntry entry : filteredData) {
	        	List<Integer> itemList = Arrays.asList(
                        entry.getItem0(), entry.getItem1(), entry.getItem2(),
                        entry.getItem3(), entry.getItem4(), entry.getItem5(),
                        entry.getItem6());
	        	for (Integer itemId : itemList) {
                    itemCounts.put(itemId, itemCounts.getOrDefault(itemId, 0) + 1);
                    if (entry.isWin()) {
                        itemWins.put(itemId, itemWins.getOrDefault(itemId, 0) + 1);
                    }
                }
	        	totalChampionCount++;
            }
	        //승률, 챔피언 총 등장 횟수 구함


	 // 아이템 등장 비율과 승률을 계산하여 결과 리스트에 추가
    for (Map.Entry<Integer, Integer> entry : itemCounts.entrySet()) {
        int itemId = entry.getKey();
        int setCount = entry.getValue();
        double appearanceRate = (double) setCount / totalChampionCount;
        int wins = itemWins.getOrDefault(itemId, 0);
        double winRate = setCount == 0 ? 0 : (double) wins / setCount;
        result.add(new ItemWinRate(itemId, winRate, setCount, appearanceRate));
    }

    // 전설 아이템 목록과 비교하여 해당되지 않는 아이템을 제외
    result.removeIf(item -> !legendaryItemIds.contains(item.getItemId()));

    // 등장 횟수를 기준으로 내림차순으로 정렬
    result.sort(Comparator.comparingInt(ItemWinRate::getItemCount).reversed());

    // 상위 3개의 아이템만 리스트로 반환
    if (result.size() > 3) {
        result = result.subList(0, 3);
    }
    return result;

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
