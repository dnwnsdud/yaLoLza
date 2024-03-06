
  package com.web.project.page.controller;
  
  import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.web.project.dao.summoner.SummonerRepository;
import com.web.project.dao.summoner.SummonerchampionsRepository;
import com.web.project.dao.summoner.SummonermatchsRepository;
import com.web.project.dao.summoner.SummonertierlogRepository;
import com.web.project.dto.info.Perk;
import com.web.project.dto.info.Runes;
import com.web.project.dto.info.Champion.Spell;
import com.web.project.dto.sjw.TimeLine.Events;
import com.web.project.dto.sjw.TimeLine.Frames;
import com.web.project.dto.sjw.TimeLine.TimeLine;
import com.web.project.dto.sjw.match.Info;
import com.web.project.dto.sjw.match.Match;
import com.web.project.dto.sjw.match.Participants;
import com.web.project.dto.sjw.summoner.Summoner;
import com.web.project.dto.sjw.summoner.SummonerLeagInfo;
import com.web.project.dto.sjw.summoner.SummonerResponse;
import com.web.project.dto.sjw.summoner.Summonerchampions;
import com.web.project.dto.sjw.summoner.Summonermatchs;
import com.web.project.dto.sjw.summoner.Summonertierlog;
import com.web.project.function.Calall;
import com.web.project.function.MillisecondsConverter;
import com.web.project.function.SummonerTimeList;
import com.web.project.function.SummonerTimeListSS;
import com.web.project.save.MatchRepository;
import com.web.project.save.MatchService;
import com.web.project.system.ChampionData;
import com.web.project.system.RuneData;
import com.web.project.system.SummonerData;
 

  @Controller  
  @RequestMapping("/yalolza.gg") 
  public class SummonerGameResult {
	  
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
  public SummonerGameResult(RestTemplateBuilder restTemplateBuilder) {
  this.restTemplate = restTemplateBuilder.build();
  }
    
  @GetMapping("/summoners/{name}")
  public String findUser(@PathVariable String  name, Model model,String keyword) {	      
	    String summonername =  name.replace("%20", " ");
	    String tag = "KR1";
	    String sommenerurl;
	    String puuidurl;
	    String match5url; 
	    String encryptedSummonerId;
	    String leag4url;
	    //조회할 매치 갯수
	    long matchnum = 10; 
	    //매치인포 리스트
	    List<ResponseEntity<Match>> matchsssinfoList = new ArrayList<>();
	    //찾을 소환사
	    Summoner findsummoner;
	    //등록될 소환사
	    Summoner newSummoner;
	    //등록될 티어 로그
	    Summonertierlog  tierlog;
	    //등록될 매치들
	    Summonermatchs  summonermatchs;
	    //매치 기록
	    ResponseEntity<Match> matchdata2;
	    //데이트
        Date dbdate =new Date();
        //소환사정보
        ResponseEntity<SummonerResponse> summoner;
        //기록 저장
        ResponseEntity<SummonerResponse> sommenerinfo;
        //기록저장
        ResponseEntity<SummonerLeagInfo[]> sommenerleagueinfo;
        //puuid 
        String puuid; 
        //매치리스트 
        List<Participants>  matchParticipants;
        //kda 결과값
		 Double kdacalresult = 0.001;
		 //타임라인 리스트
		 ResponseEntity<TimeLine>  matchdatatimeList;
		 //소환사 메치에 해당 넘버
		 List<Integer> summonerGameNumber = new ArrayList<>();
		 
		 //소환사 아이템구매 이벤트
		 List<Events> summonerItemPurchasedEvents;
		 
		 //소환사 아이템구매 이벤트
		 List<Events> summonerItemSkillEvents;	
		 
		 List<List<Object[]>> testobject = new ArrayList<>();
		 
		 //경기 소환사 리스트
	     List<SummonerTimeList>  summnnerItemTimeStemp;
	      
		 //그 리스트를 담을 리스트   
    	  List<List<List<Long>>>  summnnerItemTimeStempSS = new ArrayList<List<List<Long>>>(); ; 
         //리스트변수
    	  SummonerTimeListSS summnoenrss = null;
		   //스킬담자 
		   List<Long>  summonerskilllup =new ArrayList<>();;
		     
		   //스킬을 담을 리스트 
		   List<List<Long>> summonerskilllups = new ArrayList<List<Long>>();
		   
		   //스킬트리리스트
		   List<Long> skilltree = new ArrayList<>();
		   
		   //스킬 먼저찍을걸 담을 리스트
		   List<List<Long>> summonerskillluptree = new ArrayList<List<Long>>();
		   //일회용 소환사 모스뚜뚜뚜뚜뚜뚜뚜
		   List<Summonerchampions> summonermost = new ArrayList<Summonerchampions>();
		   
		   //모스트들 
		   Object[] allstatistics;
		    //챔피언리스트
		   List<Object[]> championStatisticsss;

        //@태그가 있을때 찾는 유저 저장 방식
        if (name.contains("-")) {
	        String[] nameParts = name.split("-");
	         summonername = nameParts[0].replace("%20", " ");
	         tag = nameParts[1].replace("%20", " ");
   
	         findsummoner = summonerRepository.findBySummonernameAndTag(summonername, tag);
	     //태그가 없을때 찾는 유저 저장방식    
        }else {
	         findsummoner = summonerRepository.findBySummonernameAndTag(summonername, tag);
        }
        
        sommenerurl = 
   			  "https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/"+summonername+"/"+tag+"?api_key="+apiKey;
      
	   //잘못된 서머너 입력시 트라이케치로 피이지 이동
        System.out.println("완료1");
	      try {
	    	    summoner =  restTemplate.getForEntity(sommenerurl, SummonerResponse.class);
		} catch (Exception e) {
		  return "xpage";
		} 
	      System.out.println("완료2");
  	    	puuid = summoner.getBody().getPuuid();
	  	    //summonerV5 정보 추출
		  	puuidurl = 
		  		  	 "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/" + puuid + "?api_key=" + apiKey;
		     sommenerinfo =  restTemplate.getForEntity(puuidurl, SummonerResponse.class);	
		     
		    
		    //league-v4 티어 전적 추출
		    encryptedSummonerId = sommenerinfo.getBody().getId();
		    leag4url =
		  		  "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + encryptedSummonerId + "?api_key=" + apiKey;
		    sommenerleagueinfo = restTemplate.getForEntity(leag4url, SummonerLeagInfo[].class);
		 String recenttier  =  sommenerleagueinfo.getBody().length != 0 ?  sommenerleagueinfo.getBody()[0].getTier() : "unranked";               
         Long leaguePoints = sommenerleagueinfo.getBody().length != 0 ?  sommenerleagueinfo.getBody()[0].getLeaguePoints() : 0; 
         Long summonerwins = sommenerleagueinfo.getBody().length != 0 ?  sommenerleagueinfo.getBody()[0].getWins() : 0; 
         Long summonerlosses = sommenerleagueinfo.getBody().length != 0 ?  sommenerleagueinfo.getBody()[0].getLosses() : 0;
         
 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
         
      if(findsummoner == null) {
    	 //찾는 유저가 db에 없을때   

            newSummoner = Summoner.builder()
	                     .summonername(summonername)
	                     .tag(tag)
	                     .summonerLevel(sommenerinfo.getBody().getSummonerLevel())
	                     .profileIconId(sommenerinfo.getBody().getProfileIconId())
	                     .recenttier(recenttier)
	                     .leaguePoints(leaguePoints)
	                     .wins(summonerwins)
	                     .losses(summonerlosses)
	                     //나중에 여기서 if문으로 자랭도 추가해보쟈
	                     .build();
	             summonerRepository.save(newSummoner);
			   
	            tierlog = Summonertierlog.builder()
	            		.summoner(newSummoner)
	            		.summonertier(recenttier)
	            		.point(leaguePoints)
	            		.build(); 
	             summonertierlogRepository.save(tierlog);
	             
  
		  		   //puuid로 최근 매치추출   
		  		    match5url = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid;
		  		    String finalmatch5url = match5url + "/ids?start=0&count="+matchnum+"&api_key=" + apiKey;
		  		    ResponseEntity<String[]> match5s = restTemplate.getForEntity(finalmatch5url, String[].class);
		  		    String[] matchids =match5s.getBody();
	   

		  		  System.out.println("이게리스트다" + Arrays.toString(matchids));
		  		    //match-v5로 각 매치 정보 추출		    
		  		    //1)url추출
		  		    List<String> matchsssList = new ArrayList<>();
		  		    for (int i = 0; i < matchids.length; i++) {
		  		      String matchinfoUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchids[i] + "/?api_key=" + apiKey;
		  		      matchsssList.add(matchinfoUrl);
		  		    }
		  		     
		  		  
		  		    //1)mattime 큐id이용  타임스탬프 추출
		  		    List<String> matchstimestamp = new ArrayList<>();
		  		    for (int i = 0; i < matchids.length; i++) {
		  		      String matchtimefoUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchids[i] +"/timeline"+ "?api_key=" + apiKey;
		  		    matchstimestamp.add(matchtimefoUrl);
		  		    }
  
		  		   //2)메치인포 추출  이게 좀 걸린다
		  		    System.out.println(matchsssList.size());
		  		    for (int i = 0; i < matchsssList.size(); i++) {
	 	   		    	
		  		    	System.out.println("시작 터지나 " + i);
		  			    matchdata2 = restTemplate.getForEntity(matchsssList.get(i), Match.class);
		  			    matchdata2.getBody().updateCaludate();
		  			    matchdata2.getBody().teammaxdfs();
		  			    matchdata2.getBody().teammaxdmg();
                         
		  			    //메치아이디 큐타입아이디 저장
		  			  System.out.println("큐타입아이디");
			  			  summonermatchs = Summonermatchs.builder()
			  					  .summoner(newSummoner)
				  				  .matchnum(matchids[i])
				  				  .queueid(matchdata2.getBody().getInfo().getQueueId())
				  				  .build();
			  			      summonermatchsRepository.save(summonermatchs); 
			  			    Long startTime = matchdata2.getBody().getInfo().getGameCreation();
                           Long endTime = matchdata2.getBody().getInfo().getGameEndTimestamp();
                           Long resulttime = conveter.convertMilliseconds(endTime-startTime);
                           

			  			     matchParticipants = matchdata2.getBody().getInfo().getParticipants();

			  			    for (int z = 0; z < matchParticipants.size(); z++) {

			  		           if(matchParticipants.get(z).getDeaths() > 0) { 	

			  		        	 kdacalresult = Math.round(((matchParticipants.get(z).getKills()+matchParticipants.get(z).getAssists())/(double)matchParticipants.get(z).getDeaths())*10)/ 10.0;
	  					           }else if(matchParticipants.get(z).getDeaths() ==0){

	  					        	 kdacalresult=10.0; 
		  					           } 

	                         //소환사 이름 받아오기
				  			    String  matchname = matchParticipants.get(z).getSummonerName().toLowerCase();
				  			    String  matchname2 = matchParticipants.get(z).getRiotIdGameName().toLowerCase();	
				  			       if(matchname.equals(summonername.toLowerCase()) || matchname2.equals(summonername.toLowerCase())) {
				  			     	   System.out.println("여기안들어와있나봐");
				  			    	 summonerGameNumber.add(z);
				  			    	System.out.println("사이즈" +summonerGameNumber.size());
	                                //매치에서 소환사 정보 넣어주기
				  			    	  long totalteamkills =
				  			    			  matchdata2.getBody().getInfo().getParticipants().get(z).getTeamId() == 100 ? 
				  			    			  matchdata2.getBody().getInfo().getTeams().get(0).getObjectives().getChampion().getKills()	:
				  			    			  matchdata2.getBody().getInfo().getTeams().get(1).getObjectives().getChampion().getKills();
				  					    Summonerchampions summonerchampions = Summonerchampions.builder()
				  					              // Summoner를 설정
				  					  		.summoner(newSummoner)
			  					            .position(matchParticipants.get(z).getTeamPosition())
			  					            .champion(matchParticipants.get(z).getChampionName())
			  					            .csaverage(Math.round((matchParticipants.get(z).getTotalMinionsKilled().doubleValue()/resulttime.doubleValue())*10)/10.0)
			  					            .championkda(kdacalresult)
			  					            .damagedealt(matchParticipants.get(z).getTotalDamageDealtToChampions())
			  					            .damagetaken(matchParticipants.get(z).getTotalDamageTaken())
			  					             .win(matchParticipants.get(z).getWin() == true ? 1 : 0)
			  					            .queueId(matchdata2.getBody().getInfo().getQueueId())
			  					            .championkills(matchParticipants.get(z).getKills())
			  					            .championdeaths(matchParticipants.get(z).getDeaths())
			  					            .championassists(matchParticipants.get(z).getAssists())
			  					            .killpers((matchParticipants.get(z).getAssists() + matchParticipants.get(z).getKills())*100 / (totalteamkills == 0 ? 1 : totalteamkills))
			  					            .build();
				  					    summonerchampionsRepository.save(summonerchampions);
				  					  //모스트정리  
				  					  summonermost.add(summonerchampions); 
				  					//넘버추가
				  					  
				  					
                                
			  			    }		 	  				   
			  			   } 
			  			    matchsssinfoList.add(matchdata2);

			  			}  

			  		  
             System.out.println("서머너사이즈"+summonermost.size());
		  		  //모스트추출
		  		    
		  		 //일단 전체 
		    		    
		  		    long totalWins = summonermost.stream().mapToLong(Summonerchampions::getWin).sum();
		  		    long averageKills = (long) (summonermost.stream().mapToDouble(Summonerchampions::getChampionkills).average().orElse(0.0) * 10) / 10;
		  		    long averageDeaths = (long) (summonermost.stream().mapToDouble(Summonerchampions::getChampiondeaths).average().orElse(0.0) * 10) / 10;
		  		    long averageAssists = (long) (summonermost.stream().mapToDouble(Summonerchampions::getChampionassists).average().orElse(0.0) * 10) / 10;
		  		    double averageKDA = Math.round(summonermost.stream().mapToDouble(Summonerchampions::getChampionkda).average().orElse(0.0) * 100.0) / 100.0;
		  		    double averageKillPer = summonermost.stream().mapToDouble(Summonerchampions::getKillpers).average().orElse(0.0);
		  		  long killconcern=  summonermost.stream().mapToLong(Summonerchampions::getWin).sum() * 100/summonermost.size();
		  		   long roundKillPer  = Math.round(averageKillPer * 10) / 10;   
		  		       allstatistics = new Object[]{
		  			    totalWins,
		  			    averageKills,
		  			    averageDeaths,
		  			    averageAssists,
		  			    averageKDA,
		  			    roundKillPer,
		  			    killconcern,
		  			    matchnum
		  			  }; 
		  	       // 찐모스트 구하는법
 
		  	        //이건나머지
		  		  Map<String, DoubleSummaryStatistics> championStatsMap = summonermost.stream()
		  		        .collect(Collectors.groupingBy(
		  		                Summonerchampions::getChampion,
		  		                Collectors.summarizingDouble(Summonerchampions::getChampionkills)
		  		        ));

		  		championStatisticsss = championStatsMap.entrySet().stream()
		  		        .map(entry -> {
		  		            String championName = entry.getKey();
		  		            DoubleSummaryStatistics killsStats = entry.getValue();

		  		            DoubleSummaryStatistics deathsStats = summonermost.stream()
		  		                    .filter(s -> Objects.equals(s.getChampion(), championName))
		  		                    .collect(Collectors.summarizingDouble(Summonerchampions::getChampiondeaths));

		  		            DoubleSummaryStatistics assistsStats = summonermost.stream()
		  		                    .filter(s -> Objects.equals(s.getChampion(), championName))
		  		                    .collect(Collectors.summarizingDouble(Summonerchampions::getChampionassists));

		  		          DoubleSummaryStatistics kdaStats = summonermost.stream()
		  		                .filter(s -> Objects.equals(s.getChampion(), championName))
		  		                .mapToDouble(s -> s.getChampiondeaths() == 0 ? 10.0 : (s.getChampionkills() + s.getChampionassists()) / (double) s.getChampiondeaths())
		  		                .summaryStatistics();
		  		            
		  		          DoubleSummaryStatistics killperStats = summonermost.stream()
		  		                    .filter(s -> Objects.equals(s.getChampion(), championName))
		  		                    .collect(Collectors.summarizingDouble(Summonerchampions::getKillpers));

		  		            long winSum = summonermost.stream()
		  		                    .filter(s -> Objects.equals(s.getChampion(), championName) && s.getWin() == 1)
		  		                    .mapToLong(Summonerchampions::getWin) 
		  		                    .sum();
     
		  		            return new Object[]{
		  		                    championName,
		  		                    killsStats.getCount(),    // 횟수
		  		                    winSum,                             // 승리 합
		  		                    Math.round(killsStats.getAverage() * 10) / 10,        // kills 평균
		  		                    Math.round(deathsStats.getAverage() * 10) / 10,       // deaths 평균
		  		                    Math.round(assistsStats.getAverage() * 10) / 10,      // assists 평균 
		  		                    String.format("%.2f", kdaStats.getAverage()),         // kda 평균
		  		                    Math.round(killperStats.getAverage() * 10) / 10       //킬관여 평균
		  		            };
		  		        })
		  		        .collect(Collectors.toList());
		  		
		  		//정렬맨
		  		Collections.sort(championStatisticsss, Comparator
		  		        .<Object[], Long>comparing(statistics -> (Long) statistics[1])  // 횟수를 기준으로 내림차순 정렬
		  		        .reversed()
		  		        .thenComparing(Comparator.<Object[], Double>comparing(statistics -> Double.valueOf(statistics[6].toString())).reversed())  // 횟수가 같으면 킬관여 평균을 기준으로 내림차순 정렬
		  		);
		  		  

		  		    
		  		   //타임 스탬프 시작
		  		    for(int i = 0; i < matchstimestamp.size(); i++) {
		  		    int summnosss = summonerGameNumber.get(i) + 1;
		  		    
		  		   //타임스탬프트들어갔고 	
		            matchdatatimeList = restTemplate.getForEntity(matchstimestamp.get(i), TimeLine.class);
                   
		            //프레임안에 이벤트가 있다 
		            List<Frames>   timelineframes  = matchdatatimeList.getBody().getInfo().getFrames();

					 //경기 소환사 리스트
				      summnnerItemTimeStemp  = new ArrayList<SummonerTimeList>();
				      
					     //스킬담자 
					  summonerskilllup = new ArrayList<Long>();

		            //프레임 길이 별로 확인
		           for(int f = 0 ; f <timelineframes.size(); f++ ) {


		        	   summonerItemPurchasedEvents  =   timelineframes.get(f).filterItemPurchasedEvents();
			        	 List<Events>   summonerTimePurchase = summonerItemPurchasedEvents.stream()
	        			 .filter(event -> event.getParticipantId().intValue() == summnosss )
	        			 .collect(Collectors.toList());
			        	 
			        	   summonerItemSkillEvents  =  timelineframes.get(f).filterSkillLevelUpEvents();
			        	   
			        	   List<Long> summonerTimeskill = summonerItemSkillEvents.stream()
			        			    .filter(event -> event.getParticipantId().intValue() == summnosss)
			        			    .map(s -> s.getSkillSlot())
			        			    .filter(skill -> skill != null)  // null 체크
			        			    .filter(skill -> skill != 0)    // 빈 배열이 아닌 경우
			        			    .collect(Collectors.toList());
			        	   
			        	   for(Long s : summonerTimeskill) {
			        		   summonerskilllup.add(s);
			        	   }
			      //스킬트리순서정립
			      skilltree = Calall.getTopNFrequentNumbers(summonerskilllup,3);

                      
			      for(int x =0 ; x<summonerTimePurchase.size() ; x++ ) {			    	
			            SummonerTimeList summonerTime = SummonerTimeList.builder()
			                    .purchasetime(summonerTimePurchase.get(x).getTimestamp() / 60000)
			                    .purchaseitem(summonerTimePurchase.get(x).getItemId())
			                    .build();     
			            summnnerItemTimeStemp.add(summonerTime); 
			            
			            summnoenrss =SummonerTimeListSS.builder()
				        		   .summonerTimeList(summnnerItemTimeStemp)
				        		   .build();

		       }       

			}  
		           
		           
		           Map<Long, List<Long>> sortedGroupedPurchaseItems = summnnerItemTimeStemp.stream()
		                   .collect(Collectors.groupingBy(
		                           SummonerTimeList::getPurchasetime,
		                           LinkedHashMap::new,
		                           Collectors.mapping(SummonerTimeList::getPurchaseitem, Collectors.toList())
		                   ));

		           List<List<Long>> resultList = sortedGroupedPurchaseItems.entrySet().stream()
		                   .map(entry -> {
		                       List<Long> result = entry.getValue();
		                       result.add(0, entry.getKey());
		                       return result;
		                   })
		                   .collect(Collectors.toList());
		            
			     summnnerItemTimeStempSS.add(resultList);        
                 summonerskilllups.add(summonerskilllup);
                 summonerskillluptree.add(skilltree);
	      }
     
   System.out.println("리스트야" +summonerskillluptree);
   System.out.println("리스트 개수야" +summonerskillluptree.size());
	 	  		  
		  		 Summoner  collectsummoner  = summonerRepository.findBySummonernameAndTag(summonername,tag);
		  		 List<Object[]> summonerchamsper =summonerchampionsRepository.findAvgStatsAndCountByChampionGroupByChampion(collectsummoner.getId());
		  		 
		  		Map<Long, String>keysquetype = Calall.createQueueTypeMap();
		   		  //진비 챔피언 이름 영어-> 한글
		  		 Map<String, String> keysChamName = ChampionData.keysChamName();
		  		Map<Long, String> keysRuneImage = RuneData.keysRuneImage();
		  		Map<Long, String> keysChamSpell = SummonerData.keysSumSpellLong();
		  		Map<String, List<Spell>> keysChamSkill = ChampionData.keysChamSkill();
		  		Map<Integer, Integer> keysRunes=RuneData.keysRunes();
		  		List<Runes> runeslist = RuneData.runeslist();
		  		List<Perk> perklist = RuneData.perklist();
		  		System.out.println("이제넣을꺼야");
/////////////////////////////////////////////////////////////////////////////////////////////////////	  		 
				    model.addAttribute("summoner", collectsummoner);
				    model.addAttribute("summonermatchnum", summonerGameNumber);
				    model.addAttribute("summonerchamsper", summonerchamsper);
				    model.addAttribute("matchsssinfoList", matchsssinfoList);
	 	 		    
			  		model.addAttribute("matchspurchase", summnnerItemTimeStempSS);
			  		model.addAttribute("matchsskill", summonerskilllups);
			  		model.addAttribute("matchsskilltree", summonerskillluptree);
			  		//전체 경기
			  		model.addAttribute("summonerall", allstatistics);
			  	     //이름 판수 승수  k d a kda 킬관여
			  		model.addAttribute("summonermost", championStatisticsss);
			  		//챔피언이름 변환
			  		model.addAttribute("keysChamName", keysChamName);
			  		//룬 아이디 이름으로 변환
			  		model.addAttribute("keysRuneImage", keysRuneImage);
			  		//소환사 주문 넘버 스트링변환
			  		model.addAttribute("keysChamSpell", keysChamSpell);  
			  		//챔피언당 스킬 불러오기
			  		model.addAttribute("keysChamSkill", keysChamSkill);
			  		//룬 총 데이터
			  		model.addAttribute("runeslist", runeslist);
			  		//핵심룬의 아이디 -> 해당 룬이 룬리스트의 index 변환 맵
			  		model.addAttribute("keysRunes", keysRunes);
			  		//perk 데이터 리스트
			  		model.addAttribute("perklist", perklist);
			  		//큐타입 추가 
			  		model.addAttribute("keysquetype", keysquetype);
///////////////////////////////////////////////////////////////////////////////////////////////////			  		
  	 //찾는 유저가 db에 있을때
      }else {

    	  //키워드 유무는 갱신버튼의 유무
    	  // 키워드가 없는 경우의 처리
    	  if (keyword == null) { 
    	    Summoner  collectsummoner  = summonerRepository.findBySummonernameAndTag(summonername,tag);		  
		     //소환사//챔이름 ,kda,cs,가한딜,받은딜,판수,승수 
		     List<Object[]> summonerchamsper =summonerchampionsRepository.findAvgStatsAndCountByChampionGroupByChampion(findsummoner.getId());
			 //소환사 티어 정보
	    	 //List<Summonertierlog> sumonertierlogs = collectsummoner.getTierlog();
	    	 //소환사가 쓴  챔피언 목록
	    	 //List<Summonerchampions> summonerchams = collectsummoner.getChampions();
	    	 //소환사 리그id 리스트
			 List<Summonermatchs> summonermatch = collectsummoner.getMatchs();
		   
			 //리스트안에 정보로 매치 불러오기
		    List<String> matchsssList = new ArrayList<>();
		    
  		    for (int i = 0; i < summonermatch.size(); i++) {
  		      String matchinfoUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + summonermatch.get(i).getMatchnum() + "/?api_key=" + apiKey;
  		      matchsssList.add(matchinfoUrl);
  		    }
  		    
  		     
  		    
		    for (int i = 0; i < matchsssList.size(); i++) {
  			    matchdata2 = restTemplate.getForEntity(matchsssList.get(i), Match.class);
  			  matchdata2.getBody().updateCaludate(); 
			  matchdata2.getBody().teammaxdfs();
			  matchdata2.getBody().teammaxdmg();
			  matchsssinfoList.add(matchdata2);
			  
			    Long startTime = matchdata2.getBody().getInfo().getGameCreation();
                Long endTime = matchdata2.getBody().getInfo().getGameEndTimestamp();
                Long resulttime = conveter.convertMilliseconds(endTime-startTime);
			  
			  
  			  
  			  matchParticipants= matchdata2.getBody().getInfo().getParticipants();
			    for (int z = 0; z < matchParticipants.size(); z++) { 
			   	
			    
		           if(matchParticipants.get(z).getDeaths() > 0) { 		
		        	 kdacalresult = Math.round(((matchParticipants.get(z).getKills()+matchParticipants.get(z).getAssists())/(double)matchParticipants.get(z).getDeaths())*10)/ 10.0;
			           }else if(matchParticipants.get(z).getDeaths() ==0){
			        	 kdacalresult=10.0; 
				           } 
			       //솬사이름
	  			    String  matchname = matchParticipants.get(z).getSummonerName().toLowerCase();
	  			    String  matchname2 = matchParticipants.get(z).getRiotIdGameName().toLowerCase();
			       if(matchname.equals(summonername.toLowerCase()) || matchname2.equals(summonername.toLowerCase())) {
                  //매치에서 소환사 정보 넣어주기
			    	    summonerGameNumber.add(z);
                   
	  			    	  long totalteamkills =
	  			    			  matchdata2.getBody().getInfo().getParticipants().get(z).getTeamId() == 100 ? 
	  			    			  matchdata2.getBody().getInfo().getTeams().get(0).getObjectives().getChampion().getKills()	:
	  			    			  matchdata2.getBody().getInfo().getTeams().get(1).getObjectives().getChampion().getKills();
	  			    	  System.out.println(totalteamkills);
	  					    Summonerchampions summonerchampions = Summonerchampions.builder()
	  					        // Summoner를 설정 디비는 모스트를위해
	  					  		.summoner(collectsummoner)
  					            .position(matchParticipants.get(z).getTeamPosition())
  					            .champion(matchParticipants.get(z).getChampionName())
  					            .csaverage(Math.round((matchParticipants.get(z).getTotalMinionsKilled().doubleValue()/resulttime.doubleValue())*10)/10.0)
  					            .championkda(kdacalresult)
  					            .damagedealt(matchParticipants.get(z).getTotalDamageDealtToChampions())
  					            .damagetaken(matchParticipants.get(z).getTotalDamageTaken())
  					             .win(matchParticipants.get(z).getWin() == true ? 1 : 0)
  					            .queueId(matchdata2.getBody().getInfo().getQueueId())
  					            .championkills(matchParticipants.get(z).getKills())
  					            .championdeaths(matchParticipants.get(z).getDeaths())
  					            .championassists(matchParticipants.get(z).getAssists())
  					            .killpers((matchParticipants.get(z).getAssists() + matchParticipants.get(z).getKills())*100 / totalteamkills)
  					            .build();
	  					    
	  					  //모스트정리  
	  					  summonermost.add(summonerchampions);      
			    	      	    
			    	     
		    }			  				   
		   }
		    }
		   //모스트 20인
  		    long totalWins = summonermost.stream().mapToLong(Summonerchampions::getWin).sum();
  		    long averageKills = (long) (summonermost.stream().mapToDouble(Summonerchampions::getChampionkills).average().orElse(0.0) * 10) / 10;
  		    long averageDeaths = (long) (summonermost.stream().mapToDouble(Summonerchampions::getChampiondeaths).average().orElse(0.0) * 10) / 10;
  		    long averageAssists = (long) (summonermost.stream().mapToDouble(Summonerchampions::getChampionassists).average().orElse(0.0) * 10) / 10;
  		    double averageKDA = Math.round(summonermost.stream().mapToDouble(Summonerchampions::getChampionkda).average().orElse(0.0) * 100.0) / 100.0;
  		    double averageKillPer = summonermost.stream().mapToDouble(Summonerchampions::getKillpers).average().orElse(0.0);
  		    long killconcern=  summonermost.stream().mapToLong(Summonerchampions::getWin).sum() * 100/summonermost.size();
  		    long roundKillPer  = Math.round(averageKillPer * 10) / 10;  
  		       allstatistics = new Object[]{
		  			    totalWins,
		  			    averageKills,
		  			    averageDeaths,
		  			    averageAssists,
		  			    averageKDA,
		  			    roundKillPer,
		  			    killconcern,
		  			    matchnum
  			  }; 
		    
		    
		    
		    
		    
	  		  //모스트추출
	  		  Map<String, DoubleSummaryStatistics> championStatsMap = summonermost.stream()
	  		        .collect(Collectors.groupingBy(
	  		                Summonerchampions::getChampion,
	  		                Collectors.summarizingDouble(Summonerchampions::getChampionkills)
	  		        ));

	  		 championStatisticsss = championStatsMap.entrySet().stream()
	  		        .map(entry -> {
	  		            String championName = entry.getKey();
	  		            DoubleSummaryStatistics killsStats = entry.getValue();

	  		            DoubleSummaryStatistics deathsStats = summonermost.stream()
	  		                    .filter(s -> Objects.equals(s.getChampion(), championName))
	  		                    .collect(Collectors.summarizingDouble(Summonerchampions::getChampiondeaths));

	  		            DoubleSummaryStatistics assistsStats = summonermost.stream()
	  		                    .filter(s -> Objects.equals(s.getChampion(), championName))
	  		                    .collect(Collectors.summarizingDouble(Summonerchampions::getChampionassists));

		  		          DoubleSummaryStatistics kdaStats = summonermost.stream()
			  		                .filter(s -> Objects.equals(s.getChampion(), championName))
			  		                .mapToDouble(s -> s.getChampiondeaths() == 0 ? 10.0 : (s.getChampionkills() + s.getChampionassists()) / (double) s.getChampiondeaths())
			  		                .summaryStatistics();
	  		            
	  		          DoubleSummaryStatistics killperStats = summonermost.stream()
	  		                    .filter(s -> Objects.equals(s.getChampion(), championName))
	  		                    .collect(Collectors.summarizingDouble(Summonerchampions::getKillpers));

	  		            long winSum = summonermost.stream()
	  		                    .filter(s -> Objects.equals(s.getChampion(), championName) && s.getWin() == 1)
	  		                    .mapToLong(Summonerchampions::getWin) // Assuming `getWin` returns 1 for a win
	  		                    .sum();

	  		            return new Object[]{
	  		                    championName,
	  		                    killsStats.getCount(),    // 횟수
	  		                    winSum,                             // 승리 합
	  		                    Math.round(killsStats.getAverage() * 10) / 10,        // kills 평균
	  		                    Math.round(deathsStats.getAverage() * 10) / 10,       // deaths 평균
	  		                    Math.round(assistsStats.getAverage() * 10) / 10,      // assists 평균 
	  		                    String.format("%.2f", kdaStats.getAverage()),         // kda 평균
	  		                    Math.round(killperStats.getAverage() * 10) / 10       //킬관여 평균
	  		            };
	  		        })
	  		        .collect(Collectors.toList());
	  		//정렬맨
	  		Collections.sort(championStatisticsss, Comparator
	  		        .<Object[], Long>comparing(statistics -> (Long) statistics[1])  // 횟수를 기준으로 내림차순 정렬
	  		        .reversed()
	  		        .thenComparing(Comparator.<Object[], Double>comparing(statistics -> Double.valueOf(statistics[6].toString())).reversed())  // 횟수가 같으면 킬관여 평균을 기준으로 내림차순 정렬
	  		);

  		    //1)mattime 큐id이용  타임스탬프 추출
  		    List<String> matchstimestamp = new ArrayList<>();
  		    for (int i = 0; i < summonermatch.size(); i++) {
  		      String matchtimefoUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + summonermatch.get(i).getMatchnum() +"/timeline"+ "?api_key=" + apiKey;
  		    matchstimestamp.add(matchtimefoUrl);
  		    } 		  
  		    
	  		   //타임 스탬프 시작
  		    for(int i = 0; i < matchstimestamp.size(); i++) {
  		    	
  		    int summnosss = summonerGameNumber.get(i) + 1;		  	
  		   //타임스탬프트들어갔고 	
            matchdatatimeList = restTemplate.getForEntity(matchstimestamp.get(i), TimeLine.class);
           
            //프레임안에 이벤트가 있다 
            List<Frames>   timelineframes  = matchdatatimeList.getBody().getInfo().getFrames();

			 //경기 소환사 리스트
		      summnnerItemTimeStemp  = new ArrayList<SummonerTimeList>();
		      
			     //스킬담자 
			  summonerskilllup = new ArrayList<Long>();
 
            //프레임 길이 별로 확인
           for(int f = 0 ; f <timelineframes.size(); f++ ) {


        	   summonerItemPurchasedEvents  =   timelineframes.get(f).filterItemPurchasedEvents();
	        	 List<Events>   summonerTimePurchase = summonerItemPurchasedEvents.stream()
    			 .filter(event -> event.getParticipantId().intValue() == summnosss )
    			 .collect(Collectors.toList());
	        	 
	        	   summonerItemSkillEvents  =  timelineframes.get(f).filterSkillLevelUpEvents();
	        	   
	        	   List<Long> summonerTimeskill = summonerItemSkillEvents.stream()
	        			    .filter(event -> event.getParticipantId().intValue() == summnosss)
	        			    .map(s -> s.getSkillSlot())
	        			    .filter(skill -> skill != null)  // null 체크
	        			    .filter(skill -> skill != 0)    // 빈 배열이 아닌 경우
	        			    .collect(Collectors.toList());
	        	   
	        	   for(Long s : summonerTimeskill) {
	        		   summonerskilllup.add(s);
	        	   }
	        	 
	        	   skilltree = Calall.getTopNFrequentNumbers(summonerskilllup,3);
             
			      for(int x =0 ; x<summonerTimePurchase.size() ; x++ ) {			    	
			            SummonerTimeList summonerTime = SummonerTimeList.builder()
			                    .purchasetime(summonerTimePurchase.get(x).getTimestamp() / 60000)
			                    .purchaseitem(summonerTimePurchase.get(x).getItemId())
			                    .build();     
			            summnnerItemTimeStemp.add(summonerTime); 
			            
			            summnoenrss =SummonerTimeListSS.builder()
				        		   .summonerTimeList(summnnerItemTimeStemp)
				        		   .build();

			     }   
		   }    
           
           Map<Long, List<Long>> sortedGroupedPurchaseItems = summnnerItemTimeStemp.stream()
                   .collect(Collectors.groupingBy(
                           SummonerTimeList::getPurchasetime,
                           LinkedHashMap::new,
                           Collectors.mapping(SummonerTimeList::getPurchaseitem, Collectors.toList())
                   ));

           List<List<Long>> resultList = sortedGroupedPurchaseItems.entrySet().stream()
                   .map(entry -> {
                       List<Long> result = entry.getValue();
                       result.add(0, entry.getKey());
                       return result;
                   })
                   .collect(Collectors.toList());
           
           
           
	          
		      summnnerItemTimeStempSS.add(resultList);
		      summonerskilllups.add(summonerskilllup);
		      summonerskillluptree.add(skilltree);
		}
  		  Map<Long, String>keysquetype = Calall.createQueueTypeMap();
 		 Map<String, String> keysChamName = ChampionData.keysChamName();
 		Map<Long, String> keysRuneImage = RuneData.keysRuneImage();
 		Map<Long, String> keysChamSpell = SummonerData.keysSumSpellLong();
 		Map<String, List<Spell>> keysChamSkill = ChampionData.keysChamSkill();
 		Map<Integer, Integer> keysRunes=RuneData.keysRunes();
 		List<Runes> runeslist = RuneData.runeslist();
 		List<Perk> perklist = RuneData.perklist();
/////////////////////////////////////////////////////////////////////////////////////////////////////		    
		    model.addAttribute("test", "디비정보다 시키들아");
		    model.addAttribute("summoner", collectsummoner);
		    //이건통계라 넣자
		    model.addAttribute("summonermatchnum", summonerGameNumber);
		    model.addAttribute("summonerchamsper", summonerchamsper);
		    model.addAttribute("matchsssinfoList", matchsssinfoList);

	  		model.addAttribute("matchspurchase", summnnerItemTimeStempSS);
	  		model.addAttribute("matchsskill", summonerskilllups);
	  		model.addAttribute("matchsskilltree", summonerskillluptree);
	  		//전체 경기
	  		model.addAttribute("summonerall", allstatistics);
	  		//이름 판수 승수  k d a kda 킬관여
	  		model.addAttribute("summonermost", championStatisticsss);
	  		//챔피언이름 변환
	  		model.addAttribute("keysChamName", keysChamName);
	  		//룬 아이디 이름으로 변환
	  		model.addAttribute("keysRuneImage", keysRuneImage);
	  		//소환사 주문 넘버 스트링변환
	  		model.addAttribute("keysChamSpell", keysChamSpell);  
	  		//챔피언당 스킬 불러오기
	  		model.addAttribute("keysChamSkill", keysChamSkill);
	  		//룬리스트
	  		model.addAttribute("runeslist", runeslist);
	  		model.addAttribute("keysRunes", keysRunes);
	  		//perk 데이터 리스트
	  		model.addAttribute("perklist", perklist);
	  		//큐타입 추가 
	  		model.addAttribute("keysquetype", keysquetype);
/////////////////////////////////////////////////////////////////////////////////////////////////////
      } 
    	  else {
    		  System.out.println("키워드로왔어");
    		  //키워드 유무는 갱신버튼의 유무
    	        // 키워드가 있는 경우의 처리
    		  Summoner  collectsummoner  = summonerRepository.findBySummonernameAndTag(summonername,tag);
              
              collectsummoner.setProfileIconId(sommenerinfo.getBody().getProfileIconId());
              collectsummoner.setSummonerLevel(sommenerinfo.getBody().getSummonerLevel());
    		  collectsummoner.setRecenttier(recenttier);
    		  collectsummoner.setLeaguePoints(leaguePoints);
    		  collectsummoner.setWins(summonerwins);
    		  collectsummoner.setLosses(summonerlosses);
    		  System.out.println("어디서 터진거지 1");
 
	           summonerRepository.save(collectsummoner);  
	            tierlog = Summonertierlog.builder()
	            		.summoner(collectsummoner)
	            		.summonertier(recenttier)
	            		.point(leaguePoints)
	            		.build(); 
	             summonertierlogRepository.save(tierlog);
	             
	             System.out.println("어디서 터진거지 2");
		  		   //puuid로 최근 매치추출   
		  		    match5url = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid;
		  		    String finalmatch5url = match5url + "/ids?start=0&count="+matchnum+"&api_key=" + apiKey;
		  		    ResponseEntity<String[]> match5s = restTemplate.getForEntity(finalmatch5url, String[].class);
		  		    String[] matchids =match5s.getBody();
		  		    //어레이리스트로 변환 새로운 매치 리스트
			  		 List<String> newmatchidsList = Arrays.asList(matchids);
			  		 
		  		    //match-v5로 각 매치 정보 추출		    
				     List<Summonermatchs> summonermatch = collectsummoner.getMatchs();
				    
				     List<String> dbmatchnumList = summonermatch.stream()
				            .map(Summonermatchs::getMatchnum)
				            .collect(Collectors.toList());
				     
				     //이전 경기와 비교해서 있는지 없는지 유무
				     List<String>  resultMatchList = Calall.nocompareLists(dbmatchnumList,newmatchidsList);
				     System.err.println("이것이 갯수야" + resultMatchList);
				     //메치리스트
				     List<String> matchsssList = new ArrayList<>();
				     //타임스탬프
				     List<String> matchstimestamp = new ArrayList<>();
				     System.out.println("어디서 터진거지 3");
				     if(resultMatchList.size() == 0) {
				    	 //걸러진 매치기록이 하나도 없을때 그냥디비가지고온다
				  		    System.out.println("없으니 그냥할게");
				  		    for (int i = 0; i < dbmatchnumList.size(); i++) {
				  		      String matchinfoUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + dbmatchnumList.get(i) + "/?api_key=" + apiKey;
				  		      matchsssList.add(matchinfoUrl);
				  		    }
				  		    
				  		    //1)mattime 큐id이용  타임스탬프 추출
				  		    
				  		    for (int i = 0; i < dbmatchnumList.size(); i++) {
				  		      String matchtimefoUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + dbmatchnumList.get(i) +"/timeline"+ "/?api_key=" + apiKey;
				  		    matchstimestamp.add(matchtimefoUrl);
				  		    }				  		    
				     }
                     else if(resultMatchList.size() > 0) {
                    	 System.out.println("있었어?");
		    		  		    for (int i = 0; i < matchids.length; i++) {
		    		  		      String matchinfoUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchids[i] + "/?api_key=" + apiKey;
		    		  		      matchsssList.add(matchinfoUrl);
		    		  		    }
    		   		    
    		  		  
		    		  		    //1)mattime 큐id이용  타임스탬프 추출
		    		  		  System.out.println("있었어?12");
		    		 for (int i = 0; i < matchids.length; i++) {
		    		  		      String matchtimefoUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchids[i] +"/timeline"+ "?api_key=" + apiKey;
		    		  		    matchstimestamp.add(matchtimefoUrl);
		    		  		    }
                    	   
						      for (int i = 0; i < resultMatchList.size(); i++) {
								  
			                       summonermatchs = Summonermatchs.builder()
					  					  .summoner(collectsummoner)
						  				  .matchnum(resultMatchList.get(i))
						  				  .build();
					  			      summonermatchsRepository.save(summonermatchs); 
							    }
						      System.out.println("있었어?34");
					      //걸러진 매치 기록이 있을때
				  		    //1) 디비 저장할 리스트 충 추출matchurl추출	
						      List<String> fordbmatchs= new ArrayList<>();
				  		    for (int i = 0; i < resultMatchList.size(); i++) {
				  		      String matchinfoUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + resultMatchList.get(i) + "/?api_key=" + apiKey;
				  		    fordbmatchs.add(matchinfoUrl);
				  		    }
				  		     
				  		  for (int i = 0; i < fordbmatchs.size(); i++) {
				  			System.out.println("있었어?232323");
				  			       matchdata2 = restTemplate.getForEntity(matchsssList.get(i), Match.class);
				  			       matchParticipants = matchdata2.getBody().getInfo().getParticipants();
				  	  			    matchdata2.getBody().updateCaludate(); 
				  				    matchdata2.getBody().teammaxdfs();
				  				    matchdata2.getBody().teammaxdmg();
				  			
				  				  
				  				    Long startTime = matchdata2.getBody().getInfo().getGameCreation();
				  	                Long endTime = matchdata2.getBody().getInfo().getGameEndTimestamp();
				  	                Long resulttime = conveter.convertMilliseconds(endTime-startTime);

					  			    for (int z = 0; z < matchParticipants.size(); z++) {

					  		           if(matchParticipants.get(z).getDeaths() > 0) { 	

					  		        	 kdacalresult = Math.round(((matchParticipants.get(z).getKills()+matchParticipants.get(z).getAssists())/(double)matchParticipants.get(z).getDeaths())*10)/ 10.0;
			  					           }else if(matchParticipants.get(z).getDeaths() ==0){

			  					        	 kdacalresult=10.0; 
				  					           } 

			                         //소환사 이름 받아오기
						  			    String  matchname = matchParticipants.get(z).getSummonerName();
						  			    String  matchname2 = matchParticipants.get(z).getRiotIdGameName();	
						  			       if(matchname.equals(summonername) || matchname2.equals(summonername)) {

			                           //없는 정보 넣어주기
						  			    	  long totalteamkills =
						  			    			  matchdata2.getBody().getInfo().getParticipants().get(z).getTeamId() == 100 ? 
						  			    			  matchdata2.getBody().getInfo().getTeams().get(0).getObjectives().getChampion().getKills()	:
						  			    			  matchdata2.getBody().getInfo().getTeams().get(1).getObjectives().getChampion().getKills();
						  					    Summonerchampions summonerchampions = Summonerchampions.builder()
						  					              // Summoner를 설정
						  					  		.summoner(collectsummoner)
					  					            .position(matchParticipants.get(z).getTeamPosition())
					  					            .champion(matchParticipants.get(z).getChampionName())
					  					            .csaverage(Math.round((matchParticipants.get(z).getTotalMinionsKilled().doubleValue()/resulttime.doubleValue())*10)/10.0)
					  					            .championkda(kdacalresult)
					  					            .damagedealt(matchParticipants.get(z).getTotalDamageDealtToChampions())
					  					            .damagetaken(matchParticipants.get(z).getTotalDamageTaken())
					  					             .win(matchParticipants.get(z).getWin() == true ? 1 : 0)
					  					            .queueId(matchdata2.getBody().getInfo().getQueueId())
					  					            .championkills(matchParticipants.get(z).getKills())
					  					            .championdeaths(matchParticipants.get(z).getDeaths())
					  					            .championassists(matchParticipants.get(z).getAssists())
					  					            .killpers((matchParticipants.get(z).getAssists() + matchParticipants.get(z).getKills())*100 / (totalteamkills == 0 ? 1 : totalteamkills))
					  					            .build();
						  					    summonerchampionsRepository.save(summonerchampions);

					  			    }		 	  				   
					  			   }  
				  			  
				  			  
				  		  }
				  		  
                     } 
						  for (int i = 0; i < matchsssList.size(); i++) {
				  			    matchdata2 = restTemplate.getForEntity(matchsssList.get(i), Match.class);
				  			  matchdata2.getBody().updateCaludate(); 
							  matchdata2.getBody().teammaxdfs();
							  matchdata2.getBody().teammaxdmg();
							  matchsssinfoList.add(matchdata2);
							  
							    Long startTime = matchdata2.getBody().getInfo().getGameCreation();
				                Long endTime = matchdata2.getBody().getInfo().getGameEndTimestamp();
				                Long resulttime = conveter.convertMilliseconds(endTime-startTime);
							  
							  
				  			  
				  			  matchParticipants= matchdata2.getBody().getInfo().getParticipants();
							    for (int z = 0; z < matchParticipants.size(); z++) { 
							   	
							    
						           if(matchParticipants.get(z).getDeaths() > 0) { 		
						        	 kdacalresult = Math.round(((matchParticipants.get(z).getKills()+matchParticipants.get(z).getAssists())/(double)matchParticipants.get(z).getDeaths())*10)/ 10.0;
							           }else if(matchParticipants.get(z).getDeaths() ==0){
							        	 kdacalresult=10.0; 
								           } 
							       //솬사이름
					  			    String  matchname = matchParticipants.get(z).getSummonerName().toLowerCase();
					  			    String  matchname2 = matchParticipants.get(z).getRiotIdGameName().toLowerCase();
							       if(matchname.equals(summonername.toLowerCase()) || matchname2.equals(summonername.toLowerCase())) {
				                  //매치에서 소환사 정보 넣어주기
							    	    summonerGameNumber.add(z);
				                    
					  			    	  long totalteamkills =
					  			    			  matchdata2.getBody().getInfo().getParticipants().get(z).getTeamId() == 100 ? 
					  			    			  matchdata2.getBody().getInfo().getTeams().get(0).getObjectives().getChampion().getKills()	:
					  			    			  matchdata2.getBody().getInfo().getTeams().get(1).getObjectives().getChampion().getKills();

					  					    Summonerchampions summonerchampions = Summonerchampions.builder()
					  					        // Summoner를 설정 디비는 모스트를위해
					  					  		.summoner(collectsummoner)
				  					            .position(matchParticipants.get(z).getTeamPosition())
				  					            .champion(matchParticipants.get(z).getChampionName())
				  					            .csaverage(Math.round((matchParticipants.get(z).getTotalMinionsKilled().doubleValue()/resulttime.doubleValue())*10)/10.0)
				  					            .championkda(kdacalresult)
				  					            .damagedealt(matchParticipants.get(z).getTotalDamageDealtToChampions())
				  					            .damagetaken(matchParticipants.get(z).getTotalDamageTaken())
				  					             .win(matchParticipants.get(z).getWin() == true ? 1 : 0)
				  					            .queueId(matchdata2.getBody().getInfo().getQueueId())
				  					            .championkills(matchParticipants.get(z).getKills())
				  					            .championdeaths(matchParticipants.get(z).getDeaths())
				  					            .championassists(matchParticipants.get(z).getAssists())
				  					            .killpers((matchParticipants.get(z).getAssists() + matchParticipants.get(z).getKills())*100 / totalteamkills)
				  					            .build();
					  					    
					  					  //모스트정리  
					  					  summonermost.add(summonerchampions);      
							    	      	    
							    	    
						    }			  				   
						   }
						    }	  		  
				  		  
						   //모스트 20인
				  		    long totalWins = summonermost.stream().mapToLong(Summonerchampions::getWin).sum();
				  		    long averageKills = (long) (summonermost.stream().mapToDouble(Summonerchampions::getChampionkills).average().orElse(0.0) * 10) / 10;
				  		    long averageDeaths = (long) (summonermost.stream().mapToDouble(Summonerchampions::getChampiondeaths).average().orElse(0.0) * 10) / 10;
				  		    long averageAssists = (long) (summonermost.stream().mapToDouble(Summonerchampions::getChampionassists).average().orElse(0.0) * 10) / 10;
				  		    double averageKDA = Math.round(summonermost.stream().mapToDouble(Summonerchampions::getChampionkda).average().orElse(0.0) * 100.0) / 100.0;
				  		    double averageKillPer = summonermost.stream().mapToDouble(Summonerchampions::getKillpers).average().orElse(0.0);
				  		    long killconcern=  summonermost.stream().mapToLong(Summonerchampions::getWin).sum() * 100/summonermost.size();
				  		    long roundKillPer  = Math.round(averageKillPer * 10) / 10;  
				  		       allstatistics = new Object[]{
						  			    totalWins,
						  			    averageKills,
						  			    averageDeaths,
						  			    averageAssists,
						  			    averageKDA,
						  			    roundKillPer,
						  			    killconcern,
						  			    matchnum
				  			  }; 
						    
						    
						    
						    
						    
					  		  //모스트추출
					  		  Map<String, DoubleSummaryStatistics> championStatsMap = summonermost.stream()
					  		        .collect(Collectors.groupingBy(
					  		                Summonerchampions::getChampion,
					  		                Collectors.summarizingDouble(Summonerchampions::getChampionkills)
					  		        ));

					  		 championStatisticsss = championStatsMap.entrySet().stream()
					  		        .map(entry -> {
					  		            String championName = entry.getKey();
					  		            DoubleSummaryStatistics killsStats = entry.getValue();

					  		            DoubleSummaryStatistics deathsStats = summonermost.stream()
					  		                    .filter(s -> Objects.equals(s.getChampion(), championName))
					  		                    .collect(Collectors.summarizingDouble(Summonerchampions::getChampiondeaths));

					  		            DoubleSummaryStatistics assistsStats = summonermost.stream()
					  		                    .filter(s -> Objects.equals(s.getChampion(), championName))
					  		                    .collect(Collectors.summarizingDouble(Summonerchampions::getChampionassists));

						  		          DoubleSummaryStatistics kdaStats = summonermost.stream()
							  		                .filter(s -> Objects.equals(s.getChampion(), championName))
							  		                .mapToDouble(s -> s.getChampiondeaths() == 0 ? 10.0 : (s.getChampionkills() + s.getChampionassists()) / (double) s.getChampiondeaths())
							  		                .summaryStatistics();
					  		            
					  		          DoubleSummaryStatistics killperStats = summonermost.stream()
					  		                    .filter(s -> Objects.equals(s.getChampion(), championName))
					  		                    .collect(Collectors.summarizingDouble(Summonerchampions::getKillpers));

					  		            long winSum = summonermost.stream()
					  		                    .filter(s -> Objects.equals(s.getChampion(), championName) && s.getWin() == 1)
					  		                    .mapToLong(Summonerchampions::getWin) // Assuming `getWin` returns 1 for a win
					  		                    .sum();

					  		            return new Object[]{
					  		                    championName,
					  		                    killsStats.getCount(),    // 횟수
					  		                    winSum,                             // 승리 합
					  		                    Math.round(killsStats.getAverage() * 10) / 10,        // kills 평균
					  		                    Math.round(deathsStats.getAverage() * 10) / 10,       // deaths 평균
					  		                    Math.round(assistsStats.getAverage() * 10) / 10,      // assists 평균 
					  		                    String.format("%.2f", kdaStats.getAverage()),         // kda 평균
					  		                    Math.round(killperStats.getAverage() * 10) / 10       //킬관여 평균
					  		            };
					  		        })
					  		        .collect(Collectors.toList());
					  		//정렬맨
					  		Collections.sort(championStatisticsss, Comparator
					  		        .<Object[], Long>comparing(statistics -> (Long) statistics[1])  // 횟수를 기준으로 내림차순 정렬
					  		        .reversed()
					  		        .thenComparing(Comparator.<Object[], Double>comparing(statistics -> Double.valueOf(statistics[6].toString())).reversed())  // 횟수가 같으면 킬관여 평균을 기준으로 내림차순 정렬
					  		);

		  
				  		  
					  		
					  		
					  		
					  		   //타임 스탬프 시작
				  		    for(int i = 0; i < matchstimestamp.size(); i++) {
				  		    	
				  		    int summnosss = summonerGameNumber.get(i) + 1;		  	
				  		   //타임스탬프트들어갔고 	
				            matchdatatimeList = restTemplate.getForEntity(matchstimestamp.get(i), TimeLine.class);
				           
				            //프레임안에 이벤트가 있다 
				            List<Frames>   timelineframes  = matchdatatimeList.getBody().getInfo().getFrames();

							 //경기 소환사 리스트
						      summnnerItemTimeStemp  = new ArrayList<SummonerTimeList>();
						      
							     //스킬담자 
							  summonerskilllup = new ArrayList<Long>();
				 
				            //프레임 길이 별로 확인
				           for(int f = 0 ; f <timelineframes.size(); f++ ) {


				        	   summonerItemPurchasedEvents  =   timelineframes.get(f).filterItemPurchasedEvents();
					        	 List<Events>   summonerTimePurchase = summonerItemPurchasedEvents.stream()
				    			 .filter(event -> event.getParticipantId().intValue() == summnosss )
				    			 .collect(Collectors.toList());
					        	 
					        	   summonerItemSkillEvents  =  timelineframes.get(f).filterSkillLevelUpEvents();
					        	   
					        	   List<Long> summonerTimeskill = summonerItemSkillEvents.stream()
					        			    .filter(event -> event.getParticipantId().intValue() == summnosss)
					        			    .map(s -> s.getSkillSlot())
					        			    .filter(skill -> skill != null)  // null 체크
					        			    .filter(skill -> skill != 0)    // 빈 배열이 아닌 경우
					        			    .collect(Collectors.toList());
					        	   
					        	   for(Long s : summonerTimeskill) {
					        		   summonerskilllup.add(s);
					        	   }
					        	 
					        	   skilltree = Calall.getTopNFrequentNumbers(summonerskilllup,3);
				             
							      for(int x =0 ; x<summonerTimePurchase.size() ; x++ ) {			    	
							            SummonerTimeList summonerTime = SummonerTimeList.builder()
							                    .purchasetime(summonerTimePurchase.get(x).getTimestamp() / 60000)
							                    .purchaseitem(summonerTimePurchase.get(x).getItemId())
							                    .build();     
							            summnnerItemTimeStemp.add(summonerTime); 
							            
							            summnoenrss =SummonerTimeListSS.builder()
								        		   .summonerTimeList(summnnerItemTimeStemp)
								        		   .build();

							     }   
						   }    
				           
				           Map<Long, List<Long>> sortedGroupedPurchaseItems = summnnerItemTimeStemp.stream()
				                   .collect(Collectors.groupingBy(
				                           SummonerTimeList::getPurchasetime,
				                           LinkedHashMap::new,
				                           Collectors.mapping(SummonerTimeList::getPurchaseitem, Collectors.toList())
				                   ));

				           List<List<Long>> resultList = sortedGroupedPurchaseItems.entrySet().stream()
				                   .map(entry -> {
				                       List<Long> result = entry.getValue();
				                       result.add(0, entry.getKey());
				                       return result;
				                   })
				                   .collect(Collectors.toList());
				           
				           
				           
					          
						      summnnerItemTimeStempSS.add(resultList);
						      summonerskilllups.add(summonerskilllup);
						      summonerskillluptree.add(skilltree);
						}  		  
				  		  
					  		Map<Long, String>keysquetype = Calall.createQueueTypeMap();
			 		   Map<String, String> keysChamName = ChampionData.keysChamName();
				 		Map<Long, String> keysRuneImage = RuneData.keysRuneImage();
				 		Map<Long, String> keysChamSpell = SummonerData.keysSumSpellLong();
				 		Map<String, List<Spell>> keysChamSkill = ChampionData.keysChamSkill();
				 		Map<Integer, Integer> keysRunes=RuneData.keysRunes();
				 		List<Runes> runeslist = RuneData.runeslist();
				 		List<Perk> perklist = RuneData.perklist();
	/////////////////////////////////////////////////////////////////////////////////////////////////////				     
					     //따끈한 서머너
					   Summoner  newcollectsummoner  = summonerRepository.findBySummonernameAndTag(summonername,tag);
					     //소환사//챔이름 ,kda,cs,가한딜,받은딜,판수,승수 
					   List<Object[]> summonerchamsper =summonerchampionsRepository.findAvgStatsAndCountByChampionGroupByChampion(findsummoner.getId()); 
					   
					    model.addAttribute("summoner", newcollectsummoner);
					    
					    //이건통계라 넣자
					    model.addAttribute("summonermatchnum", summonerGameNumber);
					    model.addAttribute("summonerchamsper", summonerchamsper);
					    model.addAttribute("matchsssinfoList", matchsssinfoList);
				  		model.addAttribute("matchspurchase", summnnerItemTimeStempSS);
				  		model.addAttribute("matchsskill", summonerskilllups);
				  		model.addAttribute("matchsskilltree", summonerskillluptree);
				  		//전체 경기
				  		model.addAttribute("summonerall", allstatistics);
				  		//이름 판수 승수  k d a kda 킬관여
				  		model.addAttribute("summonermost", championStatisticsss);
				  		//챔피언이름 변환
				  		model.addAttribute("keysChamName", keysChamName);
				  		//룬 아이디 이름으로 변환
				  		model.addAttribute("keysRuneImage", keysRuneImage);
				  		//소환사 주문 넘버 스트링변환
				  		model.addAttribute("keysChamSpell", keysChamSpell);  
				  		//챔피언당 스킬 불러오기
				  		model.addAttribute("keysChamSkill", keysChamSkill);
				  		//룬리스트
				  		model.addAttribute("runeslist", runeslist);
				  		model.addAttribute("keysRunes", keysRunes);
				  		//perk 데이터 리스트
				  		model.addAttribute("perklist", perklist);
				  		//큐타입 추가 
				  		model.addAttribute("keysquetype", keysquetype);
    	    }
    	       
    }
 
		
	  return "power"; 

   } 
  
   

  
  
  }

 