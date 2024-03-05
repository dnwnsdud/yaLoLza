package com.web.project.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.project.dto.MostChampions;

public interface MostChampionsRepository extends JpaRepository<MostChampions, Long> {

	void save(List<Object[]> most);
    // 추가적인 메소드가 필요하다면 여기에 선언할 수 있습니다.
	
    public List<MostChampions> findByDuoIdOrderByDuoIdDesc(Long duoId);
    
    
}
