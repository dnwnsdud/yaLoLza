package com.web.project.dto.runeSpell;

import java.util.Set;

import lombok.Data;


public class SummonerSpellSetWinRate {
    private Set<Integer> spellSet; 	// 소환사 주문 세트
    private double winRate; 		// 해당 세트의 승률
    private int setCount;				// 해당 세트의 등장 횟수
    private double countRate;		// 해당 세트의 등장 비율

    public SummonerSpellSetWinRate(Set<Integer> spellSet, double winRate, int setCount, double countRate ) {
        this.spellSet = spellSet;
        this.winRate = winRate;
        this.setCount = setCount;
        this.countRate =countRate;
    }

    public Set<Integer> getSpellSet() {
        return spellSet;
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
