package com.web.project.dao.summoner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.web.project.dto.sjw.summoner.Summoner;
import com.web.project.dto.sjw.summoner.Summonerchampions;

public interface SummonerchampionsRepository extends JpaRepository<Summonerchampions, Long> {
       
	 //특정 소환사 에 대한 값
	 List<Summonerchampions> findBySummoner(Summoner summoner);
	  
	 //챔이름 ,kda,cs,가한딜,받은딜,판수,승수
	 @Query("SELECT s.champion, ROUND(AVG(s.championkda), 2), AVG(s.csaverage), AVG(s.damagedealt), AVG(s.damagetaken), COUNT(s.champion), SUM(s.win),AVG(s.goldearned),AVG(s.championkills),AVG(s.championdeaths),AVG(s.championassists) FROM Summonerchampions s WHERE s.summoner.id = :summoner GROUP BY s.champion ORDER BY COUNT(s.champion) DESC")
	 List<Object[]> findAvgStatsAndCountByChampionGroupByChampion(@Param("summoner") Long summonerId);
	 
}
