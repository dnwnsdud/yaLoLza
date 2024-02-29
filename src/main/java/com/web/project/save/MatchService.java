
  package com.web.project.save;
  
  import org.springframework.beans.factory.annotation.Autowired; import
  org.springframework.http.ResponseEntity; import
  org.springframework.stereotype.Component;

import com.web.project.dto.match.Match;
  
  @Component
  public class MatchService {

      @Autowired
      private MatchRepository matchRepository;
      
   

      public void saveMatchDataFromJson(ResponseEntity<Match> matchdata2,String matchid) {
          try {
              Match match = matchdata2.getBody();
              match.setMatchnumber(matchid);
              // Match 객체를 데이터베이스에 저장합
              matchRepository.save(match);
          } catch (Exception e) {
              
              e.printStackTrace();
              throw e; // 예외를 다시 던져서 상위 레이어에서 처리하도록 합니다.
          }
      }
  }