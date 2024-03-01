package com.web.project.dao.summoner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.project.dto.sjw.summoner.Summoner;
import com.web.project.dto.sjw.summoner.Summonermatchs;
import com.web.project.dto.sjw.summoner.Summonertierlog;

public interface SummonertierlogRepository extends JpaRepository<Summonertierlog, Long> {
	
	 List<Summonertierlog> findBySummoner(Summoner summoner);
	 
	 List<Summonertierlog> findBySummonerId(Long summonerid);
}
