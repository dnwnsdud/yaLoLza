package com.web.project.system;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.project.dto.championstats.AramChampionDTO;
import com.web.project.system.AramStatsJsonReader;

@Service
public class AramDataService {

    private final AramStatsJsonReader aramStatsJsonReader;

    @Autowired
    public AramDataService(AramStatsJsonReader aramStatsJsonReader) {
        this.aramStatsJsonReader = aramStatsJsonReader;
    }

    public List<AramChampionDTO> getAramChampionData() {
        try {
            AramChampionDTO[] aramChampions = aramStatsJsonReader.readAramStatsJsonFile();
            List<AramChampionDTO> championsList = Arrays.asList(aramChampions);
             
            return championsList.stream()
                    .filter(c -> c.getStats() != null)
                    .sorted((c1, c2) -> Integer.compare(c2.getStats().getTierScore(), c1.getStats().getTierScore()))
                    .collect(Collectors.toList()); 
            
        } catch (IOException e) {
            System.out.println("ERROR : " + e.getMessage());
            return new ArrayList<>();
        }
    }
}