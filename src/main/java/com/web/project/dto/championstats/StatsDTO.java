package com.web.project.dto.championstats;

public class StatsDTO {
    private int wins;
    private int losses;
    private int picks;
    private int bans;
    private int kills;
    private int deaths;
    private int assists;
    private int goldEarned;
    private int cs;
    private double kda;
    private double winrate;
    private double pickrate;
    private double banrate;
    private double tierScore;
    private String champtier;
    
       
    
    
    
	public double getTierScore() {
		return tierScore;
	}
	public void setTierScore(double tierScore) {
		this.tierScore = tierScore;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	public int getPicks() {
		return picks;
	}
	public void setPicks(int picks) {
		this.picks = picks;
	}
	public int getBans() {
		return bans;
	}
	public void setBans(int bans) {
		this.bans = bans;
	}
	public int getKills() {
		return kills;
	}
	public void setKills(int kills) {
		this.kills = kills;
	}
	public int getDeaths() {
		return deaths;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	public int getAssists() {
		return assists;
	}
	public void setAssists(int assists) {
		this.assists = assists;
	}
	public int getGoldEarned() {
		return goldEarned;
	}
	public void setGoldEarned(int goldEarned) {
		this.goldEarned = goldEarned;
	}
	public int getCs() {
		return cs;
	}
	public void setCs(int cs) {
		this.cs = cs;
	}
	public double getKda() {
		return kda;
	}
	public void setKda(double kda) {
		this.kda = kda;
	}
	public double getWinrate() {
		return winrate;
	}
	public void setWinrate(double winrate) {
		this.winrate = winrate;
	}
	public double getPickrate() {
		return pickrate;
	}
	public void setPickrate(double pickrate) {
		this.pickrate = pickrate;
	}
	public double getBanrate() {
		return banrate;
	}
	public void setBanrate(double banrate) {
		this.banrate = banrate;
	}
	public String getChamptier() {
		return champtier;
	}
	public void setChamptier(String champtier) {
		this.champtier = champtier;
	} 
}