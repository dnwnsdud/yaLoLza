package com.web.project.dto.runeSpell;

import org.springframework.stereotype.Component;

import lombok.Data;


public class RuneWinRate {
	
	private String mainRune; 	// 메인룬
    private double winRate; 	// 해당 룬의 승률
    private int setCount;		// 해당 룬의 등장 횟수
    private double countRate;	// 해당 룬의 등장 비율
    
    
    public RuneWinRate(String mainRune, double winRate, int setCount, double countRate) {
		this.mainRune = mainRune;
		this.winRate = winRate;
		this.setCount = setCount;
		this.countRate = countRate;
	}
    public String getMainRune() {
        return mainRune;
    }
    
    public double getWinRate() {
        return winRate;
    }
    
    public int getSetCount() {
        return setCount;
    }
    
    public double getCountRate() {
        return countRate;
    }
    
}
