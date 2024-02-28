package com.web.project.dto.runeSpell;

import java.util.Set;

public class SummonerSpellSetWinRate {
    private Set<Integer> spellSet; 	// 소환사 주문 세트
    private double winRate; 		// 해당 세트의 승률

    public SummonerSpellSetWinRate(Set<Integer> spellSet, double winRate) {
        this.spellSet = spellSet;
        this.winRate = winRate;
    }

    public Set<Integer> getSpellSet() {
        return spellSet;
    }

    public double getWinRate() {
        return winRate;
    }
}
