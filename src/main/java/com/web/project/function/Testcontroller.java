package com.web.project.function;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.web.project.dao.summoner.SummonerRepository;
import com.web.project.dao.summoner.SummonerchampionsRepository;
import com.web.project.dao.summoner.SummonermatchsRepository;
import com.web.project.dao.summoner.SummonertierlogRepository;
import com.web.project.dto.sjw.summoner.Summoner;
import com.web.project.dto.sjw.summoner.SummonerLeagInfo;
import com.web.project.dto.sjw.summoner.SummonerResponse;
import com.web.project.save.MatchRepository;
import com.web.project.save.MatchService;




@Component
public class Testcontroller {
	
	@Autowired
	private MatchService matchService;	
	@Autowired
	private MatchRepository matchRepository;
	  
	@Autowired
	private SummonerRepository summonerRepository;

	@Autowired
	private SummonermatchsRepository summonermatchsRepository;

	@Autowired
	private SummonertierlogRepository summonertierlogRepository;

	@Autowired
	private SummonerchampionsRepository summonerchampionsRepository; 

	@Autowired
	private MillisecondsConverter conveter;
	  
	//매치비교클래스
	@Autowired
	private Calall calmatch;

	@Value("${lol.apikey}") 
	private String apiKey; 
	
	
	private final RestTemplate restTemplate;
	  
	  @Autowired 
	  public Testcontroller(RestTemplateBuilder restTemplateBuilder) {
	  this.restTemplate = restTemplateBuilder.build();
	  }

    // 디비서머너확인
    public Summoner findSummoner(String summonername, String tag) {
        if (tag != null && summonername.contains("-")) {
            String[] nameParts = summonername.split("-");
            summonername = nameParts[0].replace("%20", " ");
            tag = nameParts[1].replace("%20", " ");
        }

        return summonerRepository.findBySummonernameAndTag(summonername, tag);
    }
   //서머너 인포
    public ResponseEntity<SummonerResponse> getSummonerInfo(String puuid) {
        String puuidurl = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/" + puuid + "?api_key=" + apiKey;
        return restTemplate.getForEntity(puuidurl, SummonerResponse.class);
    }
    
    //ㅅ소환사 리그 인포
    public ResponseEntity<SummonerLeagInfo[]> getSummonerLeagueInfo(String encryptedSummonerId) {
        String leag4url = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + encryptedSummonerId + "?api_key=" + apiKey;
        return restTemplate.getForEntity(leag4url, SummonerLeagInfo[].class);
    }
    
    
    
    
    
    
}
    
    
    

