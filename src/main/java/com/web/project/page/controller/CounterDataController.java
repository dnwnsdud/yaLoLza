package com.web.project.page.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.dto.CounterChampionDTO;
import com.web.project.dto.CounterCountDTO;
import com.web.project.dto.CounterPositionDTO;
import com.web.project.system.CounterJsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class CounterDataController {

    private final CounterJsonReader counterJsonReader;

    @Autowired
    public CounterDataController(CounterJsonReader counterJsonReader) {
        this.counterJsonReader = counterJsonReader;
    }

    @GetMapping("/counter/{position}/champion/{champion}")   //  http://localhost:9997/counter/top/champion/darius?champion=Aatrox
    public String getCounterData(@PathVariable String position, 
                                 @PathVariable("champion") String champion,
                                 @RequestParam(name = "champion", required = false) String additionalChampion,
                                 Model model) {
        try {
            CounterPositionDTO counterData = counterJsonReader.readCounterJsonFile();
            List<CounterCountDTO> positionData = getPositionData(position, counterData);

            if (positionData == null) {
                return "error";
            }

            List<CounterChampionDTO> targetCounters = new ArrayList<>();
            for (CounterCountDTO countDTO : positionData) {
                if (additionalChampion == null) {
                    if (countDTO.getCounter().stream()
                            .anyMatch(championDTO -> championDTO.getChampionName().equalsIgnoreCase(champion))) {
                        targetCounters.addAll(countDTO.getCounter());
                    }
                } else {
                    List<CounterChampionDTO> matchedCounters = countDTO.getCounter().stream()
                        .filter(championDTO -> championDTO.getChampionName().equalsIgnoreCase(champion) ||
                                                championDTO.getChampionName().equalsIgnoreCase(additionalChampion))
                        .collect(Collectors.toList());

                    boolean containsBothChampions = matchedCounters.stream()
                        .anyMatch(championDTO -> championDTO.getChampionName().equalsIgnoreCase(champion)) &&
                        matchedCounters.stream()
                        .anyMatch(championDTO -> championDTO.getChampionName().equalsIgnoreCase(additionalChampion));

                    if (containsBothChampions) {
                        targetCounters.addAll(matchedCounters);
                    }
                }
            }
            
    
            model.addAttribute("selectedChampionName", champion);
            model.addAttribute("targetCounters", targetCounters);

            return "counterView";
        } catch (IOException e) {
            model.addAttribute("error", "Data loading error: " + e.getMessage());
            return "error";
        }
    }

    private List<CounterCountDTO> getPositionData(String position, CounterPositionDTO counterData) {
        switch (position.toUpperCase()) {
            case "TOP":
                return counterData.getTop();
            case "JUNGLE":
                return counterData.getJungle();
            case "MIDDLE":
                return counterData.getMiddle();
            case "BOTTOM":
                return counterData.getBottom();
            case "UTILITY":
                return counterData.getUtility();
            default:
                return null;
        }
    }
}
