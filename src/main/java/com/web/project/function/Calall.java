package com.web.project.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import com.web.project.dto.sjw.summoner.Summonermatchs;

@Component
 public class Calall {
	//  List<Object[]> userchamps;
    //  List<Summonermatchs> summonermatch;
      
	  //매치비교 
	  public static List<String> nocompareLists(List<String> matchnumList, List<String> matchidsList) {
	     
        
	        List<String> nonMatchingValues = matchidsList.stream()
	                .filter(value -> !matchnumList.contains(value))
	                .collect(Collectors.toList());

	        return  nonMatchingValues;
	    }
    
      //매치 정렬
	    public static List<String> sortListByNumber(List<String> inputList) {
	        return inputList.stream()
	                .sorted(Comparator.comparing(Calall::extractNumber).reversed())
	                .collect(Collectors.toList());
	    }

	    private static long extractNumber(String str) {
	        // "KR_" 다음에 오는 숫자를 추출하는 메서드
	        return Long.parseLong(str.substring(3));
	    }
	    
	    //스킬 순서 코드
	    public static List<Long> getTopNFrequentNumbers(List<Long> inputList, int n) {
	        // 숫자별 빈도수를 저장하는 맵
	        Map<Long, Integer> frequencyMap = new HashMap<>();

	        // 빈도수를 계산하고 맵에 저장
	        for (Long num : inputList) {
	            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
	        }

	        // 빈도수를 기준으로 내림차순으로 정렬
	        List<Map.Entry<Long, Integer>> entries = new ArrayList<>(frequencyMap.entrySet());
	        entries.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));

	        // 상위 n개의 숫자를 추출하여 결과 리스트에 추가
	        List<Long> result = new ArrayList<>();
	        for (int i = 0; i < n && i < entries.size(); i++) {
	            result.add(entries.get(i).getKey());
	        }

	        return result;
	    }
	    
	
}

