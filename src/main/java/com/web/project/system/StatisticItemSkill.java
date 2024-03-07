package com.web.project.system;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.project.dto.runeSpell.ItemStatistics;
import com.web.project.dto.runeSpell.SkillStatistics;

public class StatisticItemSkill {
	
	
	public static List<ItemStatistics> parseJson(String rawData) {//json parse하는 함수
        ObjectMapper objectMapper = new ObjectMapper();
        try {											//selections같은 타입 유지
            return objectMapper.readValue(rawData, new TypeReference<List<ItemStatistics>>() {});
 
        } catch (Exception e) {
            e.printStackTrace();   
            return Collections.emptyList(); // 빈 리스트 반환
        }
    }
	
	public static List<SkillStatistics> parseJson2(String rawData) {//json parse하는 함수
        ObjectMapper objectMapper = new ObjectMapper();
        try {											//selections같은 타입 유지
            return objectMapper.readValue(rawData, new TypeReference<List<SkillStatistics>>() {});
 
        } catch (Exception e) {
            e.printStackTrace();   
            return Collections.emptyList(); // 빈 리스트 반환
        }
    }
	
	public static List<ItemStatistics> filterStatistic(List<ItemStatistics> data, String tier, String position, String championName) {
        return data.stream()
                .filter(entry -> entry.getPosition().equals(position)
                        && entry.getChampionName().equals(championName))
                .collect(Collectors.toList());
    }
	
	public static List<SkillStatistics> filterStatistic2(List<SkillStatistics> data, String tier, String position, String championName) {
		return data.stream()
				.filter(entry -> entry.getPosition().equals(position)
						&& entry.getChampionName().equals(championName))
				.collect(Collectors.toList());
	}
	
	public static List<ItemStatistics> caculateFirstItems(List<ItemStatistics> filteredStatistic){
		
	        filteredStatistic.sort(Comparator.comparingDouble(ItemStatistics::getWinRate).reversed());	        
	        // 상위 2개 아이템만 추출
	        List<ItemStatistics> topThreeItems = filteredStatistic.stream()
	                .limit(2)
	                .collect(Collectors.toList());
	        return topThreeItems;
	    }
	
	public static List<ItemStatistics> caculateItems(List<ItemStatistics> filteredStatistic){
	
		filteredStatistic.sort(Comparator.comparingDouble(ItemStatistics::getWinRate).reversed());	        
		List<ItemStatistics> ItemList = filteredStatistic.stream()
            .limit(5)
            .collect(Collectors.toList());
			return ItemList;
		}
	
	
	public static List<SkillStatistics> caculateFirstSkills(List<SkillStatistics> filteredStatistic){
	
		filteredStatistic.sort(Comparator.comparingDouble(SkillStatistics::getWinRate).reversed());	        

		List<SkillStatistics> firstSkill = filteredStatistic.stream()
            .limit(3)
            .collect(Collectors.toList());
		return firstSkill;
}

	public static List<SkillStatistics> caculateSkills(List<SkillStatistics> filteredStatistic){
		
		filteredStatistic.sort(Comparator.comparingDouble(SkillStatistics::getWinRate).reversed());	        

	    List<SkillStatistics> mostSkill = filteredStatistic.stream()
	            .limit(3)
	            .collect(Collectors.toList());
	    return mostSkill;
	}
	
}
	
	
	
	

