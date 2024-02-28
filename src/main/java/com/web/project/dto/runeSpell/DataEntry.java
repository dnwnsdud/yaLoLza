package com.web.project.dto.runeSpell;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class DataEntry {

	private long gameStartTime;//게임 시작 타임 아직은 안 쓸 예정
	private int championId;
	private String championName;
    private String individualPosition;
    private int item0;
    private int item1;
    private int item2;
    private int item3;
    private int item4;
    private int item5;
    private int item6;
    
    private Perks perks;
    
    private int summoner1Id;
    private int summoner2Id;
    private boolean win;

    
    public long getGameStartTime() {
		return gameStartTime;
	}
    
    public int getChampionId() {
		return championId;
	}
    
    public String getChampionName() {
		return championName;
	}
    
	public String getIndividualPosition() {
		return individualPosition;
	}

	public int getItem0() {
		return item0;
	}
	public int getItem1() {
		return item1;
	}
	
	public int getItem2() {
		return item2;
	}

	public int getItem3() {
		return item3;
	}
	
	public int getItem4() {
		return item4;
	}
	
	public int getItem5() {
		return item5;
	}
	
	public int getItem6() {
		return item6;
	}
	
	public Perks getPerks() {	   
	    return perks;
	}
	public int getSummoner1Id() {
		return summoner1Id;
	}
	
	
	public int getSummoner2Id() {
		return summoner2Id;
	}
	
	
	public boolean isWin() {
        return win;
    }
	
	
	
}