package com.web.project.system;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.project.dto.runeSpell.ItemListStatistics;
import com.web.project.dto.runeSpell.ItemStatistics;
import com.web.project.dto.runeSpell.SkillStatistics;

public class StatisticItemSkill {
	
	//
	public static List<ItemStatistics> parseJson(String rawData) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ItemStatistics> parsedData = new ArrayList<>();
        try {
            TypeReference<Map<String, ItemStatistics>> typeReference = new TypeReference<>() {};
            Map<String, ItemStatistics> dataMap = objectMapper.readValue(rawData, typeReference);
            for (ItemStatistics itemStatistics : dataMap.values()) {
                parsedData.add(itemStatistics);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parsedData;
    }

	
	public static List<ItemListStatistics> parseJson3(String rawData) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ItemListStatistics> parsedData = new ArrayList<>();
        try {
            TypeReference<Map<String, ItemListStatistics>> typeReference = new TypeReference<>() {};
            Map<String, ItemListStatistics> dataMap = objectMapper.readValue(rawData, typeReference);
            for (ItemListStatistics itemListStatistics : dataMap.values()) {
                parsedData.add(itemListStatistics);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parsedData;
    }

	//
	public static List<SkillStatistics> parseJson2(String rawData) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<SkillStatistics> parsedData = new ArrayList<>();
        try {
            TypeReference<Map<String, SkillStatistics>> typeReference = new TypeReference<>() {};
            Map<String, SkillStatistics> dataMap = objectMapper.readValue(rawData, typeReference);
            for (SkillStatistics skillStatistics : dataMap.values()) {
                parsedData.add(skillStatistics);
            }
                   
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parsedData;
    }

	public static List<ItemStatistics> filterStatistic(List<ItemStatistics> data, String tier, String position, String championName) {		    
		List<ItemStatistics> result = new ArrayList<>();
		    for (ItemStatistics entry : data) {
		        if (entry.getPosition() != null && entry.getChampionName() != null) {
		            if (entry.getPosition().equals(position) && entry.getChampionName().equals(championName)) {
		                result.add(entry);
		            }
		        }
		    }
		    return result;
		}
	
	public static List<ItemListStatistics> filterStatistic3(List<ItemListStatistics> data, String tier, String position, String championName) {		    
		List<ItemListStatistics> result = new ArrayList<>();
		    for (ItemListStatistics entry : data) {
		        if (entry.getPosition() != null && entry.getChampionName() != null) {
		            if (entry.getPosition().equals(position) && entry.getChampionName().equals(championName)) {
		                result.add(entry);
		            }
		        }
		    }
		    return result;
		}
	
	
	public static List<SkillStatistics> filterStatistic2(List<SkillStatistics> data, String tier, String position, String championName) {
		List<SkillStatistics> result = new ArrayList<>();
	    for (SkillStatistics entry : data) {
	        if (entry.getPosition() != null && entry.getChampionName() != null) {
	            if (entry.getPosition().equals(position) && entry.getChampionName().equals(championName)) {
	                result.add(entry);
	            }
	        }
	    }
	    return result;
	}
		

	public static List<ItemStatistics> caculateFirstItems(List<ItemStatistics> filteredStatistic){	
	        filteredStatistic.sort(Comparator.comparingDouble(ItemStatistics::getWinRate).reversed());	  
	        // 상위 2개 아이템만 추출
	        // 20번 이상 등장해야 함
	        List<ItemStatistics> topTwoItems = filteredStatistic.stream()
	        		.filter(stat-> stat.getPickCount() > 10 )
	                .limit(1)
	                .collect(Collectors.toList());
	        return topTwoItems;
	    }
	
	
	public static List<ItemListStatistics> caculateItems(List<ItemListStatistics> filteredStatistic){
			filteredStatistic.sort(Comparator.comparingDouble(ItemListStatistics::getWinRate).reversed());	  
	
        // 상위 5등 템트리 추출
        // 12번 이상 등장해야 함
        List<ItemListStatistics> Items = filteredStatistic.stream()
        		.filter(stat-> stat.getPickCount() > 5 )
                .limit(5)
                .collect(Collectors.toList());
        return Items;
    }
	
	
	public static List<SkillStatistics> caculateFirstSkills(List<SkillStatistics> filteredStatistic){	
		filteredStatistic.sort(Comparator.comparingDouble(SkillStatistics::getWinRate).reversed());	        

		List<SkillStatistics> firstSkill = filteredStatistic.stream()
        	.filter(stat-> stat.getPickCount() > 5 )
            .limit(3)
            .collect(Collectors.toList());
		return firstSkill;
}

	public static List<SkillStatistics> caculateSkills(List<SkillStatistics> filteredStatistic){		
		filteredStatistic.sort(Comparator.comparingDouble(SkillStatistics::getWinRate).reversed());	        
	    List<SkillStatistics> mostSkill = filteredStatistic.stream()
        		.filter(stat-> stat.getPickCount() > 5 )
	            .limit(3)
	            .collect(Collectors.toList());
	    return mostSkill;
	}
	
}
	
	
	
	

