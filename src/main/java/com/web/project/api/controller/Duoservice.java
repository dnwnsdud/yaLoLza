package com.web.project.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.project.dao.DuoRepository;
import com.web.project.dto.Duo;
import com.web.project.dto.enumerated.Myposition;
import com.web.project.dto.enumerated.Queuetype;
import com.web.project.dto.enumerated.Yourposition;

@Service
public class Duoservice {
	private final DuoRepository duoRepository;


	
    @Autowired
    public Duoservice(DuoRepository duoRepository) {
        this.duoRepository = duoRepository;
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
    

    
    
//	public boolean isPasswordMatching(Long id, Long duopassword) {
//	    // 비밀번호 유효성 검증 로직 추가
//	    Duo duo = duoRepository.findById(id).orElse(null);
//	    return duo != null && duopassword.equals(duo.getDuopassword());
//	}
	

  
	/*
	 * public List<Duo> getDuoByTiertype(Tier tier) { return
	 * duoRepository.findByTierOrderByIdDesc(tier); }
	 */
}

