package com.web.project.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.project.dao.MostChampionsRepository;
import com.web.project.dto.MostChampions;

@Service
public class MostChampionsService {

    private final MostChampionsRepository mostChampionsRepository;

    @Autowired
    public MostChampionsService(MostChampionsRepository mostChampionsRepository) {
        this.mostChampionsRepository = mostChampionsRepository;
    }

    public List<MostChampions> findByDuoId(Long duoId) {
        return mostChampionsRepository.findByDuoIdOrderByDuoIdDesc(duoId);
    }
   


    // 다른 필요한 메소드들도 추가 가능
}
