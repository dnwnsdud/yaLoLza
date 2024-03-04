package com.web.project.dto.runeSpell;

import java.util.Set;

public class ItemWinRate {

	private int itemId;
	private double winRate; 		// 템 승률
	private int itemCount;			// 템 등장 횟수
	private double pickRate;		// 템 등장 비율
	
	public ItemWinRate(int itemId, double winRate, int itemCount, double pickRate) {
		this.itemId = itemId;
		this.winRate = winRate;
		this.itemCount = itemCount;
		this.pickRate = pickRate;
	}

	public int getItemId() {
        return itemId;
    }

    public double getWinRate() {
        return winRate;
    }
    
    public int getItemCount() {
        return itemCount;
    }
    
    public double getpickRate() {
        return pickRate;
    }



}
