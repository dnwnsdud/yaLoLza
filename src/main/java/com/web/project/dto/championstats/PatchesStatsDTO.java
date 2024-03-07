package com.web.project.dto.championstats;

public class PatchesStatsDTO {
    private int wins;        
    private int losses;      
    private int picks;       
    private double winRate;  
    private double pickRate; 
    private double banRate;

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getPicks() {
        return picks;
    }

    public double getWinRate() {
        return winRate;
    }

    public double getPickRate() {
        return pickRate;
    }

    public double getBanRate() {
        return banRate;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setPicks(int picks) {
        this.picks = picks;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public void setPickRate(double pickRate) {
        this.pickRate = pickRate;
    }

    public void setBanRate(double banRate) {
        this.banRate = banRate;
    }
}
