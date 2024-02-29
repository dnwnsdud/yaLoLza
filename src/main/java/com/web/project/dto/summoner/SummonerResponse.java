package com.web.project.dto.summoner;

import lombok.Data;

@Data
public class SummonerResponse {
	private String id;
	private String accountId;
	private String puuid;
	private String name;
	private Long profileIconId;
	private Long revisionDate;
	private Long summonerLevel;
	

	
	//태그있을때 들어올값
	private String gameName;
	private String tagLine;


}