
  package com.web.project.save;
  
  import org.springframework.data.jpa.repository.JpaRepository;

import com.web.project.dto.sjw.match.Match;
  
  
  public interface MatchRepository extends JpaRepository<Match, Long> {
	  
      //메치넘버로 정보를 가지고 온다
	  Match findByMatchnumber(String matchnumber);
	  
	  
  }
 