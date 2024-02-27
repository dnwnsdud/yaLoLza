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


@Controller
public class CounterDataController {

    private final CounterJsonReader counterJsonReader;

    @Autowired
    public CounterDataController(CounterJsonReader counterJsonReader) {
        this.counterJsonReader = counterJsonReader;
    }

    @GetMapping("/counter/{position}/champion/{champion}") //http://localhost:9998/counter/top/champion/darius
    public String getCounterData(@PathVariable String position, @PathVariable("champion") String champion,
                                 @RequestParam(value = "target_champion", required = false) String targetChampion, Model model) {
        try {
            CounterPositionDTO counterData = counterJsonReader.readCounterJsonFile();
            List<CounterChampionDTO> targetCounters = new ArrayList<>();

            List<CounterCountDTO> counterCounts = null;
            switch (position.toUpperCase()) {
                case "TOP":
                    counterCounts = counterData.getTop();
                    break;
                case "JUNGLE":
                    counterCounts = counterData.getJungle();
                    break;
                case "MIDDLE":
                    counterCounts = counterData.getMiddle();
                    break;
                case "BOTTOM":
                    counterCounts = counterData.getBottom();
                    break;
                case "UTILITY":
                    counterCounts = counterData.getUtility();
                    break;
                default:
                    return "error";
            }

            if (counterCounts != null) {
                for (CounterCountDTO countDTO : counterCounts) {
                    for (CounterChampionDTO championDTO : countDTO.getCounter()) {
                        if (championDTO.getChampionName().equalsIgnoreCase(champion) &&
                                (targetChampion == null || championDTO.getChampionName().equalsIgnoreCase(targetChampion))) {
                            targetCounters.addAll(countDTO.getCounter());
                            break;
                        }
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
}