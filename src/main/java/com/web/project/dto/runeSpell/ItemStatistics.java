package com.web.project.dto.runeSpell;

public class ItemStatistics {
    private String championName;
    private String position;
    private int item;
    private int pickCount;
    private int winCount;
    private double winRate;
    private int totalCount;

    public String getChampionName() {
        return championName;
    }

    public String getPosition() {
        return position;
    }

    public int getItem() {
        return item;
    }

    public int getPickCount() {
        return pickCount;
    }

    public int getWinCount() {
        return winCount;
    }

    public double getWinRate() {
        return winRate;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
