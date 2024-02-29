package com.web.project.function;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import com.web.project.dto.summoner.Summonermatchs;

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
	    
	
}

