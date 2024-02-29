package com.web.project.dao.summoner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.web.project.dto.summoner.Summoner;
import com.web.project.dto.summoner.Summonerchampions;
import com.web.project.dto.summoner.Summonermatchs;



public interface SummonermatchsRepository extends JpaRepository<Summonermatchs, Long> {
         
	  List<Summonermatchs> findBySummoner(Summoner summoner);
	  
	//  List<Double> findAllCsAveragesByChampion(String champion);
}
