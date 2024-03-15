package com.web.project.function;

import java.util.ArrayList;


import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.web.project.dto.sjw.match.Match;
import com.web.project.dto.sjw.match.Participants;
import com.web.project.dto.sjw.summoner.SummonerResponse;
import com.web.project.dto.sjw.summoner.Summonerchampions;
import com.web.project.dto.sjw.summoner.Summonermatchs;

@Component
 public class Calall {

   @Value("${lol.apikey}") 
   private String apiKey;
   
    @Autowired
       private MillisecondsConverter conveter;
   
   private final RestTemplate restTemplate;
     
   @Autowired 
   public Calall(RestTemplateBuilder restTemplateBuilder) {
   this.restTemplate = restTemplateBuilder.build();
   }
      
     //매치비교 
     public static List<String> nocompareLists(List<String> matchnumList, List<String> matchidsList) {
         
        
           List<String> nonMatchingValues = matchidsList.stream()
                   .filter(value -> !matchnumList.contains(value))
                   .collect(Collectors.toList());

           return  nonMatchingValues;
       }
    
      //매치 정렬
       public static List<String> sortListByNumber(List<String> inputList) {
           return inputList.stream()
                   .sorted(Comparator.comparing(Calall::extractNumber).reversed())
                   .collect(Collectors.toList());
       }

       private static long extractNumber(String str) {
           // "KR_" 다음에 오는 숫자를 추출하는 메서드
           return Long.parseLong(str.substring(3));
       }
       
       //스킬 순서 코드
       public static List<Long> getTopNFrequentNumbers(List<Long> inputList) {
          int count1 =0;
          int count2 =0;
          int count3 =0;
          int scont1 =0;
          int scont2 =0;
          int scont3 =0;
          List<Long>ad = new ArrayList<Long>();
           // 숫자별 빈도수를 저장하는 맵
          List<Long> a =new ArrayList<Long>();
          inputList.forEach(s->{
             if(s!=4) {
                a.add(s);
             }
          });
          
          for(int i = 0 ;  i< a.size() ; i++) {
          
             if(a.get(i) == 1) {
                count1 +=1;
             }else if(a.get(i) == 2) {
                count2 +=1;
            }else if(a.get(i) == 3) {
               count3 +=1;
            }
             
          }
          for (int i = 0 ;  i< a.size() ; i++) {
             if(a.get(i) == 1) {
                scont1 +=1;
                if(count1 == scont1) {
                   ad.add(1L);
                }
             }else if(a.get(i) == 2) {
                scont2 +=1;
                if(count2 ==scont2) {
                   ad.add(2L);
                }
            }else if(a.get(i) == 3) {
               scont3 +=1;
               if(count3 ==scont3) {
                   ad.add(3L);
                }
            }
          }
          

          
   return ad;
       }
       
   //db저장할 칼 데이터
   public List<Object[]>  calDuoMost(String name) throws Exception {
       String summonername =  name.replace("%20", " ");
       String tag = "KR1";
       String summonerurl = null;
       ResponseEntity<SummonerResponse> summoner =null ;
       String puuid = null;
       String  matchurl= null;
       long matchnum = 10;
       Double kdacalresult = null;
       List<Summonerchampions> summonermost =new ArrayList<Summonerchampions>();
       List<Object[]> MostTop3 = null;
       if (name.contains("-")) {
           String[] nameParts = name.split("-");
            summonername = nameParts[0].replace("%20", " ");
            tag = nameParts[1].replace("%20", " ");
            summonerurl =
                   "https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/"+summonername+"/"+tag+"?api_key="+apiKey;
        //태그가 없을때 찾는 유저 저장방식    
        }else {
         summonerurl =
           "https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/"+summonername+"/"+tag+"?api_key="+apiKey; 
        }
       //혹시나 예외처리
       try {
      summoner = restTemplate.getForEntity(summonerurl, SummonerResponse.class);
     } catch (Exception e) {
        // 오류 발생 시 "error"로 리다이렉트
           throw new Exception("소환사 조회를 못해요");
     }
       puuid = summoner.getBody().getPuuid(); 
       
       matchurl = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid;
           
           String finalmatch5url = matchurl + "/ids?start=0&count="+matchnum+"&api_key=" + apiKey;
            ResponseEntity<String[]> match5s = restTemplate.getForEntity(finalmatch5url, String[].class);
            String[] matchids =match5s.getBody();
            
        List<String> matchsssList = new ArrayList<>();
        
         for (int i = 0; i < matchids.length; i++) {
         String matchinfoUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchids[i] + "/?api_key=" + apiKey;
         matchsssList.add(matchinfoUrl);
         }
         
        for (int i = 0; i < matchsssList.size(); i++) {
          ResponseEntity<Match> matchdata2 = restTemplate.getForEntity(matchsssList.get(i), Match.class);
           
          
         Long startTime = matchdata2.getBody().getInfo().getGameCreation();
        Long endTime = matchdata2.getBody().getInfo().getGameEndTimestamp();
        Long resulttime = conveter.convertMilliseconds(endTime-startTime);
        
        
        List<Participants> matchParticipants = matchdata2.getBody().getInfo().getParticipants();

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
                         
                     
                     
                     //매치에서 소환사 정보 넣어주기
                       long totalteamkills =
                             matchdata2.getBody().getInfo().getParticipants().get(z).getTeamId() == 100 ? 
                             matchdata2.getBody().getInfo().getTeams().get(0).getObjectives().getChampion().getKills()   :
                             matchdata2.getBody().getInfo().getTeams().get(1).getObjectives().getChampion().getKills();
                        Summonerchampions summonerchampions = Summonerchampions.builder()
                                  // Summoner를 설정
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
                     
                 summonermost.add(summonerchampions); 

             }                           
            } 
         //모스트리 스트 정리
             Map<String, DoubleSummaryStatistics> championStatsMap = summonermost.stream()
                      .collect(Collectors.groupingBy(
                              Summonerchampions::getChampion,
                              Collectors.summarizingDouble(Summonerchampions::getChampionkills)
                      ));

         List<Object[]> championStatisticsss = championStatsMap.entrySet().stream()
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
                                  // Math.round( kdaStats.getAverage() * 10) / 10,           // kda 평균                                         
                                  String.format("%.2f", kdaStats.getAverage()),         // kda 평균
                                  Math.round(killperStats.getAverage() * 10) / 10       //킬관여 평균
                          };
                      })
                      .collect(Collectors.toList());
              
              //정렬맨
              Collections.sort(championStatisticsss, Comparator
                      .<Object[], Long>comparing(statistics -> (Long) statistics[1])  // 횟수를 기준으로 내림차순 정렬
                      .reversed()
                      .thenComparing(Comparator.<Object[], Double>comparing(statistics -> Double.valueOf(statistics[7].toString())).reversed())  // 횟수가 같으면 킬관여 평균을 기준으로 내림차순 정렬
              ); 
          
          
       MostTop3 = championStatisticsss.stream().limit(3).collect(Collectors.toList());    
          
          
     }
   //이름 횟수  승리합  킬 데스 어시 kda 킬관여
   return  MostTop3;   
        
   }
   
   
    public static Map<Long, String> createQueueTypeMap() {
        Map<Long, String> queueTypeMap = new HashMap<>();
        queueTypeMap.put((long) 400, "일반");  // Normal Draft Pick -> 일반
        queueTypeMap.put((long) 420, "솔랭");  // Ranked Solo/Duo -> 솔랭
        queueTypeMap.put((long) 430, "일반");  // Normal Blind Pick -> 일반
        queueTypeMap.put((long) 440, "자유랭");  // Ranked Flex -> 자랭
        queueTypeMap.put((long) 450, "무작위 총력전");  // ARAM -> 칼바람
        queueTypeMap.put((long) 700, "격전");  // Clash -> 격전
        queueTypeMap.put((long) 800, "AI");   // Deprecated
        queueTypeMap.put((long) 810, "AI");   // Deprecated
        queueTypeMap.put((long) 820, "AI");   // Deprecated
        queueTypeMap.put((long) 830, "AI");   // AI Intro Bot -> AI대전
        queueTypeMap.put((long) 840, "AI");   // AI Beginner Bot -> AI대전
        queueTypeMap.put((long) 850, "AI");   // AI Intermediate Bot -> AI대전
        queueTypeMap.put((long) 1900, "URF");  // URF -> URF
        queueTypeMap.put((long) 920, "포로왕");  // Poro King -> 포로왕
        queueTypeMap.put((long) 1020, "단일");  // One for All -> 단일
        queueTypeMap.put((long) 1300, "돌넥");  // Nexus Blitz -> 돌넥
        queueTypeMap.put((long) 1400, "궁주문서");  // Ultimate Spellbook -> 궁주문서
        queueTypeMap.put((long) 2000, "튜토리얼");  // Tutorial 1 -> 튜토리얼
        queueTypeMap.put((long) 2010, "튜토리얼");  // Tutorial 2 -> 튜토리얼
        queueTypeMap.put((long) 2020, "튜토리얼");  // Tutorial 3 -> 튜토리얼
        return queueTypeMap;
    }
   
   
   
}
