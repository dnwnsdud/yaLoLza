 
package com.web.project.dao.summoner;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.web.project.dto.sjw.summoner.Summoner;
import com.web.project.dto.sjw.summoner.Summonerchampions;

public interface SummonerRepository extends JpaRepository<Summoner, Long> {
	
    //이름과 태그가 일치하면 
	Summoner findBySummonernameAndTag(String summonername, String tag);
	
}