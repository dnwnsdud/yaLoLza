package com.web.project.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.project.dao.DuoRepository;
import com.web.project.dao.MostChampionsRepository;
import com.web.project.dto.Duo;
import com.web.project.dto.MostChampions;
import com.web.project.dto.enumerated.Myposition;
import com.web.project.dto.enumerated.Queuetype;
import com.web.project.dto.enumerated.Yourposition;

@Service
public class Duoservice {
	private final DuoRepository duoRepository;
	private final MostChampionsRepository mostChampionsRepository;
	
	@Autowired
	MostChampionsService mostChampionsService;
	
    @Autowired
    public Duoservice(DuoRepository duoRepository, MostChampionsRepository mostChampionsRepository) {
        this.duoRepository = duoRepository;
		this.mostChampionsRepository = mostChampionsRepository;
    }

    public List<Duo> getAllDuos() {
    	return duoRepository.findAllByOrderByIdDesc();
    	}
    
    public List<Duo> getDuoByPosition(Myposition myposition) {
        return duoRepository.findByMypositionOrderByIdDesc(myposition);
    }
    public List<Duo> getDuoByQueuetype(Queuetype queuetype) {
        return duoRepository.findByQueuetypeOrderByIdDesc(queuetype);
    }
    public List<Duo> getDuoByYourposition(Yourposition yourposition) {
    	return duoRepository.findByYourpositionOrderByIdDesc(yourposition);
    }
    public Duo duoview(Long id) {
    	return duoRepository.findById(id).get();
    }
 
    public void duoDelete(Long id){ 
    	duoRepository.deleteById(id);
    }
    
    public List<MostChampions> findMostChampionsByDuoId(Long duoId) {
        return mostChampionsRepository.findByDuoIdOrderByDuoIdDesc(duoId);
    }
    public List<Duo> findAllByOrderByCreatedDateDesc() {
    	return duoRepository.findAllByOrderByCreatedDateDesc();
    }
    
//	public boolean isPasswordMatching(Long id, Long duopassword) {
//	    // 비밀번호 유효성 검증 로직 추가
//	    Duo duo = duoRepository.findById(id).orElse(null);
//	    return duo != null && duopassword.equals(duo.getDuopassword());
//	}
	

  
	/*
	 * public List<Duo> getDuoByTiertype(Tier tier) { return
	 * duoRepository.findByTierOrderByIdDesc(tier); }
	 */
    
    public List<Duo> getAllDuosWithMostChampions() {
        List<Duo> duoList = duoRepository.findAllByOrderByCreatedDateDesc(); // 먼저 모든 Duo 객체를 가져옵니다.

        // 각 Duo 객체마다 MostChampions 정보를 가져와서 설정합니다.
        for (Duo duo : duoList) {
            List<MostChampions> mostChampions = mostChampionsService.findByDuoId(duo.getId());
            duo.setMostChampions(mostChampions);
        }

        return duoList;
    }
}

